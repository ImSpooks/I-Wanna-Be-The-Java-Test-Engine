package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 11 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.HIGHEST)
public class Water extends Interactable {
    
    @Getter @Setter private WaterType waterType;

    public Water(Room parent, double x, double y, Sprite sprite, WaterType waterType) {
        super(parent, x, y, sprite);
        this.waterType = waterType;

        this.setHitbox(new Hitbox(this, Hitbox.HitboxType.SQUARE, new Rectangle(0, 0, sprite.getImage().getWidth(), sprite.getImage().getHeight())) {
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
    public TouchAction onTouch() {
        return kid -> {
            if (kid.getVelY() > 2)
                kid.setVelY(2);

            if (waterType == WaterType.FULL_JUMP) {
                kid.setCanJump(kid.getMaxJumps());
            }
            else if (waterType == WaterType.DOUBLE_JUMP) {
                kid.setCanJump(kid.getMaxJumps() - 1);
            }
            else if (waterType == WaterType.SINGLE_JUMP && kid.getCanJump() == 0 && kid.getVelY() > 0) {
                kid.setCanJump(1);
            }
        };
    }

    @RequiredArgsConstructor
    public enum WaterType {
        FULL_JUMP(1),
        DOUBLE_JUMP(2),
        SINGLE_JUMP(3)
        ;

        @Getter private final int id;
    }
}
