package me.ImSpooks.iwbtgengine.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum Difficulty {
    NULL(-1, "", "NULL"),
    MEDIUM(0, "Medium", "Medium"),
    HARD(1, "Hard", "Hard"),
    VERY_HARD(2, "Very Hard", "VHard"),
    IMPOSSIBLE(3, "Impossible", "Impossible")
    ;

    @Getter private final int id;
    @Getter private final String name;
    @Getter private final String internalName;

    public static final Difficulty[] CACHE = values();

    public static Difficulty getFromId(int id) {
        for (Difficulty difficulty : CACHE) {
            if (difficulty.getId() == id)
                return difficulty;
        }
        return null;
    }

    public static Difficulty getFromName(String name) {
        for (Difficulty difficulty : CACHE) {
            if (difficulty.getName().equalsIgnoreCase(name))
                return difficulty;
        }
        return null;
    }

    public static Difficulty getFromInternalName(String internalName) {
        for (Difficulty difficulty : CACHE) {
            if (difficulty.getInternalName().equalsIgnoreCase(internalName))
                return difficulty;
        }
        return null;
    }
}
