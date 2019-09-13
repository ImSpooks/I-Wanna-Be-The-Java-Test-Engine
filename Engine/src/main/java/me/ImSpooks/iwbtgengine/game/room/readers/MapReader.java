package me.ImSpooks.iwbtgengine.game.room.readers;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.game.room.RoomType;
import me.ImSpooks.iwbtgengine.handler.ResourceHandler;

import java.awt.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class MapReader {
    @Getter @Setter private List<GameObject> objects;

    @Getter @Setter private int startX = 32, startY = 32;
    @Getter @Setter private RoomType roomType = RoomType.NORMAL;

    @Getter @Setter private int roomWidth, roomHeight;

    @Getter @Setter private Dimension roomSize = new Dimension(25, 19);
    @Getter @Setter private boolean deathBorder = false;

    public abstract void readMap();

    protected final BufferedReader reader;
    @Getter private final Room room;

    @Getter private Object[] args;

    public MapReader(BufferedReader reader, Room room, Object... args) {
        this.reader = reader;
        this.room = room;
        this.objects = new ArrayList<>();
        this.args = args;
    }

    public ResourceHandler getResourceHandler() {
        return Main.getInstance().getResourceHandler();
    }

    protected void addObject(GameObject object) {
        this.objects.add(object);
    }
}