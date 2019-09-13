package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.SaveBlocker;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Cherry;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.ColoredCherry;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Spike;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.Gravity;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.JumpRefresher;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.Walljump;
import me.ImSpooks.iwbtgengine.game.object.objects.saves.Save;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.objects.warps.Warp;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.game.room.RoomType;
import me.ImSpooks.iwbtgengine.global.Difficulty;
import me.ImSpooks.iwbtgengine.global.Global;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.BufferedReader;

/**
 * Created by Nick on 03 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class EngineReader extends MapReader {

    public EngineReader(BufferedReader reader, Room room, Object... args) {
        super(reader, room);
    }

    @Override
    public void readMap() {
        int lastX = 0;
        int lastY = 0;

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
            //Read JSON file
            JSONObject map = (JSONObject) jsonParser.parse(reader);


            JSONObject mapType = map.get("map_type", JSONObject.class);

            this.setRoomType(RoomType.valueOf(mapType.get("type", String.class).toUpperCase()));

            switch (this.getRoomType()) {
                default:
                case NORMAL: {
                    this.setRoomWidth(Global.GAME_WIDTH);
                    this.setRoomHeight(Global.GAME_HEIGHT);
                    break;
                }
                case SCROLLING: {
                    this.setRoomWidth(Math.toIntExact(mapType.get("width", Long.class)));
                    this.setRoomHeight(Math.toIntExact(mapType.get("height", Long.class)));
                    break;
                }
                case SHIFT: {
                    this.setRoomWidth(Math.toIntExact(mapType.get("rooms_horizontal", Long.class) * Global.GAME_WIDTH));
                    this.setRoomHeight(Math.toIntExact(mapType.get("rooms_vertical", Long.class) * Global.GAME_HEIGHT));
                    break;
                }
            }

            JSONArray objects = map.get("objects", JSONArray.class);

            //Iterate over employee array

            for (Object obj : objects) {
                GameObject gameObject = null;

                JSONObject object = (JSONObject) obj;

                String tile = object.get("tile", String.class);
                String type = object.get("type", String.class);

                int x = Math.toIntExact(object.get("x", Long.class));
                int y = Math.toIntExact(object.get("y", Long.class));

                String objType = tile.replace("spr", "");

                switch (type.toLowerCase()) {
                    default: break;

                    case "blocks": {
                        if (objType.startsWith("SaveBlocker")) {
                            gameObject = new SaveBlocker(this.getRoom(), x, y, this.getSprite(tile));
                        }
                        else {
                            gameObject = new Block(this.getRoom(), x, y, this.getSprite(tile));
                        }
                        break;
                    }

                    case "killers": {
                        if (objType.startsWith("Cherry")) {
                            if (objType.endsWith("White"))
                                gameObject = new ColoredCherry(this.getRoom(), x, y, this.getSprite(tile), Color.GREEN);
                            else
                                gameObject = new Cherry(this.getRoom(), x, y, this.getSprite(tile));
                        }
                        else if (objType.startsWith("Spike") || objType.startsWith("Mini")) {
                            gameObject = new Spike(this.getRoom(), x, y, this.getSprite(tile));

                        }
                        break;
                    }

                    case "misc": {
                        if (objType.startsWith("playerstart")) {
                            this.setStartX(x);
                            this.setStartY(y);
                        }
                        else if (objType.startsWith("JumpRefresher")) {
                            gameObject = new JumpRefresher(this.getRoom(), x, y, this.getSprite(tile));
                        }
                        else if (objType.startsWith("Walljump")) {
                            gameObject = new Walljump(this.getRoom(), x, y, this.getSprite(tile), tile.endsWith("L") || tile.endsWith("Left"));
                        }

                        else if (objType.startsWith("Gravity")) {
                            boolean up = objType.endsWith("Up");
                            gameObject = new Gravity(this.getRoom(), x, y, this.getSprite(tile), up);
                        }
                        break;
                    }

                    case "saves": {
                        if (this.getRoom().getHandler().getSaveData().getDifficulty() == Difficulty.IMPOSSIBLE)
                            continue;

                        boolean flip = objType.endsWith("Flip");
                        Difficulty difficulty = Difficulty.HARD;

                        if (objType.contains(Difficulty.MEDIUM.getInternalName())) difficulty = Difficulty.MEDIUM;
                        if (objType.contains(Difficulty.VERY_HARD.getInternalName())) difficulty = Difficulty.VERY_HARD;

                        if (difficulty != Difficulty.HARD)
                            tile = tile.replace(difficulty.getInternalName(), "");

                        if (difficulty.getId() < this.getRoom().getHandler().getSaveData().getDifficulty().getId())
                            continue;

                        gameObject = new Save(this.getRoom(), x, y, this.getSprite(tile), difficulty, flip);
                        break;
                    }

                    case "triggers": {
                        if (objType.startsWith("TriggeMaskr")) {
                            gameObject = new Trigger(this.getRoom(), x, y, this.getSprite(tile));
                        }
                        break;
                    }

                    case "warps": {
                        if (objType.endsWith("Warp")) {
                            gameObject = new Warp(this.getRoom(), x, y, this.getSprite(tile));
                        }
                        break;
                    }
                }


                if (gameObject != null) {
                    gameObject.setCustomId((String) object.get("custom_id"));
                    this.addObject(gameObject);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Sprite getSprite(String tile) {
        return Sprite.generateSprite(this.getResourceHandler().getResource(tile));
    }
}
