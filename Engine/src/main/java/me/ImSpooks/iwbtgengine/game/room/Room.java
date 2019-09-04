package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.KillerObject;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.room.readers.EngineReader;
import me.ImSpooks.iwbtgengine.game.room.readers.JToolReader;
import me.ImSpooks.iwbtgengine.game.room.readers.MapReader;
import me.ImSpooks.iwbtgengine.global.ErrorCodes;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Room {

    @Getter private MapReader map;

    private String path;

    @Getter @Setter private String internalId = "";

    @Getter protected BufferedImage background = null;

    public Room(ReaderType type, String path) {
        this.path = path;

        this.readMap(type);

        this.onLoad();
    }

    public void render(Graphics graphics) {
        if (this.background != null) {
            graphics.drawImage(this.background, 0, 0, map.getRoomWidth(), map.getRoomHeight(), null);
        }

        this.getObjects().stream().filter(object -> !(!Main.getInstance().isDebugging() && object instanceof Trigger)).forEach(object -> object.render(graphics));
    }

    public void update(float delta) {
        this.getObjects().forEach(object -> object.update(delta));
    }

    public void readMap(ReaderType type) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(this.path)));

        switch (type) {
            default:
            case ENGINE: {
                map = new EngineReader(bufferedReader, this);
                break;
            }

            case JTOOL: {
                map = new JToolReader(bufferedReader, this);
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
        return new int[] {this.map.getStartX(), this.map.getStartY()};
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

    public List<GameObject> getObjectsById(String id) {
        List<GameObject> objects = new ArrayList<>();

        for (GameObject object : this.map.getObjects())
            if (object.getCustomId().equalsIgnoreCase(id))
                objects.add(object);

        return objects;
    }

    public void reset() {
        try {
            Main.getInstance().getHandler().setRoom(this.getClass().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onLoad();
}
