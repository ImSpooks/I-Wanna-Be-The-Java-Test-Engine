package me.ImSpooks.iwbtgengine.game.object.objects.killer;

import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.RenderPriority;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 10 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = RenderPriority.LOWEST)
public class Spike extends KillerObject {

    public Spike(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

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