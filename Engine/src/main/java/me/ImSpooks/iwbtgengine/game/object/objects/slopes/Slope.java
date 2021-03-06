package me.ImSpooks.iwbtgengine.game.object.objects.slopes;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 20 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Slope extends Block {

    @Getter private final SlopeDirection direction;

    public Slope(Room parent, double x, double y, Sprite sprite, SlopeDirection direction) {
        super(parent, x, y, sprite);
        this.direction = direction;
    }

    public enum SlopeDirection {
        DOWN_LEFT,
        DOWN_RIGHT,
        UP_LEFT,
        UP_RIGHT,
        ;
    }


    @Override
    public Hitbox getUpdatedHitbox() {
        return new Hitbox(this, new Rectangle(0, 0, sprite.getImage().getWidth(), sprite.getImage().getHeight())) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x < sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y < sprite.getImage().getHeight(); y++) {

                        if ((sprite.getImage().getRGB(x,y) >> 24) == 0x00)
                            continue;

                        pixels.add(new int[] {x, y});
                    }
                }

                return pixels;
            }
        };
    }
}
