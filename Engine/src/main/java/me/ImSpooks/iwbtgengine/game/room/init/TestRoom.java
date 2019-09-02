package me.ImSpooks.iwbtgengine.game.room.init;

import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.game.room.RoomType;

/**
 * Created by Nick on 02 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class TestRoom extends Room {

    public TestRoom(Object... args) {
        super(RoomType.JTOOL, "/room/level/stage1/room1.jmap", args);
    }

    @Override
    public void update(float delta) {
        //TODO touching specific areas trigger events
    }
}
