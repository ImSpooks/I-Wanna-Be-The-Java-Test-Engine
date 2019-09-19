package me.ImSpooks.iwbtgengine.collision;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;

import java.awt.*;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Hitbox {
    private List<int[]> pixels;

    @Getter private final GameObject parent;
    @Getter private final HitboxType hitboxType;

    @Getter private final Rectangle size;

    public Hitbox(GameObject parent, Rectangle size) {
        this.parent = parent;
        this.hitboxType = HitboxType.CUSTOM;
        this.pixels = this.getPixels();
        this.size = size;
    }

    public Hitbox(GameObject parent, HitboxType hitboxType, Rectangle size) {
        this.parent = parent;
        this.hitboxType = hitboxType;
        this.pixels = this.getPixels();
        this.size = size;
    }

    public List<int[]> getCachedPixels() {
        return this.pixels;
    }

    public abstract List<int[]> getPixels();

    public boolean intersects(Hitbox hitbox, double x1, double y1, double x2, double y2) {
        Rectangle r1 = this.getRectIfPossible(x1, y1);
        Rectangle r2 = hitbox.getRectIfPossible(x2, y2);

        if (r1.intersects(r2)) {
            if (this.getHitboxType().getDataType() == 1 && hitbox.getHitboxType().getDataType() == 1) {
                return true;
            } else {
                for (int[] pixel : this.pixels) {
                    for (int[] cachedPixel : hitbox.getCachedPixels()) {
                        if (Math.floor(pixel[0] + x1) == Math.floor(cachedPixel[0] + x2) && Math.floor(pixel[1] + y1) == Math.floor(cachedPixel[1] + y2))
                            return true;

                        /*boolean pixelFilled1 = parent.getSprite().getImage().getRGB((int) Math.floor(pixel[0]), (int)Math.floor(pixel[1])) >> 24 != 0x00;
                        boolean pixelFilled2 = hitbox.getParent().getSprite().getImage().getRGB((int) Math.floor(cachedPixel[0]), (int)Math.floor(cachedPixel[1])) >> 24 != 0x00;

                        if (!pixelFilled1 && this.getHitboxType().getDataType() == 1) {
                            pixelFilled1 = true;
                        }

                        if (!pixelFilled2 && hitbox.getHitboxType().getDataType() == 1) {
                            pixelFilled2 = true;
                            System.out.println("kid 2");
                        }

                        if (pixelFilled1 && pixelFilled2)
                            return true;*/
                    }
                }
            }
        }
        return false;
    }

    public void renderHitbox(Camera camera, double x, double y, Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(new Color(200, 0, 220, 128));

        switch (hitboxType.getDataType()) {
            case 1: {
                int px = this.pixels.get(0)[0];
                int py = this.pixels.get(0)[1];
                graphics.fillRect((int) x + px - camera.getCameraX(), (int) y + py - camera.getCameraY(), this.pixels.get(this.pixels.size() - 1)[0] - px, this.pixels.get(this.pixels.size() - 1)[1] - py);
                break;
            }
            default:
            case 2: {
                if (this.pixels != null && !this.pixels.isEmpty()) {
                    for (int[] pixel : this.pixels) {
                        graphics.fillRect((int) x + pixel[0] - camera.getCameraX(), (int) y + pixel[1] - camera.getCameraY(), 1, 1);
                    }
                }
                break;
            }
        }

        graphics.setColor(oldColor);
    }

    private Rectangle cachedRectangle;
    public Rectangle getRectIfPossible() {
        return size;
    }

    public Rectangle getRectIfPossible(double x, double y) {
        Rectangle rectangle = this.getRectIfPossible();
        if (rectangle == null)
            return null;

        Rectangle clone = (Rectangle) rectangle.clone();
        clone.setLocation((int) (rectangle.getX() + x), (int) (rectangle.getY() + y));
        return clone;
    }

    @RequiredArgsConstructor
    public enum HitboxType {
        SQUARE(1),
        RECTANGLE(1),
        KID(1),
        CUSTOM(2),
        ;

        @Getter private final int dataType;
    }
}
