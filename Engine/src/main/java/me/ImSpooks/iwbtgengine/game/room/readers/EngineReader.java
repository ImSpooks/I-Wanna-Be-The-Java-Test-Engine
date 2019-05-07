package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.room.Room;

import java.io.BufferedReader;

/**
 * Created by Nick on 03 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class EngineReader extends MapReader {

    public EngineReader(BufferedReader reader, Room room) {
        super(reader, room);
    }

    @Override
    public void readMap() {

    }
}
