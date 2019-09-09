package me.ImSpooks.iwbtgengine.game.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.game.item.items.TestItem;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum GameItems {
    TEST_ITEM(1, "Test Item", new TestItem())
    ;

    @Getter private final int id;
    @Getter private final String name;
    @Getter private final GameItem gameItem;

    public static final GameItems[] CACHE = values();

    public static GameItems getFromId(int id) {
        for (GameItems gameItems : CACHE) {
            if (gameItems.getId() == id) {
                return gameItems;
            }
        }
        return null;
    }

    public static GameItems getFromName(String name) {
        for (GameItems gameItems : CACHE) {
            if (gameItems.getName().equalsIgnoreCase(name)) {
                return gameItems;
            }
        }
        return null;
    }

    public static GameItems getFromClass(Class clazz) {
        for (GameItems gameItems : CACHE) {
            if (gameItems.getGameItem().getClass() == clazz) {
                return gameItems;
            }
        }
        return null;
    }
}
