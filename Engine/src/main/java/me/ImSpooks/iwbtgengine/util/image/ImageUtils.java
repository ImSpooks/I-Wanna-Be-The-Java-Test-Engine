package me.ImSpooks.iwbtgengine.util.image;

import lombok.Getter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created by Nick on 03 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ImageUtils {

    private static ImageUtils instance;

    /**
     * @return Static image util instance
     */
    public static ImageUtils getInstance() {
        if (instance == null)
            instance = new ImageUtils();
        return instance;
    }

    @Getter private ColoringUtils coloringUtils = new ColoringUtils();

    /**
     * @deprecated
     */
    @Deprecated
    public void drawImage(Graphics graphics, BufferedImage sourceImage, double x, double y, int imageNumber, int tileWidth, int tileHeight) {
        int px = (int)((x));
        int py = (int)((Math.floor(y)));
        int sx = (imageNumber%20) * tileWidth;
        int sy = tileHeight * (imageNumber/20);
        graphics.drawImage(sourceImage, px, py, px + tileWidth,
                py + tileHeight, sx, sy, sx + tileWidth, sy + tileHeight,
                null);
    }

    /**
     * Get a subimage in an image
     *
     * @param sourceImage Source image
     * @param column Column
     * @param row Row
     * @param tileWidth Sub image width
     * @param tileHeight Sub image height
     * @return Sub image
     */
    public BufferedImage getSubImage(BufferedImage sourceImage, int column, int row, int tileWidth, int tileHeight) {
        return sourceImage.getSubimage(column * tileWidth, row * tileHeight, tileWidth, tileHeight);
    }

    /**
     * Scales an image
     *
     * @param image Source image
     * @param scale Image scale
     * @return Scaled image
     */
    public BufferedImage scaleImage(BufferedImage image, double scale) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return scaleOp.filter(image, after);
    }

    /**
     * Flips an image horizontally
     *
     * @param image Source image
     * @return Horizontally flipped image
     */
    public BufferedImage flipImageHorizontally(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        return image;
    }

    /**
     * Flips an image vertically
     *
     * @param image Source image
     * @return Horizontally flipped image
     */
    public BufferedImage flipImageVertically(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        return image;
    }

    /**
     * Clones an image
     *
     * @param image Image to clone
     * @return Cloned image
     */
    public BufferedImage cloneImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Draws a string with an shadow underneath
     *
     * @param graphics Game graphics
     * @param string String to render
     * @param x X position
     * @param y Y position
     * @param color Text color
     */
    public void drawStringShadow(Graphics graphics, String string, int x, int y, Color color) {
        Color prevColor = graphics.getColor();
        graphics.setColor(color);
        graphics.drawString(string, x, y);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 6));
        graphics.drawString(string, x + 2, y + 1);
        graphics.setColor(prevColor);
    }
    /**
     * Draws string with current color on the game's graphics
     * @see ImageUtils#drawStringShadow(Graphics, String, int, int, Color)
     * @deprecated
     */
    @Deprecated
    public void drawStringShadow(Graphics g, String string, int x, int y) {
        this.drawStringShadow(g, string, x, y, g.getColor());
    }
}
