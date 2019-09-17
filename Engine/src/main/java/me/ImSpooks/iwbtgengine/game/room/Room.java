package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.player.KidState;
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
import java.util.Comparator;
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

    @Getter protected boolean shiftBackgroundImage = true;

    public Room(ReaderType type, String path, GameHandler handler) {
        this.path = path;
        this.handler = handler;

        this.readMap(type);
    }

    public void render(Camera camera, Graphics graphics) {
        if (this.background != null) {
            if (this.map.getRoomType() == RoomType.SHIFT && shiftBackgroundImage) graphics.drawImage(this.background, 0, 0, Global.GAME_WIDTH, Global.GAME_HEIGHT, null);
            else graphics.drawImage(this.background, 0 - camera.getCameraX(), 0 - camera.getCameraY(), map.getRoomWidth(), map.getRoomHeight(), null);
        }

        this.getMap().getObjects().stream()
                .filter(object -> !(!Main.getInstance().isDebugging() && object instanceof Trigger && !((Trigger) object).isVisible()))
                .forEach(object -> object.render(camera, graphics));

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

            if (this.getHandler().getKid() != null && (Math.abs(camera.getCameraX() - kidX) + Math.abs(camera.getCameraY() + kidY)) > 32) {
                x = (int) Math.floor(kidX - Global.GAME_WIDTH / 2.0 + 16);
                y = (int) Math.floor(kidY - Global.GAME_HEIGHT / 2.0 + 16);
            }

            if (this.getHandler().getKid().getKidState() != KidState.DEAD)
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
                x = (int) Math.floor((kidX + 16.0) / Global.GAME_WIDTH);
                y = (int) Math.floor((kidY + 16.0) / Global.GAME_HEIGHT);

                x = x * Global.GAME_WIDTH;
                y = -y + y * Global.GAME_HEIGHT;
            }

            if (this.getHandler().getKid().getKidState() != KidState.DEAD)
                camera.setCameraPosition(Math.max(Math.min(x, this.map.getRoomWidth() - Global.GAME_WIDTH), 0), Math.max(Math.min(y, this.map.getRoomHeight() - Global.GAME_HEIGHT), 0));
        }
    }

    private void readMap(ReaderType type) {
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

            List<GameObject> renderedObjects = map.getObjects();
            renderedObjects.sort(Comparator.comparingInt(a -> a.getClass().getAnnotation(ObjectPriority.class).renderPriority().getPriority()));
            map.setObjects(renderedObjects);
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
        return this.getMap().getObjects();
    }

    public GameObject addObject(GameObject object) {
        List<GameObject> objects = this.map.getObjects();
        objects.add(object);
        this.map.setObjects(objects);
        return object;
    }

    public List<GameObject> getObjectsAt(double x, double y, List<GameObject> existingObjects) {
        List<GameObject> list = new ArrayList<>();

        for (GameObject gameObject : this.getObjects()) {
            if (existingObjects.contains(gameObject)) continue;
            if (x >= gameObject.getX() && x < gameObject.getX() + gameObject.getWidth()) {
                if (y >= gameObject.getY() && y < gameObject.getY() + gameObject.getHeight()) {
                    list.add(gameObject);
                }
            }
        }

        return list;
    }

    public List<GameObject> getObjectsAt(double x, double y) {
        List<GameObject> list = new ArrayList<>();

        for (GameObject gameObject : this.getObjects()) {
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

        for (GameObject object : this.getObjects())
            if (object.getCustomId().equalsIgnoreCase(id))
                objects.add(object); return objects;
    }

    public Room reset() {
        try {
            long now = System.nanoTime();

            Room room = this.getClass().getConstructor(GameHandler.class).newInstance(this.getHandler());
            room.setInternalId(this.getInternalId());
            Main.getInstance().getHandler().setRoom(room);

            this.getHandler().getParticleManager().clear();

            long time = System.nanoTime() - now;

            if (time > TimeUnit.MILLISECONDS.toNanos(20)) {
                System.out.println(String.format("Reloading room took more than a single frame (%s ns, %s ms)", time, time / 1000000L));
            }
            return room;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void resetObjects() {
        /*for (GameObject object : this.getObjects()) {
            if (object instanceof Sign) {
                ((Sign) object).setVisible(false);
            }
        }*/
    }

    public abstract void onLoad();

}
