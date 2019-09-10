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

    public boolean intersects(Hitbox hitbox, int x1, int y1, int x2, int y2) {
        if (this.getHitboxType().getDataType() == 1 && hitbox.getHitboxType().getDataType() == 1) {
            Rectangle r1 = this.getRectIfPossible();
            Rectangle r2 = hitbox.getRectIfPossible();

            return r1.intersects(r2);


            /*int tw = this.pixels.get(this.pixels.size() - 1)[0] - this.pixels.get(0)[0];
            int th = this.pixels.get(this.pixels.size() - 1)[1] - this.pixels.get(0)[1];

            List<int[]> other = hitbox.getCachedPixels();
            int rw = other.get(other.size() - 1)[0] - other.get(0)[0];
            int rh = other.get(other.size() - 1)[1] - other.get(0)[1];


            if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
                return false;
            }

            int tx = this.pixels.get(0)[0] + x1;
            int ty = this.pixels.get(0)[1] + y1;
            int rx = other.get(0)[0] + x2;
            int ry = other.get(0)[1] + y2;

            rw += rx;
            rh += ry;
            tw += tx;
            th += ty;

            System.out.println();
            System.out.println("(" + rw + " < " + rx + " || " + rw + " > " + tx + ") = " + (rw < rx || rw > tx));
            System.out.println("(" + rh + " < " + ry + " || " + rh + " > " + ty + ") = " + (rh < ry || rh > ty));
            System.out.println("(" + tw + " < " + tx + " || " + tw + " > " + rx + ") = " + (tw < tx || tw > rx));
            System.out.println("(" + th + " < " + ty + " || " + th + " > " + ry + ") = " + (th < ty || th > ry));

            //      overflow || intersect
            return (rw < rx || rw > tx) &&
                    (rh < ry || rh > ty) &&
                    (tw < tx || tw > rx) &&
                    (th < ty || th > ry);*/

        }
        else {
            for (int[] integers : hitbox.getCachedPixels()) {
                for (int[] pixel : this.pixels) {
                    if (integers[0] + x1 == pixel[0] + x2 && integers[1] + y1 == pixel[1] + y2) {
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

    @RequiredArgsConstructor
    public enum HitboxType {
        SQUARE(1),
        RECTANGLE(1),
        CUSTOM(2),
        ;

        @Getter private final int dataType;
    }
}
