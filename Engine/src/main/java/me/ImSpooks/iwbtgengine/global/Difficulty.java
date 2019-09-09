package me.ImSpooks.iwbtgengine.global;

import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum Difficulty {
    NULL(-1, ""),
    MEDIUM(0, "Medium"),
    HARD(1, "Hard"),
    VERY_HARD(2, "Very Hard"),
    IMPOSSIBLE(3, "Impossible")
    ;

    private final int id;
    private final String name;

    private static final Difficulty[] CACHE = values();
}
