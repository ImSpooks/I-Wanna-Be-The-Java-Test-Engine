package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

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
