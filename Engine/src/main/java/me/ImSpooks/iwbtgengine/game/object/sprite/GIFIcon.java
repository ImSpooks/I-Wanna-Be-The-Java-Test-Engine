package me.ImSpooks.iwbtgengine.game.object.sprite;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;

/**
 * GIFIcon is an Icon for displaying animations or single images.
 * Any src format that ImageIO can read is fair game.  So for example,
 * if you have a TIFF reader plugin then a multi-frame tiff can be made into
 * a slide show using this class.
 * <p>
 * Unlike the ImageIcon class which uses the method Toolkit#getImage, this
 * class does not cache the BufferedImages and other data read from a file or
 * url.  So if you need multiple GIFIcon instances from the same source,
 * consider using the clone() method to reduce memory footprint.
 * <p>
 * This class provides some support for persistant storage via XML.  The
 * original String that was used to construct the icon is saved as the bean
 * property <code>source</code>.
 * <p>
 * <i><b>Note regarding GIF Images</b></i>
 * <p>
 * If a GIF file is used as the source then this class will extract the
 * necessary information (images, delays, offsets, disposal) to properly
 * display the animation. Just like with internet browsers, chat programs,
 * and other src viewers, if the GIF source does not specify any durations
 * (that is, the durations are 0) then an arbitrary frame-duration of 10
 * centiseconds will be induced when the icon is constructed.  To change this
 * value, change the class variable <code>DEFAULT_DURATION</code> (in centiseconds).
 * <p>
 * Because an icon cannot know when it is removed from a Component, it is good
 * practice to call <code>dispose()</code> on animated GIFIcons when done
 * with them.  The component that an animated icon belonged to will continue to
 * be repainted until <code>dispose()</code> is called or the icon is finalized.
 * In the specific case of an icon's <code>updateOnlyOnPaint</code>
 * flag being true, failing to call dispose() may affect other animations on
 * screen.  By default, <code>updateOnlyOnPaint</code> is false.
 *
 *
 * @author Maxideon (java.sun forums)
 * @since 2009
 */
public class GIFIcon extends javax.swing.ImageIcon implements Cloneable{

    /**The GIF Frame disposal methods*/
    public static enum Disposal {
        /**Treated the same as DO_NOT_DISPOSE*/
        UNSPECIFIED ("none","undefinedDisposalMethod4","undefinedDisposalMethod5",
                "undefinedDisposalMethod6","undefinedDisposalMethod7"),
        /**Any pixels not covered by the next frame continues to display. However,
         * even if the last frame is marked as DO_NOT_DISPOSE, the area will be
         * cleared when the animation starts over.*/
        DO_NOT_DISPOSE("doNotDispose"),

        /**There exists two intepretations of this dispose method.  One
         * interpretation is that the curent frame is restored to a background
         * color before rendering the next frame.  The second interpretation
         * is that the frame is entirely cleared allowing the background of the
         * web browser to show.  The later interpretation is used in this case.*/
        RESTORE_TO_BACKGROUND("restoreToBackgroundColor"),

        /**Resores the src to the previous frame marked by UNSPECIFIED
         * or DO_NOT_DISPOSE*/
        RESTORE_TO_PREVIOUS("restoreToPrevious");

        private String[] gifMetaNames;
        private Disposal(String ... gifMetaNames) {
            this.gifMetaNames = gifMetaNames;
        }

        public static Disposal disposalForString(String s) {
            for(Disposal d : Disposal.values()) {
                for(String validName : d.gifMetaNames) {
                    if(validName.equals(s)) {
                        return d;
                    }
                }
            }
            return UNSPECIFIED;
        }
    }

    /**The default frame duration. 10 centiseconds.  See class comments.*/
    public static int DEFAULT_DURATION = 10;

    //the images are drawn onto the buffer according to the disposal methods
    private BufferedImage buffer;

    //if applicable, the graphics object for drawing on the buffer
    private Graphics2D bufferGraphics;

    //user set information
    private int userDelay = DEFAULT_DURATION;
    private int currentIndex;
    private volatile boolean paused;
    private boolean useSourceDelay;

    //the source file/url and (possibly) the src description
    private String source;

    //information obtained from source file/url.
    private Image[] images;
    private short[] durations; //hundredths of a second
    private short[] xOffsets;
    private short[] yOffsets;
    private Disposal[] disposalMethods;

    //The background color of the global pallete in the GIF file is not
    //used.  It is here merely in case you want to change the implementation
    //of RESTORE_TO_BACKGROUND disposal method.
    private Color backgroundColor;

    //Note that frames do not have to fill up the whole animation.  This
    //is the reasons for the offsets for each src within the animation.
    //The icon width and height is the maximum of all the images'
    //widths/heights plus their offsets.
    private int iconWidth;
    private int iconHeight;

    private IconRepaintManager repainter;

    //Controls whether an animation should progress to the next frame only
    //when it has been assured the previous frame was painted onscreen.
    private boolean updateOnlyOnPaint = false;
    private boolean canMoveToNextFrame;

    //The amount of time in nanoseconds that the current frame has been
    //displayed (if an animation).
    private long currentDuration;

    private final Lock paintingLock =
            new java.util.concurrent.locks.ReentrantLock(false);

    /**Creates an empy icon.*/
    public GIFIcon() {}
    /**Creates an icon loaded from encoded bytes.  The encoded bytes must be in
     * a format that ImageIO recognizes.  No src description is set.
     * @param imageData encoded src data
     */
    public GIFIcon(byte[] imageData) {
        this(imageData,null);
    }
    /**Creates an icon loaded from encoded bytes and sets the icon's
     * description.  The encoded bytes must be in a format that ImageIO
     * recognizes.  The desciption is meant to be a brief textual description
     * of the src (as would appear in a tool tip).
     */
    public GIFIcon(byte[] imageData, String description) {
        setSource(imageData);
        setDescription(description);
    }
    /**Creates an icon with the given src.*/
    public GIFIcon(Image image) {
        setSource(image);
    }
    /**Creates an icon loaded from the resource at the indicated string.
     * The string can be an absolute path, a relative path (to user.dir or
     * Thread#getContextClassLoader), or any valid url.  If an IOException
     * occurs reading from the resource, then an error will be printed to
     * System.err.
     * <p>
     * The description is initialized to the <code>source</code> string.*/
    public GIFIcon(String source) {
        this(source,source);
    }
    /**Creates an icon loaded from the resource at the indicated string.
     * The string can be an absolute path, a relative path (to user.dir or
     * Thread#getContextClassLoader), or any valid url.  If an IOException
     * occurs reading from the file, then an error will be printed.  The
     * desciption is meant to be a brief textual description of the src
     * (as would appear in a tool tip).*/
    public GIFIcon(String source, String description) {
        setSource(source);
        setDescription(description);
    }
    /**Creates an icon loaded from a url.  If an IOException occurs reading
     * from the url, then an error will be printed to System.err
     * <p>
     * The description is initialized to <code>url.toExternalForm()</code>*/
    public GIFIcon(java.net.URL url) {
        this(url,url.toExternalForm());
    }
    /**Creates an icon loaded from a url.  If an IOException occurs reading
     * from the url, then an error will be printed to System.err.  The
     * description is meant to be a brief textual description of the src
     * (as would appear in a tool tip).*/
    public GIFIcon(java.net.URL url, String description) {
        setSource(url);
        setDescription(description);
    }
    /**Convenience method do all the Exception handeling stuff when reading
     * an src.*/
    private void init(Object source, java.io.InputStream sourceStream) {
        ImageInputStream imageStream = null;
        try{
            imageStream = ImageIO.createImageInputStream(
                    sourceStream == null?source:sourceStream);
            if(imageStream != null) {
                loadFromStream(imageStream);
                useSourceDelay = true;
            }else {
                System.err.println("Could not create image stream to: " + source);
            }
        }catch(java.io.IOException e) {
            System.err.println("IOException occured reading inpustream. " +
                    "Could not create animation. Message: " + e.getMessage() +
                    " Source: " + source);
        }finally {
            if(imageStream != null)
                try{
                    imageStream.close();
                }catch(java.io.IOException e) {
                    System.err.println("Could not close ImageStream to: "+source);
                }
            if(sourceStream != null)
                try{
                    sourceStream.close();
                } catch (java.io.IOException e) {
                    System.err.println("Could not close InputStream to: "+source);
                }
        }

        initBuffer();
    }
    /**This method updates the icon width and height, as well as the size
     * of a back buffer that is used by this class to properly display an
     * animation.  This method only needs to be be called after any changes
     * are made to the images, the offsets, or the disposals.*/
    private void initBuffer() {
        iconWidth = iconHeight = 0;
        for (int i = 0; i < getFrameCount(); i++) {
            Image img = getImage(i);

            iconWidth = Math.max(iconWidth,
                    (img==null?0:img.getWidth(null)) + getXOffset(i));
            iconHeight = Math.max(iconHeight,
                    (img==null?0:img.getHeight(null)) + getYOffset(i));
        }

        boolean needBuffer = false;
        for (int i = 0; i < getFrameCount()-1; i++) {
            if (getDisposalMethod(i) != Disposal.RESTORE_TO_BACKGROUND) {
                needBuffer = true;
                break;
            }
        }

        if (needBuffer) {
            buffer = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration()
                    .createCompatibleImage(iconWidth, iconHeight,
                            java.awt.Transparency.BITMASK);
            bufferGraphics = buffer.createGraphics();
        } else {
            buffer = null;
            bufferGraphics = null;
        }
        paintCurrentFrame();
    }
    /**Loads all the frames in an src from the given ImageInputStream.
     * Furthermore, if the src references a GIF src then information
     * on frame durations, offsets, and disposal methods will be extracted
     * (and stored).  The src stream is not closed at the end of the method.
     * It is the duty of the caller to close it if they so wish.*/
    private void loadFromStream(ImageInputStream imageStream)
            throws java.io.IOException {

        //obtain an appropriate src reader
        java.util.Iterator<ImageReader> readers =
                ImageIO.getImageReaders(imageStream);

        ImageReader reader = null;
        while(readers.hasNext()) {
            reader = readers.next();

            String metaFormat = reader.getOriginatingProvider()
                    .getNativeImageMetadataFormatName();
            if("gif".equalsIgnoreCase(reader.getFormatName()) &&
                    !"javax_imageio_gif_image_1.0".equals(metaFormat)) {
                continue;
            }else {
                break;
            }
        }
        if(reader == null) {
            throw new java.io.IOException("Can not read image format!");
        }

        boolean isGif = reader.getFormatName().equalsIgnoreCase("gif");

        reader.setInput(imageStream,false,!isGif);

        //before we get to the frames, determine if there is a background color
        IIOMetadata globalMeta = reader.getStreamMetadata();
        if(globalMeta != null && "javax_imageio_gif_stream_1.0".equals(
                globalMeta.getNativeMetadataFormatName())) {

            IIOMetadataNode root = (IIOMetadataNode)
                    globalMeta.getAsTree("javax_imageio_gif_stream_1.0");

            IIOMetadataNode colorTable = (IIOMetadataNode)
                    root.getElementsByTagName("GlobalColorTable").item(0);

            if (colorTable != null) {
                String bgIndex = colorTable.getAttribute("backgroundColorIndex");

                IIOMetadataNode colorEntry = (IIOMetadataNode) colorTable.getFirstChild();
                while (colorEntry != null) {
                    if (colorEntry.getAttribute("index").equals(bgIndex)) {
                        int red = Integer.parseInt(colorEntry.getAttribute("red"));
                        int green = Integer.parseInt(colorEntry.getAttribute("green"));
                        int blue = Integer.parseInt(colorEntry.getAttribute("blue"));

                        backgroundColor = new java.awt.Color(red, green, blue);
                        break;
                    }

                    colorEntry = (IIOMetadataNode) colorEntry.getNextSibling();
                }
            }
        }

        //now we read the images, delay times, offsets and disposal methods
        List<BufferedImage> frames    = new ArrayList<BufferedImage>();
        List<Integer>       delays    = new ArrayList<Integer>();
        List<Integer>       lOffsets  = new ArrayList<Integer>();
        List<Integer>       tOffsets  = new ArrayList<Integer>();
        List<Disposal>      disposals = new ArrayList<Disposal>();

        boolean unkownMetaFormat = false;
        for(int index = 0;;index++) {
            try {
                //read a frame and its metadata
                javax.imageio.IIOImage frame = reader.readAll(index,null);

                //add the frame to the list
                frames.add(forceNonCustom((BufferedImage) frame.getRenderedImage()));

                if(unkownMetaFormat)
                    continue;

                //obtain src metadata
                javax.imageio.metadata.IIOMetadata meta = frame.getMetadata();

                IIOMetadataNode imgRootNode = null;
                try{
                    imgRootNode = (IIOMetadataNode)
                            meta.getAsTree("javax_imageio_gif_image_1.0");
                }catch(IllegalArgumentException e) {
                    //unkown metadata format, can't do anyting about this
                    unkownMetaFormat = true;
                    continue;
                }

                IIOMetadataNode gce = (IIOMetadataNode)
                        imgRootNode.getElementsByTagName("GraphicControlExtension").item(0);

                delays.add(Integer.parseInt(gce.getAttribute("delayTime")));
                disposals.add(Disposal.disposalForString(gce.getAttribute("disposalMethod")));

                IIOMetadataNode imgDescr = (IIOMetadataNode)
                        imgRootNode.getElementsByTagName("ImageDescriptor").item(0);

                lOffsets.add(Integer.parseInt(imgDescr.getAttribute("imageLeftPosition")));
                tOffsets.add(Integer.parseInt(imgDescr.getAttribute("imageTopPosition")));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        //clean up
        reader.dispose();

        //copy the source information into their respective arrays
        if(!frames.isEmpty()) {
            images = frames.toArray(new BufferedImage[]{});
        }
        if(!delays.isEmpty()) {
            durations = new short[delays.size()];
            int i = 0;
            for (int duration : delays)
                durations[i++] = (short) (duration == 0?DEFAULT_DURATION:
                        duration);
        }
        if(!lOffsets.isEmpty()) {
            xOffsets = new short[lOffsets.size()];
            int i = 0;
            for(int offset : lOffsets)
                xOffsets[i++] = (short) offset;
        }
        if(!tOffsets.isEmpty()) {
            yOffsets = new short[tOffsets.size()];
            int i = 0;
            for(int offset : tOffsets)
                yOffsets[i++] = (short) offset;
        }
        if(!disposals.isEmpty()) {
            disposalMethods = disposals.toArray(new Disposal[]{});
        }
    }
    private BufferedImage forceNonCustom(BufferedImage src) {
        if(src.getType() != BufferedImage.TYPE_CUSTOM) {
            return src;
        }else {
            int type;
            if(src.getColorModel().hasAlpha()) {
                type = BufferedImage.TYPE_INT_ARGB;
            }else {
                if(src.getColorModel().getColorSpace().getType() ==
                        java.awt.color.ColorSpace.TYPE_GRAY) {
                    type = BufferedImage.TYPE_BYTE_GRAY;
                }else {
                    type = BufferedImage.TYPE_3BYTE_BGR;
                }
            }
            BufferedImage dst = new BufferedImage(
                    src.getWidth(),src.getHeight(),type);

            return new java.awt.image.ColorConvertOp(null).filter(src, dst);
        }
    }
    /**Clones the icon.*/
    @Override
    public GIFIcon clone() {
        try {
            return (GIFIcon) super.clone();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    /**If possible, this method returns the original String that was used to
     * construct the icon.  Combined with <code>setSource</code>, these methods
     * allow persistant storage of an icon via the XML encoder/decoder.*/
    public Object getSource() {
        return source;
    }
    /**Reinitializes the icon to the given source.  The source may be any
     * one of the following types:<br>
     * <pre>
     * java.lang.String (relative or absolute filename, or url)
     * java.io.File
     * java.net.URL
     * byte[] (the encoded bytes of any format that ImageIO will recognize)
     * java.awt.Image
     * java.awt.Image[]
     * </pre>
     * <br>
     * In the specific cases of a String, File, or URL then the String
     * representation of the source will be saved and returned by calls to
     * <code>getSource()</code>.  This provides some XML encoding/decoding
     * support.
     * <p>
     * This method is thread safe.*/
    public void setSource(Object src) {
        dispose();

        synchronized (paintingLock) {
            if (src == null) {
                return;
            }
            if (src instanceof String || src instanceof java.io.File ||
                    src instanceof java.net.URL) {
                setDescription(src.toString());
            }
            if (src instanceof String) {
                this.source = src.toString();
                //Is it a file?
                try {
                    java.io.File file = new java.io.File(src.toString());
                    if (file.exists() && file.canRead()) {
                        init(src, new java.io.FileInputStream(file));
                        return;
                    }
                } catch (Exception eaten) {}
                //Is is a jar resource?
                try {
                    java.io.InputStream urlResource = Thread
                            .currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(src.toString());
                    if (urlResource != null) {
                        init(src, urlResource);
                        return;
                    }
                } catch (Exception eaten) {}
                //Is it an internet address?
                try {
                    java.net.URL url = new java.net.URL(src.toString());
                    init(src, url.openStream());
                    return;
                } catch (Exception eaten) {}
                System.err.println("In GIFIcon#setSource; " +
                        "Could not find: " + "\"" + src + "\"");
            } else if (src instanceof java.io.File) {
                this.source = src.toString();
                try {
                    init(src, new java.io.FileInputStream((java.io.File) src));
                } catch (Exception eaten) {}
            } else if (src instanceof java.net.URL) {
                this.source = src.toString();
                try {
                    init(src, ((java.net.URL) src).openStream());
                } catch (Exception eaten) {
                    System.err.println(eaten.getMessage());
                }
            } else if (src instanceof byte[]) {
                this.source = null;
                init(src, new java.io.ByteArrayInputStream((byte[]) src));
            } else if (src instanceof Image) {
                this.source = null;
                this.images = new Image[]{(Image) src};
                super.setImage(images[0]);
                initBuffer();
            } else if (src instanceof Image[]) {
                this.source = null;
                this.images = (Image[]) src;
                for(Image i : images) {
                    if(i != null) loadImage(i);
                }
                initBuffer();
            } else {
                throw new IllegalArgumentException("urecognized input type: " +
                        src.getClass());
            }
        }
    }
    /**Sets the src the icon will display.  Specifically, this method just
     * calls <code>setSource(src)</code>.*/
    @Override
    public void setImage(Image image) {
        setSource(image);
    }

    /**Puts the icon in an unitialized state.  The icon is removed from the
     * GIF animator thread if present.  All images are flushed and discarded.*/
    public void dispose() {
        ComponentProber.uninstall(this);
        GIFAnimator.uninstall(this);

        paintingLock.lock();
        try{
            source = null;
            iconWidth = iconHeight = 0;
            if(images != null)
                for(Image i : images)
                    if(i != null) i.flush();
            images = null;
            xOffsets = yOffsets = durations = null;
            disposalMethods = null;
            repainter = null;
            if (buffer != null) {
                bufferGraphics.dispose();
                buffer.flush();
                bufferGraphics = null;
                buffer = null;
            }
            currentIndex = 0;
            setDescription(null);
        }finally{
            paintingLock.unlock();
        }
    }
    /**Returns whether this icon is an animation.  If this icon has more than
     * one src and delay info, then it is an animation.*/
    public boolean isAnimation() {
        return getFrameCount() > 1 && durations != null;
    }
    /**Manually sets animation info.  If a GIF file is used as the source, then
     * this information will be loaded automatically.  For any other format,
     * the information will need to be set manually.  Any of the arrays may
     * be null.  If the delays is null, then this icon will not be considered
     * an animation, but instead a collection of static images.  All shorts are
     * treated as unsigned short values.*/
    public void setAnimation(short[] delays, short[] xOffsets, short[] yOffsets) {
        paintingLock.lock();
        try {
            int frameCount = getFrameCount();
            if((delays != null && delays.length < frameCount) ||
                    (xOffsets != null && xOffsets.length < frameCount) ||
                    (yOffsets != null && yOffsets.length < frameCount)) {
                throw new IllegalArgumentException("length of delays, " +
                        "xOffsets, or yOffsets < frame count!");
            }

            durations = delays;
            this.xOffsets = xOffsets;
            this.yOffsets = yOffsets;
        }finally {
            paintingLock.unlock();
        }
    }
    /**Manually sets the frame to display.  This may be useful when the icon
     * is not an animation, but contains multiple images (example: a .ico file).*/
    public void setFrame(int frameIndex) {
        paintingLock.lock();
        try {
            if(frameIndex >= getFrameCount())
                throw new IllegalArgumentException("frameIndex >= frameCount");

            currentIndex = getFrameCount()-1;
            disposeCurrentFrame();

            for(currentIndex = 0; currentIndex <= frameIndex; currentIndex++) {
                paintCurrentFrame();
                if(currentIndex < frameIndex)
                    disposeCurrentFrame();
            }
            currentIndex--;
        }finally {
            paintingLock.unlock();
        }
    }
    /**Returns whether the animation is currently paused.*/
    public boolean isPaused() {
        return paused;
    }
    /**Makes the animation paused or resumes the painting.*/
    public void setPaused(boolean paused) {
        this.paused = paused;
        if(isAnimation())
            GIFAnimator.notifyPausedStateChanged();
    }
    /**Returns the optional delay set by the user for all frames in
     * centiseconds.  If <code>useSourceDelay</code> is true, this value
     * is not being used.*/
    public int getDelay() {
        return userDelay;
    }
    /**Sets an optional delay between two consecutive frames (for all frames)
     * in centiseconds.  If <code>useSourceDelay</code> is true then this
     * property is ignored.
     * @see #isUseSourceDelay()
     * @see #setUseSourceDelay(boolean)  */
    public void setDelay(int delay) {
        this.userDelay = delay;
    }
    /**Returns whether this icon is using the original delay times in
     * the source file/url to display.*/
    public boolean isUseSourceDelay() {
        if(!isAnimation()) {
            return false;
        }else {
            return useSourceDelay;
        }
    }
    /**Sets whether the delays in the original source file or url should be
     * used instead of the user set delay.  By default, this value is true
     * if there was delay information in the file or url.  Note that if this
     * animation did not come from a source or there was an error in reading
     * the source's delay times, then this property will be ignored.*/
    public void setUseSourceDelay(boolean useSource) {
        useSourceDelay = useSource;
    }
    /**Set's whether an animation should be allowed to move foward only
     * when the the <code>paintIcon</code> method is called.  If set to
     * true and a component is in a state such that it won't call
     * <code>paintIcon</code>, then the animation will enter a supsended
     * state.  An example of this is minimizing the frame.  When
     * the frame is minimized then repaint() calls are ignored.  This means
     * the animation is never painted.  If <code>updateOnlyOnPaint</code> is
     * set to true then the animation will resume only when the frame is
     * restored.  If set to false, then the animamtion will continue in
     * the background. Any reason for why <code>paintIcon</code> would not
     * be called is fair game.  By default the value is false.*/
    public void setUpdateOnlyOnPaint(boolean updateOnlyOnPaint) {
        this.updateOnlyOnPaint = updateOnlyOnPaint;
    }
    /**Returns whether this animation is set to move foward only when the
     * <code>paintIcon</code> method is called.*/
    public boolean isUpdateOnlyOnPaint() {
        return updateOnlyOnPaint;
    }
    /**Returns the number of frames in the animation.  One frame or less means
     * this icon is in fact not an animation.*/
    public int getFrameCount() {
        return images == null?0:images.length;
    }
    /**Returns the src that will be drawn into the animation for the
     * specified frame.*/
    public Image getImage(int frame) {
        return images == null?null:
                images.length == 0?null:images[frame];
    }
    /**Returns the offset in the x direction for which the src in the given
     * frame is painted.  This information was obtained from the original
     * file/url.   Frames are indexed from 0 to <code>getFrameCount()- 1</code>*/
    public int getXOffset(int frame) {
        return xOffsets==null?0:xOffsets[frame] & 0xffff;
    }
    /**Returns the offset in the y direction for which the src in the given
     * frame is painted.  This information was obtained from the original
     * file/url.   Frames are indexed from 0 to <code>getFrameCount()-1</code>*/
    public int getYOffset(int frame) {
        return yOffsets==null?0:yOffsets[frame] & 0xffff;
    }
    /**Returns the disposal method that the specified frame is using.  This
     * information was obtained from the original file/url.  Frames are index
     * from 0 to <code>getFrameCount()-1</code>*/
    public Disposal getDisposalMethod(int frame) {
        return disposalMethods == null?Disposal.RESTORE_TO_BACKGROUND:
                disposalMethods[frame];
    }
    /**Returns the animation duration in centiseconds for the specified frame.
     * The duration is the amount of time that a specific frame is shown
     * before painting the next frame.  The frames are indexed from
     * 0 to <code>getFrameCount()-1</code>*/
    public int getDuration(int frame) {
        if (isUseSourceDelay()) {
            return durations==null?DEFAULT_DURATION:(durations[frame] & 0xffff);
        } else {
            return userDelay;
        }
    }
    /**Returns the width of the icon.  This will either be the width of the
     * animation, if this icon is an animation, or the width of the current
     * src being displayed.*/
    @Override
    public int getIconWidth() {
        if(isAnimation()) {
            return iconWidth;
        }else {
            Image currentImage = getImage(currentIndex);
            return getXOffset(currentIndex) +
                    (currentImage != null? currentImage.getWidth(null):0);
        }
    }
    /**Returns the height of the icon.  This will either be the height of the
     * animation, if this icon is an animation, or the height of the current
     * src being displayed.*/
    @Override
    public int getIconHeight() {
        if(isAnimation()) {
            return iconHeight;
        }else {
            Image currentImage = getImage(currentIndex);
            return getYOffset(currentIndex) +
                    (currentImage != null? currentImage.getHeight(null):0);
        }
    }
    /**Paints the current state of the icon on the given graphics object at
     * the given point.*/
    public void paintIcon(Component c, Graphics g, int x, int y) {
        paintingLock.lock();
        try{
            if (repainter == null && isAnimation() && !isBogusComponent(c,x,y)) {
                repainter = new IconRepaintManager();
                ComponentProber.install(this);
                GIFAnimator.install(this);
            }
            if (repainter != null) {
                if (repainter.probingComp != null && repainter.addRegion(c, x, y)){
                    return;
                } else {
                    repainter.consistencyCheck(c, x, y);
                }
            }

            Image currentFrame = getImage(currentIndex);
            if (currentFrame == null) {
                return;
            }
            if (buffer != null) {
                g.drawImage(buffer, x, y, null);
            } else {
                ImageObserver io = super.getImageObserver();
                if(io == null) io = c;
                g.drawImage(currentFrame,
                        x + getXOffset(currentIndex),
                        y + getYOffset(currentIndex), io);
            }
            canMoveToNextFrame = true;
        }finally{
            paintingLock.unlock();
        }
    }
    private boolean isBogusComponent(Component c, int x, int y) {
        return c == null || x > c.getWidth() || y > c.getHeight() ||
                !c.isDisplayable();
    }


    /**Called by GIFAnimator thread when it's time to move to the next frame.*/
    private void updateFrame() {
        if (paintingLock.tryLock()) {
            try {
                if (repainter == null || !isAnimation()) {
                    GIFAnimator.uninstall(this);
                    return;
                }

                if (!canMoveToNextFrame && updateOnlyOnPaint) {
                    repainter.repaint();
                    return;
                }

                disposeCurrentFrame();
                currentIndex = (currentIndex + 1) % getFrameCount();
                paintCurrentFrame();
                canMoveToNextFrame = false;
                repainter.repaint();
                currentDuration = 0;
            } finally {
                paintingLock.unlock();
            }
        }
    }
    //Convenience method.  Called by updateFrame()
    private void disposeCurrentFrame() {
        if (buffer != null) {
            Disposal disposal = getDisposalMethod(currentIndex);

            boolean lastFrame = currentIndex == getFrameCount()-1;

            if (disposal == Disposal.RESTORE_TO_PREVIOUS   ||
                    disposal == Disposal.RESTORE_TO_BACKGROUND || lastFrame) {

                bufferGraphics.setComposite(
                        java.awt.AlphaComposite.Clear);
                bufferGraphics.fillRect(
                        0, 0, buffer.getWidth(), buffer.getHeight());
                bufferGraphics.setComposite(
                        java.awt.AlphaComposite.SrcOver);
            }
            if (disposal == Disposal.RESTORE_TO_PREVIOUS && !lastFrame) {
                int backtrack = currentIndex - 1;

                while (backtrack > 0) {
                    Disposal backtrackDisposal = getDisposalMethod(backtrack);
                    if (backtrackDisposal == Disposal.UNSPECIFIED ||
                            backtrackDisposal == Disposal.DO_NOT_DISPOSE) {

                        bufferGraphics.drawImage(getImage(backtrack),
                                getXOffset(backtrack),
                                getYOffset(backtrack),
                                null);
                        break;
                    }
                    backtrack--;
                }
            }
        }
    }
    //Convenience method. Called by updateFrame()
    private void paintCurrentFrame() {
        if(buffer != null) {
            Image currentFrame = getImage(currentIndex);
            bufferGraphics.drawImage(
                    currentFrame,
                    getXOffset(currentIndex),
                    getYOffset(currentIndex),
                    null);
        }
    }
    /**Called by the GIFAnimator thread.  Increments the number of nano seconds
     * the current frame has been displayed so far.*/
    private void incrementDuration(long incr) {
        currentDuration += incr;
    }
    /**Called by the GIFAnimator thread.*/
    private long getTimeTillNextFrame() {
        if (paintingLock.tryLock()) {
            try {
                //check for overflow error.
                if (currentDuration < 0) {
                    return -1;
                }else {
                    //nanos
                    return getDuration(currentIndex)*10000000 - currentDuration;
                }
            } finally {
                paintingLock.unlock();
            }
        } else {
            return 1;
        }
    }
    /**Overriden to dispose() of the icon before finalization.*/
    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    /**
     * For animation purposes only.
     * <p>
     * This class stores the components this icon belongs to and the
     * point where the icon is located.  This way, efficient repainting can
     * be done.  Whenever an inconsistency occurs between the (x,y) passed to
     * the paintIcon(...) method and the stored points then it is
     * an indication that the component changed in such a way that we need to
     * reprobe it for all the positions of the icon.
     * <p>
     * To do this, the component is drawn exactly once to a bogus 1x1
     * BufferedImage, and all the positions that the icon is painted to,
     * as a result of this bogus paint, is stored.  The probing only needs
     * to be done for components that can contain the icon in several positions
     * (JTextPane, JEditorPane, JList, JTree, JTable, ect...).  An it only
     * occurs once, one second after an inconsistency occurs (this is to avoid
     * constant probing while drag-resizing a component).
     * <p>
     * Due to the nature of this probing, in the special case of a JTextPane
     * or JEditorPane you can eliminate the probing entirely by wraping the
     * icon in a label and then adding the label to the text component instead
     * of inserting the icon directly.
     * <p>
     * Lastly, the components are held in weak references.  Thus the
     * existence of a GIFIcon won't keep the components it belongs to from
     * being garbage collected.*/
    protected class IconRepaintManager {
        private WeakHashMap<Component,ArrayList<Point>> repaintInfo =
                new WeakHashMap<Component, ArrayList<Point>>(1);

        private java.awt.Point lookupPoint = new java.awt.Point();

        private Component probingComp;

        /**Schedules a repaint of the current frame on the Event Disptach
         * Thread.  The icon is repainted in all the components that it belongs
         * to.*/
        public void repaint() {
            if(repaintInfo.size() == 0) {
                GIFAnimator.uninstall(GIFIcon.this);
                return;
            }

            Iterator<Map.Entry<Component,ArrayList<Point>>> it =
                    repaintInfo.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<Component,ArrayList<Point>> entry = it.next();

                Component c = entry.getKey();
                if(!c.isDisplayable()) {
                    it.remove();
                    continue;
                }

                //the icon is repainted often.  The reason for ArrayList
                //specifically is so we don't always have to create a
                //new Iterator object to traverse the points (especially
                //when most likely there is only one point)
                ArrayList<Point> points = entry.getValue();
                for (int i = 0; i < points.size(); i++) {
                    Point p = points.get(i);
                    c.repaint(p.x, p.y,getIconWidth(),getIconHeight());
                }
            }

        }
        /**Called by the paintIcon() method.  Should only be called on
         * the Event Dispatch Thread.  This method checks whether the
         * given (x,y) and Component is consistent with the information
         * already saved by this icon.  If not, then the component was changed
         * in such a way that we need reprobe for all the icon's positions
         * within the component.  To do this a bogus repaint of the whole
         * component is done to a 1x1 BufferedImage.*/
        private void consistencyCheck(Component c, int x, int y) {
            if(isBogusComponent(c,x,y))
                return;

            ArrayList<Point> points = repaintInfo.get(c);

            //the component might be a rendered component, in which
            //case the cell renderer's parent is saved within the map
            //instead
            if (points == null) {
                lookupPoint.x = x;
                lookupPoint.y = y;
                c = getWhereRendered(c, lookupPoint);
                x = lookupPoint.x;
                y = lookupPoint.y;
                points = repaintInfo.get(c);
            }

            if (points == null) {
                points = new ArrayList<Point>(1);
                repaintInfo.put(c,points);
            }

            for (int i = 0; i < points.size(); i++) {
                Point p = points.get(i);

                if (x == p.x && y == p.y) {
                    return; //consistency check positive
                }
            }

            //minor optimization - some noteworth classes that can only
            //have one icon and hence we need not probe the component
            if ((c instanceof javax.swing.AbstractButton ||
                    c instanceof javax.swing.JLabel) && points.size() == 1) {
                Point p = points.get(0);
                p.x = x;
                p.y = y;
                return;
            }

            //consistency check negative
            points.add(new Point(x, y));
            ComponentProber.scheduleProbe(c, 1000);
        }

        //called by the the component prober.  Should only be called on EDT
        void prepareProbe(Component c) {
            List<Point> points = repaintInfo.get(c);
            if(points != null)
                points.clear();
            probingComp = c;
        }
        //called by the component prober.  Should only be called on the EDT
        void endProbe(Component c) {
            List<Point> foundPoints = repaintInfo.get(c);
            if(foundPoints == null || foundPoints.size() == 0)
                repaintInfo.remove(c);
            probingComp = null;
        }
        //called by paintIcon() when probing.  Should only be called on the EDT
        boolean addRegion(Component c,int x,int y) {
            lookupPoint.x = x; lookupPoint.y = y;
            c = getWhereRendered(c,lookupPoint);

            if (c != probingComp) {
                return false;
            } else {
                ArrayList<Point> points = repaintInfo.get(c);
                if (points == null) {
                    points = new ArrayList<Point>(1);
                    repaintInfo.put(c, points);
                }
                points.add(new Point(lookupPoint));
                return true;
            }
        }
        //the component might be a rendered component, in which
        //case we need the parent of the cell renderer as well as
        //the exact (x,y) within the parent.
        private Component getWhereRendered(Component c,
                                           java.awt.Point p) {

            Component parent = c;

            int tmpX = p.x + c.getX(); int tmpY = p.y + c.getY();

            while ((parent = parent.getParent()) != null) {
                tmpX += parent.getX();
                tmpY += parent.getY();
                if (parent instanceof javax.swing.CellRendererPane) {
                    p.x = tmpX;
                    p.y = tmpY;
                    c = parent.getParent();
                }
            }
            return c;
        }
    }

    static final class ComponentProber
            implements java.awt.event.ActionListener{

        private static final java.util.Set<WeakRefIcon> allIcons;
        private static final CopyOnWriteArraySet<Component> compsToProbe;

        static {
            allIcons = new java.util.HashSet<WeakRefIcon>();
            compsToProbe = new CopyOnWriteArraySet<Component>();
        }

        public static void install(GIFIcon ico) {
            synchronized(allIcons) {
                allIcons.add(new WeakRefIcon(ico));
            }
        }
        public static void uninstall(GIFIcon ico) {
            synchronized(allIcons) {
                allIcons.remove(new WeakRefIcon(ico));
            }
        }
        public static void scheduleProbe(Component c, int delay) {
            if(!compsToProbe.contains(c)) {
                compsToProbe.add(c);
                javax.swing.Timer tmp = new javax.swing.Timer(
                        delay,new ComponentProber());
                tmp.setRepeats(false);
                tmp.start();
            }
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            while(compsToProbe.size() > 0) {
                BufferedImage bogusImg = java.awt.GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration()
                        .createCompatibleImage(1,1,java.awt.Transparency.OPAQUE);
                Graphics bogusGraphics = bogusImg.getGraphics();

                List<Component> toProbe =
                        new java.util.ArrayList<Component>(compsToProbe);

                synchronized (allIcons) {
                    for (Component c : toProbe) {
                        for (WeakRefIcon weakIco : allIcons) {
                            GIFIcon ico = weakIco.get();
                            if (ico != null && ico.repainter != null) {
                                ico.repainter.prepareProbe(c);
                            }
                        }

                        bogusGraphics.setClip(0,0,c.getWidth(),c.getHeight());
                        c.paint(bogusGraphics);

                        for (WeakRefIcon weakIco : allIcons) {
                            GIFIcon ico = weakIco.get();
                            if (ico != null && ico.repainter != null) {
                                ico.repainter.endProbe(c);
                            }
                        }
                    }
                }
                compsToProbe.removeAll(toProbe);

                bogusGraphics.dispose();
                bogusImg.flush();
            }
        }
    }
    static final class GIFAnimator
            implements Runnable {

        //specifically want ArrayList here, so I know traversing via
        //get(int index) will be fast (and avoid creation of many
        //iterator objects).  The catch is we now have to ensure duplicate
        //animations are not added.
        private static final ArrayList<WeakRefIcon> animations;
        private static final CopyOnWriteArraySet<WeakRefIcon> toAdd;
        private static final CopyOnWriteArraySet<WeakRefIcon> toRemove;


        private static final Thread backgroundThread;
        private static int sleeplessLoopCount;

        static {
            animations = new ArrayList<WeakRefIcon>();
            toAdd = new CopyOnWriteArraySet<WeakRefIcon>();
            toRemove = new CopyOnWriteArraySet<WeakRefIcon>();

            backgroundThread = new Thread(new GIFAnimator(),
                    "GIF Animator");
            backgroundThread.setDaemon(true);
            backgroundThread.start();
        }

        //don't allow someone else to create an instance
        private GIFAnimator() {}
        /**Adds the given animation into the the set of animations, if
         * not already present.*/
        public static void install(GIFIcon icon) {
            if (toAdd.add(new WeakRefIcon(icon))) {
                synchronized (animations) {
                    animations.notify();
                }
            }
        }
        /**Removes the given animation from the set of animations, if
         * already present.*/
        public static void uninstall(GIFIcon icon) {
            if (toRemove.add(new WeakRefIcon(icon))) {
                synchronized (animations) {
                    animations.notify();
                }
            }
        }
        public static void notifyPausedStateChanged() {
            synchronized(animations) {
                animations.notify();
            }
        }
        public void run() {
            try{_run();}
            catch(InterruptedException e) {
                System.err.println("GIF Animator thread interrupted. " +
                        "Shutting down thread.");
                e.printStackTrace();
            }finally {
                //no cleanup needed
            }
        }
        private void _run() throws InterruptedException{
            boolean possiblyPaused = false;

            while (true) {
                //first we remove garbage collected animations and add
                //newly queued ones
                while(toRemove.size() > 0) {
                    List<WeakRefIcon> tmp = new ArrayList<WeakRefIcon>(toRemove);
                    animations.removeAll(tmp);
                    toRemove.removeAll(tmp);
                }
                while(toAdd.size() > 0) {
                    List<WeakRefIcon> tmp = new ArrayList<WeakRefIcon>(toAdd);
                    //ensure no duplicates
                    for(WeakRefIcon ico : tmp) {
                        if(!animations.contains(ico))
                            animations.add(ico);
                    }
                    toAdd.removeAll(tmp);
                }

                //Check to see if everyting is paused.  Often times this
                //part will simply be skipped over.
                if(possiblyPaused) {
                    synchronized(animations) {
                        possiblyPaused = false;

                        boolean allPaused = true;
                        for(int i = 0; i < animations.size(); i++) {
                            GIFIcon anim = animations.get(i).get();

                            if(anim != null)
                                allPaused &= anim.isPaused();
                            else //anim = null!
                                animations.remove(i--);
                        }

                        //wait for notification that circumstances have changed
                        //(either a paused state was changed, an animation was
                        //added, or an animation was removed)
                        if(allPaused && animations.size() != 0)
                            waitOnAnimations(0);
                    }
                }

                //If there are no animations to play, then
                //just wait for an animation to be added
                if(animations.size() == 0) {
                    waitOnAnimations(0);
                }

                long sleepTime = Long.MAX_VALUE;
                long timeStamp = System.nanoTime();

                //Now we find the minimum time required to sleep until
                //we need to update one of the animations.
                for (int i = 0; i < animations.size(); i++) {
                    GIFIcon anim = animations.get(i).get();

                    if (anim == null)
                        animations.remove(i--);
                    else if (!anim.isPaused())
                        sleepTime = Math.min(sleepTime,
                                anim.getTimeTillNextFrame());
                }

                if(sleepTime == Long.MAX_VALUE) {
                    possiblyPaused = true;
                    continue;
                }

                //This is a guard against busy waiting.  This may happen if
                //all the animations are currently in components that are
                //not showing on screen, such as when the frame is minimized.
                //If the updateOnlyOnPaint flag is set, then attempts to
                //update to the next frame will fail.  The time to sleep will
                //become 0 and we end up in a spin loop until the frame is
                //restored.  So instead, we wait a little when this occurs.
                if (sleeplessLoopCount > 10) {
                    //sleep 20 milliseconds
                    waitOnAnimations(20 * 1000000);
                    sleeplessLoopCount = 0;
                }

                //Now we sleep the minimum time.  The sleep may be ended
                //prematurely if a new animation is added or removed during
                //the sleep.  It may also be ended prematurely if an GIFIcon's
                //paused state is changed.
                if (sleepTime > 0) {
                    waitOnAnimations(sleepTime);
                } else {
                    sleeplessLoopCount++;
                }

                //actual sleep time nanoseconds
                sleepTime = (System.nanoTime() - timeStamp);

                //update the duration of the GIFIcons
                for(int i = 0; i < animations.size(); i++) {
                    GIFIcon anim = animations.get(i).get();

                    if(anim == null)
                        animations.remove(i--);
                    else if(!anim.isPaused()){
                        anim.incrementDuration(sleepTime);
                        if(anim.getTimeTillNextFrame() <= 0)
                            anim.updateFrame();
                    }
                }
            } //end while loop
        } //end run method
        private void waitOnAnimations(long timeOutNano)
                throws InterruptedException
        {
            synchronized (animations) {
                if (toAdd.size() == 0 && toRemove.size() == 0) {
                    animations.wait(
                            (int) (timeOutNano/1E6),
                            (int) (timeOutNano % 1E6));
                }
            }
        }
    } //end GIFAnimator class

    private static final class WeakRefIcon
            extends WeakReference<GIFIcon>
    {
        private int hash;
        public WeakRefIcon(GIFIcon icon) {
            super(icon);
            hash = icon.hashCode();
        }
        public int hashCode() {
            return hash;
        }
        public boolean equals(Object o) {
            if(this == o)
                return true;

            GIFIcon myIcon = get();
            GIFIcon otherIcon = null;

            if(o instanceof GIFIcon)
                otherIcon = (GIFIcon) o;
            else if(o instanceof WeakRefIcon)
                otherIcon = ((WeakRefIcon) o).get();

            return myIcon == otherIcon;
        }
    }
}