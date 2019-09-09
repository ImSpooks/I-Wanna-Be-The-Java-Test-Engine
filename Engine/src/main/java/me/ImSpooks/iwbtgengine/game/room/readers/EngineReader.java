package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Cherry;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.ColoredCherry;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Spike;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.JumpRefresher;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.objects.warps.Warp;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.game.room.RoomType;
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

                switch (type.toLowerCase()) {
                    default: break;

                    case "misc": {
                        if (tile.startsWith("playerstart")) {
                            this.setStartX(x);
                            this.setStartY(y);
                        }
                        else if (tile.startsWith("sprJumpRefresher")) {
                            gameObject = new JumpRefresher(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));
                        }
                        break;
                    }

                    case "blocks": {
                        gameObject = new Block(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));
                        break;
                    }

                    case "killers": {
                        if (tile.startsWith("sprCherry")) {
                            if (tile.endsWith("White"))
                                gameObject = new ColoredCherry(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)), Color.GREEN);
                            else
                                gameObject = new Cherry(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));
                        }
                        else if (tile.startsWith("sprSpike") || tile.startsWith("sprMini")) {
                            gameObject = new Spike(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));

                        }
                        break;
                    }

                    case "triggers": {
                        if (tile.equalsIgnoreCase("sprTriggerMask")) {
                            gameObject = new Trigger(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));
                        }
                        break;
                    }

                    case "warps": {
                        if (tile.equalsIgnoreCase("sprWarp")) {
                            gameObject = new Warp(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));
                        }
                        break;
                    }
                }


                if (gameObject != null) {
                    gameObject.setCustomId((String) object.get("custom_id"));
                    this.getObjects().add(gameObject);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
