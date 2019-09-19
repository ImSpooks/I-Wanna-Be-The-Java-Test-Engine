package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 13 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Gravity extends Interactable {

    @Getter private boolean up;

    public Gravity(Room parent, double x, double y, Sprite sprite, boolean up) {
        super(parent, x, y, sprite);
        this.up = up;

        this.setHitbox(new Hitbox(this, new Rectangle(0, 0, sprite.getImage().getWidth(), sprite.getImage().getHeight())) {
            @Override
            public java.util.List<int[]> getPixels() {
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

    @Override
    public TouchAction onTouch() {
        return (kid) -> {
            if (this.up) {
                Global.GRAVITY = -1.0;

                if (kid.getHitbox() != kid.getFlippedHitbox())
                    kid.setHitbox(kid.getFlippedHitbox());
            }
            else {
                Global.GRAVITY = 1.0;

                if (kid.getHitbox() != kid.getNormalHitbox())
                    kid.setHitbox(kid.getNormalHitbox());
            }
        };
    }
}
