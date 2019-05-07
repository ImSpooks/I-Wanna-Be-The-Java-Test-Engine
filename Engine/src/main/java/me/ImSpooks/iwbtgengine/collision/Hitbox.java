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

    public boolean intersects(Hitbox hitbox, int x, int y) {
        for (int[] integers : hitbox.getPixels()) {
            if (this.pixels.contains(new int[] {
                    x + integers[0], y + integers[1]
            })) {
                return true;
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
