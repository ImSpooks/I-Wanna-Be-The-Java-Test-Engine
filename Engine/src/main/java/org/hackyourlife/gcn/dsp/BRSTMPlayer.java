package org.hackyourlife.gcn.dsp;

import org.hackyourlife.gcn.dsp.file.BRSTMFile;
import org.hackyourlife.gcn.dsp.stream.BRSTMStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by Nick on 18 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class BRSTMPlayer {

    private Stream stream;

    private Thread asyncThread;

    public BRSTMPlayer(Stream stream) {
        this.stream = stream;
    }

    public BRSTMPlayer(InputStream inputStream) {
        this.stream = BRSTMStream.getFromStream(inputStream);
    }

    public BRSTMPlayer(RandomAccessFile randomAccessFile) {
        this.stream = BRSTMFile.getFromFile(randomAccessFile);
    }

    private AsyncDecoder decoder;

    public void start() {
        // check if result isnt null
        if (this.stream == null) {
            throw new NullPointerException("Cannot handle BRSTM player for an empty or undefined stream");
        }

        // if audio is already playing it resets

        // setting up a async thread so the current doesn't freeze so other code in the same thread can continue
        this.asyncThread = new Thread(() -> {
            // starting the brstm file
            this.decoder = new AsyncDecoder(stream);
            this.decoder.start();
            this.play(decoder);

            // closing the result after audio started
            try {
                this.stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.asyncThread.start();
    }

    public void pause() {
        this.decoder.setPaused(true);
    }

    public void resume() {
        this.decoder.setPaused(false);
    }

    public boolean isPaused() {
        return this.decoder.isPaused();
    }

    public void stop() {
        try {
            if (this.decoder == null || this.decoder.isClosed()) {
                throw new IllegalStateException("Cannot close stream that is null or is already closed");
            }
            this.asyncThread.interrupt();
            this.decoder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play(AsyncDecoder stream) {
        try {
            int channels = stream.getChannels();
            if(channels > 2) {
                channels = 2;
            }
            AudioFormat format = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,	// encoding
                    stream.getSampleRate(),			    // sample rate
                    16,					// bit/sample
                    channels,				            // channels
                    2 * channels,
                    stream.getSampleRate(),
                    true					    // big-endian
            );

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if(!AudioSystem.isLineSupported(info)) {
                throw new Exception("Line matching " + info + " not supported");
            }

            SourceDataLine waveout;
            waveout = (SourceDataLine) AudioSystem.getLine(info);
            waveout.open(format, 16384);

            waveout.start();
            while(!stream.isClosed() && stream.hasMoreData()) {
                if (stream.isClosed() || stream.isInterrupted()) {
                    return;
                }

                byte[] buffer = stream.decode();
                if (buffer == null)
                    continue;

                buffer = sum(buffer, stream.getChannels());
                waveout.write(buffer, 0, buffer.length);
            }
            waveout.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] sum(byte[] data, int channels) {
        if(channels == 1 || channels == 2) {
            return data;
        }
        int samples = data.length / (channels * 2);
        byte[] result = new byte[samples * 4]; // 2 channels, 16bit
        for(int i = 0; i < samples; i++) {
            int l = 0;
            int r = 0;
            for(int ch = 0; ch < channels; ch++) {
                int idx = (i * channels + ch) * 2;
                short val = (short) (Byte.toUnsignedInt(data[idx]) << 8 | Byte.toUnsignedInt(data[idx + 1]));
                if((ch & 1) == 0) {
                    l += val;
                } else {
                    r += val;
                }
            }
            // clamp
            if(l < -32768) {
                l = -32768;
            } else if(l > 32767) {
                l = 32767;
            }
            if(r < -32768) {
                r = -32768;
            } else if(r > 32767) {
                r = 32767;
            }
            // write back
            result[i * 4] = (byte) (l >> 8);
            result[i * 4 + 1] = (byte) l;
            result[i * 4 + 2] = (byte) (r >> 8);
            result[i * 4 + 3] = (byte) r;
        }
        return result;
    }

}
