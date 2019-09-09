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
        if (this.getHitboxType().getDataType() == hitbox.getHitboxType().getDataType()) {
            int tw = this.pixels.get(this.pixels.size() - 1)[0];
            int th = this.pixels.get(this.pixels.size() - 1)[1];
            int rw = hitbox.getCachedPixels().get(hitbox.getCachedPixels().size() - 1)[0];
            int rh = hitbox.getCachedPixels().get(hitbox.getCachedPixels().size() - 1)[1];
            if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
                return false;
            }

            rw += x2;
            rh += y2;
            tw += x1;
            th += y1;
            //      overflow || intersect
            return ((rw < x2 || rw > x1) &&
                    (rh < y2 || rh > y1) &&
                    (tw < x1 || tw > x2) &&
                    (th < y1 || th > y2));
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
                graphics.fillRect((int) x + this.pixels.get(0)[0] - camera.getCameraX(), (int) y + this.pixels.get(0)[1] - camera.getCameraY(), this.pixels.get(this.pixels.size() - 1)[0], this.pixels.get(this.pixels.size() - 1)[1]);
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

    @RequiredArgsConstructor
    public enum HitboxType {
        SQUARE(1),
        RECTANGLE(1),
        CUSTOM(2),
        ;

        @Getter private final int dataType;
    }
}
