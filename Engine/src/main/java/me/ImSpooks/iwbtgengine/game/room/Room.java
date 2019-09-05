package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.KillerObject;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.room.readers.EngineReader;
import me.ImSpooks.iwbtgengine.game.room.readers.JToolReader;
import me.ImSpooks.iwbtgengine.game.room.readers.MapReader;
import me.ImSpooks.iwbtgengine.global.ErrorCodes;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Room {


    @Getter private MapReader map;

    private String path;
    @Getter private GameHandler handler;

    @Getter @Setter private String internalId = "";

    @Getter protected BufferedImage background = null;

    @Getter protected boolean shiftBackroundImage = true;

    public Room(ReaderType type, String path) {
        this.path = path;

        this.readMap(type);

        this.onLoad();
    }

    public void render(Camera camera, Graphics graphics) {
        if (this.background != null) {
            if (this.map.getRoomType() == RoomType.SHIFT && shiftBackroundImage) graphics.drawImage(this.background, 0, 0, Global.GAME_WIDTH, Global.GAME_HEIGHT, null);
            else graphics.drawImage(this.background, 0 - camera.getCameraX(), 0 - camera.getCameraY(), map.getRoomWidth(), map.getRoomHeight(), null);
        }

        this.getObjects().stream().filter(object -> !(!Main.getInstance().isDebugging() && object instanceof Trigger)).forEach(object -> object.render(camera, graphics));
    }

    public void update(Camera camera, float delta) {
        this.getObjects().forEach(object -> object.update(delta));

        if (this.map.getRoomType() == RoomType.NORMAL) {
            if (camera.isSmoothTransition()) {
                camera.setSmoothTransition(false);
            }
            camera.setCameraPosition(0, 0);
        }

        else if (this.getMap().getRoomType() == RoomType.SCROLLING) {
            if (!camera.isSmoothTransition()) {
                camera.setSmoothTransition(true);
            }

            int kidX = (int) Math.floor(this.getHandler().getKid().getX());
            int kidY = (int) Math.floor(this.getHandler().getKid().getY());

            int x = 0, y = 0;

            if (this.getHandler().getKid() != null) {
                x = (int) Math.floor(kidX - Global.GAME_WIDTH / 2.0 + 16);
                y = (int) Math.floor(kidY - Global.GAME_HEIGHT / 2.0 + 16);
            }

            if (!this.getHandler().getKid().isDeath())
                camera.setCameraPosition(Math.max(Math.min(x, this.map.getRoomWidth() - Global.GAME_WIDTH), 0), Math.max(Math.min(y, this.map.getRoomHeight() - Global.GAME_HEIGHT - 2), 0));
        }

        else if (this.getMap().getRoomType() == RoomType.SHIFT) {
            if (camera.isSmoothTransition()) {
                camera.setSmoothTransition(false);
            }

            int kidX = (int) Math.floor(this.getHandler().getKid().getX());
            int kidY = (int) Math.floor(this.getHandler().getKid().getY());

            int x = 0, y = 0;

            if (this.getHandler().getKid() != null) {
                x = (int) Math.floor((kidX + 16.0) / Global.GAME_WIDTH) * Global.GAME_WIDTH;
                y = (int) Math.floor((kidY + 16.0) / Global.GAME_HEIGHT) * Global.GAME_HEIGHT;
            }

            if (!this.getHandler().getKid().isDeath())
                camera.setCameraPosition(Math.max(Math.min(x, this.map.getRoomWidth()), 0), Math.max(Math.min(y, this.map.getRoomHeight()), 0));
        }
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
            long now = System.nanoTime();
            Main.getInstance().getHandler().setRoom(this.getClass().newInstance().setHandler(this.getHandler()));
            long time = System.nanoTime() - now;

            if (time > TimeUnit.MILLISECONDS.toNanos(20)) {
                System.out.println(String.format("Reloading room took more than a single frame (%s ns, %s ms)", time, time / 1000000L));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onLoad();

    public Room setHandler(GameHandler handler) {
        this.handler = handler;
        return this;
    }
}
