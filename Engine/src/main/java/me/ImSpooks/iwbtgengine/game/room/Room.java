package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.KillerObject;
import me.ImSpooks.iwbtgengine.game.room.readers.JToolReader;
import me.ImSpooks.iwbtgengine.game.room.readers.MapReader;
import me.ImSpooks.iwbtgengine.global.ErrorCodes;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Room {

    @Getter private MapReader map;

    private String path;

    @Getter private Object[] args;

    public Room(RoomType type, String path, Object... args) {
        this.path = path;
        this.args = args;

        this.readMap(type);
    }

    public Room(Room room) {
        this.path = room.path;
        this.map = room.map;
    }

    public void render(Graphics graphics) {
        this.getObjects().forEach(object -> object.render(graphics));
    }

    public void update(float delta) {
        this.getObjects().forEach(object -> object.update(delta));
    }

    public void readMap(RoomType type) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(this.path)));

        switch (type) {
            default:
            case ENGINE: {
                map = null;
                break;
            }

            case JTOOL: {
                map = new JToolReader(bufferedReader, this, args);
                break;
            }

            case CUSTOM: {
                map = null;
                break;
            }
        }

        if (map != null) {
            map.readMap();
        }
        else {
            System.exit(ErrorCodes.ROOM_ERROR);
            throw new NullPointerException(String.format("An error occured while loading map \"%s\"", this.path));
        }
    }

    //TODO cache positions question mark?

    public int[] getStartingPositions() {
        return new int[] {this.map.getStartX(), this.getMap().getStartY()};
    }

    public List<GameObject> getObjects() {
        return this.map.getObjects();
    }

    public List<GameObject> getObjectsAt(int x, int y) {
        List<GameObject> list = new ArrayList<>();

        for (GameObject gameObject : this.getObjects()) {
            if (gameObject instanceof KillerObject) {
            }
            if (x >= gameObject.getX() && x < gameObject.getX() + gameObject.getWidth()) {
                if (y >= gameObject.getY() && y < gameObject.getY() + gameObject.getHeight()) {
                    list.add(gameObject);
                }
            }
        }

        return list;
    }

    @Override
    public Room clone() {
        return new Room(this);
    }
}
