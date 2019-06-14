package me.ImSpooks.iwbtgengine.collision;

import lombok.Getter;

import java.awt.*;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Hitbox {
    @Getter private List<int[]> pixels;

    public Hitbox() {
        this.pixels = this.getPixels();
    }

    public abstract List<int[]> getPixels();

    public boolean intersects(Hitbox hitbox, int x1, int y1, int x2, int y2) {
        for (int[] integers : hitbox.getPixels()) {
            for (int[] pixel : this.pixels) {
                if (integers[0] + x1 == pixel[0] + x2 && integers[1] + y1 == pixel[1] + y2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void renderHitbox(int x, int y, Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(new Color(200, 0, 220, 128));
        if (this.pixels != null && !this.pixels.isEmpty()) {
            for (int[] pixel : this.pixels) {
                graphics.fillRect(x + pixel[0], y + pixel[1], 1, 1);
            }
        }

        graphics.setColor(oldColor);
    }
}
