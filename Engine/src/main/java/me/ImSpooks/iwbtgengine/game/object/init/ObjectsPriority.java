package me.ImSpooks.iwbtgengine.game.object.init;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 11 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum ObjectsPriority {
    LOWEST(1, "Lowest"),
    LOW(2, "Low"),
    NORMAL(3, "Normal"),
    HIGH(4, "Highest"),
    HIGHEST(5, "Highest"),
    ;

    @Getter private final int priority;
    @Getter private final String name;
}
