package me.ImSpooks.iwbtgengine.util.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ColoringUtils {

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
