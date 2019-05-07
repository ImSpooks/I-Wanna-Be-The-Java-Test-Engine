package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum RoomType {
    ENGINE(1, "Engine"),
    JTOOL(2, "JTool"),
    CUSTOM(3, "Custom")
    ;

    @Getter private final int id;
    @Getter private final String name;
}
