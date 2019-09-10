package me.ImSpooks.iwbtgengine.game.object.objects.blocks;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.event.events.init.PerformAction;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Block extends GameObject {

    @Getter @Setter private PerformAction onTouch = () -> {};

    public Block(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        this.setHitbox(new Hitbox(Hitbox.HitboxType.SQUARE) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x <= sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y <= sprite.getImage().getHeight(); y++) {

                        // only adding outline to reduce lag

                        pixels.add(new int[] {x, y});
                    }
                }

                return pixels;
            }
        });
    }

    @Override
    public boolean update(float delta) {
        if (super.update(delta)) {
            this.x += this.velX;
            this.y += this.velY;
            return true;
        }
        return false;
    }
}
