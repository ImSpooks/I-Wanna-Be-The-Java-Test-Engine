package me.ImSpooks.iwbtgengine.util;

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
        BufferedImage before = image;
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return scaleOp.filter(before, after);
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

    public void drawStringShadow(Graphics g, String string, int x, int y) {
        Color prevColor = g.getColor();
        g.drawString(string, x, y);
        g.setColor(new Color(prevColor.getRed(), prevColor.getGreen(), prevColor.getBlue(), prevColor.getAlpha() / 3));
        g.drawString(string, x + 2, y + 1);
        g.setColor(prevColor);
    }

    public BufferedImage addColor(BufferedImage inImage, int red, int green, int blue, int alpha, float ratio) {
        int w = inImage.getWidth();
        int h = inImage.getHeight();

        BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color color = new Color(inImage.getRGB(i, j), true);
                if (color.getAlpha() <= 0)
                    continue;

                Color blendedColor = this.blendColor(color, new Color(red, green, blue, alpha), ratio);

                outImage.setRGB(i, j, blendedColor.getRGB());
            }
        }
        return outImage;
    }

    public BufferedImage addColor(BufferedImage inImage, Color color, float ratio) {
        return addColor(inImage, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), ratio);
    }

    public BufferedImage addColor(BufferedImage inImage, Color color) {
        return addColor(inImage, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 1f);
    }

    public BufferedImage addColor(BufferedImage inImage, int red, int green, int blue, float ratio) {
        return addColor(inImage, red, green, blue, 255, ratio);
    }

    public BufferedImage addColor(BufferedImage inImage, int red, int green, int blue) {
        return addColor(inImage, red, green, blue, 255, 1f);
    }

    public Color blendColor(Color c1, Color c2, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));

        return new Color(a << 24 | r << 16 | g << 8 | b);
    }

    private int keep256(int i) {
        if (i <= 255 && i >= 0)
            return i;
        if (i > 255)
            return 255;
        return 0;
    }
}
