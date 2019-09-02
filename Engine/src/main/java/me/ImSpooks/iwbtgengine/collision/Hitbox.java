package me.ImSpooks.iwbtgengine.collision;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Hitbox {
    @Getter private List<int[]> pixels;

    @Getter private HitboxType hitboxType;

    public Hitbox() {
        this.hitboxType = HitboxType.CUSTOM;
        this.pixels = this.getPixels();
    }

    public Hitbox(HitboxType hitboxType) {
        this.hitboxType = hitboxType;
        this.pixels = this.getPixels();
    }

    public abstract List<int[]> getPixels();

    public boolean intersects(Hitbox hitbox, int x1, int y1, int x2, int y2) {
        if (this.getHitboxType().getDataType() == hitbox.getHitboxType().getDataType()) {
            int tw = this.pixels.get(this.pixels.size() - 1)[0];
            int th = this.pixels.get(this.pixels.size() - 1)[1];
            int rw = hitbox.getPixels().get(hitbox.getPixels().size() - 1)[0];
            int rh = hitbox.getPixels().get(hitbox.getPixels().size() - 1)[1];
            if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
                return false;
            }


            int tx = x1;
            int ty = y1;
            int rx = x2;
            int ry = y2;
            rw += rx;
            rh += ry;
            tw += tx;
            th += ty;
            //      overflow || intersect
            return ((rw < rx || rw > tx) &&
                    (rh < ry || rh > ty) &&
                    (tw < tx || tw > rx) &&
                    (th < ty || th > ry));
        }
        else {
            for (int[] integers : hitbox.getPixels()) {
                for (int[] pixel : this.pixels) {
                    if (integers[0] + x1 == pixel[0] + x2 && integers[1] + y1 == pixel[1] + y2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void renderHitbox(int x, int y, Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(new Color(200, 0, 220, 128));

        switch (hitboxType.getDataType()) {
            case 1: {
                graphics.fillRect(x + this.pixels.get(0)[0], y + this.pixels.get(0)[1], this.pixels.get(this.pixels.size() - 1)[0], this.pixels.get(this.pixels.size() - 1)[1]);
                break;
            }
            default: {
                if (this.pixels != null && !this.pixels.isEmpty()) {
                    for (int[] pixel : this.pixels) {
                        graphics.fillRect(x + pixel[0], y + pixel[1], 1, 1);
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
