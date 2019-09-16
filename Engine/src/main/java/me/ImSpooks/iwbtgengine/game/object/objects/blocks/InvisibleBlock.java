package me.ImSpooks.iwbtgengine.game.object.objects.blocks;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.LOW, colisionPriority = ObjectsPriority.HIGHEST)
public class InvisibleBlock extends Block {

    @Getter @Setter private TouchAction onTouch = (kid) -> {};

    public InvisibleBlock(Room parent, double x, double y, int width, int height) {
        super(parent, x, y, null);

        this.setWidth(width);
        this.setHeight(height);

        this.setHitbox(new Hitbox(Hitbox.HitboxType.SQUARE) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x <= width; x++) {
                    for (int y = 0; y <= height; y++) {

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
