package org.hackyourlife.gcn.dsp;

import org.hackyourlife.gcn.dsp.input.InputData;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RS03 implements Stream {
	public final static int HEADER_SIZE = 0x60;

	long	sample_count;
	long	nibble_count;
	long	sample_rate;
	int	loop_flag;
	long	loop_start_offset;
	long	loop_end_offset;
	long	channel_count;
	int	coef[][]; /* really 8x2 */

	InputData inputData;
	long	startoffset;
	long	filepos;
	long	filesize;

	long	current_sample;
	long	current_byte;

	long	interleave_block_size;
	long	interleave_smallblock_size;

	ADPCMDecoder decoder[];

	public RS03(InputData file) throws FileFormatException, IOException {
		this.inputData = file;
		this.filesize = file.length();
		if(!readHeader())
			throw new FileFormatException("not a RS03 file");
		reset();
	}

	public RS03(RandomAccessFile file) throws IOException, FileFormatException {
		this(InputData.getInputData(file));
	}

	public RS03(InputStream stream) throws IOException, FileFormatException {
		this(InputData.getInputData(stream));
	}

	public final static int unsigned2signed16bit(int x) {
		int sign = x & (1 << 15);
		int value = x & ~(1 << 15);
		return (sign != 0) ? -(~x & 0xFFFF) : value;
	}

	public boolean read_dsp_header(byte[] header, InputData in) throws IOException {
		if(endianess.get_32bitBE(header, 0x00) != 0x52530003)
			return(false);

		channel_count =
			endianess.get_32bitBE(header, 0x04);

		sample_count =
			endianess.get_32bitBE(header, 0x08);
		sample_rate =
			endianess.get_32bitBE(header, 0x0C);
		nibble_count =
			endianess.get_32bitBE(header, 0x10);
		loop_flag =
			endianess.get_16bitBE(header, 0x14);
		loop_start_offset =
			endianess.get_32bitBE(header, 0x18);
		loop_end_offset =
			endianess.get_32bitBE(header, 0x1C);

		coef = new int[(int)channel_count][16];
		byte[] buf = new byte[(int) (0x20 * channel_count)];
		in.read(buf);
		for(int c = 0; c < channel_count; c++)
			for(int i = 0; i < 16; i++)
				coef[c][i] =
					unsigned2signed16bit(endianess.get_16bitBE(buf, (c * 0x20) + i * 2));

		return(true);
	}

	@Override
	public long getSampleRate() {
		return(sample_rate);
	}

	@Override
	public int getChannels() {
		return((int)channel_count);
	}

	public long getInterleaveSize() {
		return interleave_block_size;
	}

	public long getPreferedBufferSize() {
		return getChannels() * (long)(getInterleaveSize() / 8.0 * 14.0);
	}

	public boolean open(String filename, boolean resource) throws Exception {
		if(inputData != null)
			close();
		if (!resource)
			inputData = InputData.getInputData(new RandomAccessFile(filename, "r"));
		else
			inputData = InputData.getInputData(this.getClass().getResourceAsStream(filename));
		filesize = inputData.length();
		return readHeader();
	}

	private boolean readHeader() throws IOException {
		seek(0);
		startoffset = 0x20;

		byte[] header = new byte[0x20];
		inputData.read(header);
		if(!read_dsp_header(header, inputData))
			return(false);

		startoffset += 0x20 * channel_count;

		interleave_block_size = 0x8f00;
		interleave_smallblock_size = (((filesize - startoffset) % (0x8f00 * 2)) / 2 + 7) / 8 * 8;

		decoder = new ADPCMDecoder[(int)channel_count];
		for(int i = 0; i < channel_count; i++) {
			decoder[i] = new ADPCMDecoder();
			decoder[i].setCoef(coef[i]);
			decoder[i].setHistory(0, 0);
		}

		filepos = startoffset;
		current_sample = 0;
		current_byte = 0;

		return(true);
	}

	@Override
	public void close() throws Exception {
		inputData.close();
	}

	@Override
	public boolean hasMoreData() {
		return((loop_flag != 0) || (filepos < filesize));
	}

	private void seek(long pos) throws IOException {
		inputData.seek(startoffset + pos);
		filepos = startoffset + pos;
	}

	public void reset() throws IOException {
		seek(0);
		for(int i = 0; i < channel_count; i++)
			decoder[i].setHistory(0, 0);
	}

	private int[] doDecode() throws Exception {
		long start_offset = current_byte;
		if((loop_flag != 0) && (current_byte >= loop_end_offset))
			start_offset = loop_start_offset;
		long blocks = start_offset / interleave_block_size;
		if((loop_flag != 0) && (current_byte >= loop_end_offset))
			current_byte = blocks * interleave_block_size;
		else
			current_byte = start_offset;
		seek(blocks * interleave_block_size * channel_count);

		start_offset = start_offset - (blocks * interleave_block_size);
		start_offset = (start_offset / 8) * 8;
		int startsample = (int)(start_offset / 8.0 * 14.0);

		long interleave = interleave_block_size;
		if((filesize - filepos) < (interleave_block_size * channel_count))
			interleave = interleave_smallblock_size;

		long end_offset = current_byte + interleave;
		if((loop_flag != 0) && ((current_byte + interleave) > loop_end_offset))
			end_offset = loop_end_offset;

		int endsample = (int)((end_offset - current_byte) / 8.0 * 14.0);

		byte[] rawdata = new byte[(int)(interleave * channel_count)];
		int samplecnt = endsample - startsample;
		if(samplecnt < 0)
			samplecnt = 0;
		int[] samples = new int[(int)(samplecnt * channel_count)];
		int read = inputData.read(rawdata);
		filepos += read;
		current_byte += read / channel_count;
		for(int ch = 0; ch < channel_count; ch++) {
			for(int i = 0; i < samplecnt; i += 14) {
				int samplestodo = samplecnt - i;
				if(samplestodo > 14)
					samplestodo = 14;
				int[] buf = decoder[ch].decode_ngc_dsp((int)(interleave * ch), startsample + i, samplestodo, rawdata);
				for(int x = 0; x < buf.length; x++)
					samples[(int)((i + x) * channel_count + ch)] = buf[x];
			}
		}
		return samples;
	}

	@Override
	public byte[] decode() throws Exception {
		int[] samples = doDecode();
		byte[] buffer = new byte[samples.length * 2];
		for(int i = 0; i < samples.length; i++)
			endianess.set16bit_BE(samples[i], buffer, i * 2);
		return(buffer);
	}

	public short[] decode16() throws Exception {
		int[] samples = doDecode();
		short[] buffer = new short[samples.length];
		for(int i = 0; i < samples.length; i++)
			buffer[i] = (short) samples[i];
		return(buffer);
	}

	@Override
	public String toString() {
		return(new String("RS03[" + sample_rate + "Hz,16bit," + sample_count + " samples,loop:" + ((loop_flag != 0) ? "yes" : "no") + "," + channel_count + "ch]"));
	}
}