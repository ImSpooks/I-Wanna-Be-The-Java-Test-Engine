package me.ImSpooks.iwbtgengine.game.object.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 10 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum  KidState {
    DEAD(0, false),
    IDLE(1, true),
    MOVING(2, true),
    JUMPING(3, true),
    FALLING(4, true),

    IN_WATER(6, false),
    SLIDING_LEFT(7, false),
    SLIDING_RIGHT(8, false),
    ;

    @Getter private final int id;
    private final boolean canFall;

    public boolean canFall() {
        return canFall;
    }
}
