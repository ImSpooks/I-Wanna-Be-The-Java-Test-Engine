package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.global.ErrorCodes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class RoomManager {

    @Getter private Map<String, Room> rooms;
    @Getter private Map<Integer, Room> roomsById;

    public RoomManager() {
        this.rooms = new LinkedHashMap<>();
        this.roomsById = new LinkedHashMap<>();

        this.rooms.put("stage1_room1", new Room(RoomType.JTOOL, "/room/level/stage1/room1.jmap"));

        for (Room room : this.rooms.values()) {
            roomsById.put(roomsById.size(), room);
        }
    }

    public Room getRoom(String name) {
        if (!this.rooms.containsKey(name)) {
            System.exit(ErrorCodes.ROOM_NOT_FOUND);
            throw new NullPointerException(String.format("Room with name \"%s\" not found.", name));
        }
        return this.rooms.get(name).clone();
    }

    public Room getRoom(int id) {
        if (!this.roomsById.containsKey(id)) {
            System.exit(ErrorCodes.ROOM_NOT_FOUND);
            throw new NullPointerException(String.format("Room with id \"%s\" not found.", id));
        }
        return this.roomsById.get(id).clone();
    }
}
