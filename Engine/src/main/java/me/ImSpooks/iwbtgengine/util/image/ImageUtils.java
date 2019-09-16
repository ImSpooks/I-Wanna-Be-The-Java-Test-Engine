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

    public static ImageUtils getInstance() {
        if (instance == null)
            instance = new ImageUtils();
        return instance;
    }

    @Getter private ColoringUtils coloringUtils = new ColoringUtils();

    public void drawImage(Graphics g, BufferedImage sourceImage, double x, double y, int imageNumber, int tileWidth, int tileHeight) {
        int px = (int)((x));
        int py = (int)((Math.floor(y)));
        int sx = (imageNumber%20) * tileWidth;
        int sy = tileHeight * (imageNumber/20);
        g.drawImage(sourceImage, px, py, px + tileWidth,
                py + tileHeight, sx, sy, sx + tileWidth, sy + tileHeight,
                null);
    }

    public BufferedImage getSubImage(BufferedImage sourceImage, int column, int row, int tileWidth, int tileHeight) {
        return sourceImage.getSubimage(column * tileWidth, row * tileHeight, tileWidth, tileHeight);
    }

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

    public BufferedImage flipImage(BufferedImage image, boolean horizontally) {
        AffineTransform tx;
        AffineTransformOp op;

        if (horizontally) { // flip horizontally
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
        }
        else { // flip vertically
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
        }
        return image;
    }

    public BufferedImage cloneimage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void drawStringShadow(Graphics g, String string, int x, int y, Color color) {
        Color prevColor = g.getColor();
        g.setColor(color);
        g.drawString(string, x, y);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 6));
        g.drawString(string, x + 2, y + 1);
        g.setColor(prevColor);
    }

    public void drawStringShadow(Graphics g, String string, int x, int y) {
        this.drawStringShadow(g, string, x, y, g.getColor());
    }
}
