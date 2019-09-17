package me.ImSpooks.iwbtgengine.collision;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.camera.Camera;

import java.awt.*;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Hitbox {
    private List<int[]> pixels;

    @Getter private HitboxType hitboxType;

    public Hitbox() {
        this.hitboxType = HitboxType.CUSTOM;
        this.pixels = this.getPixels();
    }

    public Hitbox(HitboxType hitboxType) {
        this.hitboxType = hitboxType;
        this.pixels = this.getPixels();
    }

    public List<int[]> getCachedPixels() {
        return this.pixels;
    }

    public abstract List<int[]> getPixels();

    public boolean intersects(Hitbox hitbox, double x1, double y1, double x2, double y2) {
        if (this.getHitboxType().getDataType() == 1 && hitbox.getHitboxType().getDataType() == 1) {
            Rectangle r1 = this.getRectIfPossible(x1, y1);
            Rectangle r2 = hitbox.getRectIfPossible(x2, y2);

            int tw = r1.width;
            int th = r1.height;
            int rw = r2.width;
            int rh = r2.height;
            if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
                return false;
            }
            int tx = r1.x;
            int ty = r1.y;
            int rx = r2.x;
            int ry = r2.y;
            rw += rx;
            rh += ry;
            tw += tx;
            th += ty;
            //      overflow || intersect
            return ((rw <= rx || rw >= tx) &&
                    (rh <= ry || rh >= ty) &&
                    (tw <= tx || tw >= rx) &&
                    (th <= ty || th >= ry));
        }
        else {
            for (int[] pixel : this.pixels) {
                for (int[] cachedPixel : hitbox.getCachedPixels()) {
                    if ((int)Math.floor(pixel[0] + x1) == (int)Math.floor(cachedPixel[0] + x2)
                            && (int)Math.floor(pixel[1] + y1) == (int)Math.floor(cachedPixel[1] + y2)) {
                        return true;
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
        if (this.getHitboxType().getDataType() != 1)
            return null;

        if (cachedRectangle == null) {
            int px = this.pixels.get(0)[0];
            int py = this.pixels.get(0)[1];

            return cachedRectangle = new Rectangle(px, py, this.pixels.get(this.pixels.size() - 1)[0] - px, this.pixels.get(this.pixels.size() - 1)[1] - py);
        }
        return cachedRectangle;
    }

    public Rectangle getRectIfPossible(double x, double y) {
        Rectangle rectangle = this.getRectIfPossible();
        if (rectangle == null)
            return null;

        Rectangle clone = (Rectangle) rectangle.clone();
        clone.setLocation((int) (rectangle.getX() + x), (int) (rectangle.getY() + y));
        //System.out.println("rectangle 1 = " + rectangle.toString());
        //System.out.println("rectangle 2 = " + clone.toString() + String.format(" {%s + %s = %s}", x, rectangle.getX(), rectangle.getX() + x));
        return clone;
    }

    @RequiredArgsConstructor
    public enum HitboxType {
        SQUARE(1),
        RECTANGLE(1),
        CUSTOM(2),
        ;

        @Getter private final int dataType;
    }
}
