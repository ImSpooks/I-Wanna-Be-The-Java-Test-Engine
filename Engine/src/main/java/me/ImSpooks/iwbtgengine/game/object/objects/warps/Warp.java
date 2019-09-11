package me.ImSpooks.iwbtgengine.game.object.objects.warps;

import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 06 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Warp extends Trigger {

    public Warp(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        this.setVisible(true);

        this.setHitbox(new Hitbox() {
            @Override
            public List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x < sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y < sprite.getImage().getHeight(); y++) {

                        if ((sprite.getImage().getRGB(x,y) >>24) == 0x00)
                            continue;

                        pixels.add(new int[] {x, y});
                    }
                }

                return pixels;
            }
        });
    }

}
