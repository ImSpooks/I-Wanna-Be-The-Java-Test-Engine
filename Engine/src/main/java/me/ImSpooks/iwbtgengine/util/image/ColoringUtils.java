package me.ImSpooks.iwbtgengine.util.image;

import me.ImSpooks.iwbtgengine.helpers.NumberConversions;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ColoringUtils {

    /**
     * Adds color to given image {@see ColoringUtils#blendColor(Color, Color, float)}
     *
     * @param image Input image
     * @param red Red value from 0-255
     * @param green Green value from 0-255
     * @param blue Blue value from 0-255
     * @param alpha Alpha value from 0-255
     * @param ratio Ratio value from 0-1 as a {@link float}
     * @return Image with added color layer
     */
    public BufferedImage addColor(BufferedImage image, int red, int green, int blue, int alpha, float ratio) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color color = new Color(image.getRGB(i, j), true);
                if (color.getAlpha() <= 0)
                    continue;

                Color blendedColor = this.blendColor(color, new Color(red, green, blue, alpha), ratio);

                outImage.setRGB(i, j, blendedColor.getRGB());
            }
        }
        return outImage;
    }
    /**
     * @see ColoringUtils#addColor(BufferedImage, int, int, int, float)
     */
    public BufferedImage addColor(BufferedImage image, int red, int green, int blue, float ratio) {
        return addColor(image, red, green, blue, 255, ratio);
    }
    /**
     * @see ColoringUtils#addColor(BufferedImage, int, int, int, float)
     */
    public BufferedImage addColor(BufferedImage image, int red, int green, int blue) {
        return addColor(image, red, green, blue, 255, 1f);
    }

    /**
     * Adds color to given image
     *
     * @param image Input image
     * @param color Color to blend with
     * @param ratio Ratio value from 0-1 as a {@link float}
     * @return Image with added color layer
     */
    public BufferedImage addColor(BufferedImage image, Color color, float ratio) {
        return addColor(image, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), ratio);
    }
    /**
     * @see ColoringUtils#addColor(BufferedImage, Color, float)
     */
    public BufferedImage addColor(BufferedImage image, Color color) {
        return addColor(image, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 1f);
    }

    /**
     * Blends 2 colors together
     *
     * @param c1 First color
     * @param c2 Second color
     * @param ratio Ratio for the second color to blend in the first color
     */
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

    /**
     * Add an alpha layer to the image
     *
     * @param image Input image
     * @param alpha Alpha value from 0-255
     * @return Image with added alpha layer
     */
    public BufferedImage setAlpha(BufferedImage image, int alpha) {
        for (int cx = 0; cx < image.getWidth(); cx++) {
            for (int cy = 0; cy < image.getHeight(); cy++) {
                int color = image.getRGB(cx, cy);

                int mc = (alpha << 24) | 0x00ffffff;
                int newcolor = color & mc;
                image.setRGB(cx, cy, newcolor);
            }
        }
        return image;
    }

    /**
     * @param value Color value
     * @return Clamped value between 0 and 255
     */
    public int keep256(int value) {
        return NumberConversions.clamp(value, 0, 255);
    }
}
