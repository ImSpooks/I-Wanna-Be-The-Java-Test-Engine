package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Spike;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.game.room.RoomType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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


            JSONObject mapType = (JSONObject) map.get("map_type");

            this.setRoomType(RoomType.valueOf(((String) mapType.get("type")).toUpperCase()));

            JSONArray objects = (JSONArray) map.get("objects");

            //Iterate over employee array

            for (Object obj : objects) {
                GameObject gameObject = null;

                JSONObject object = (JSONObject) obj;

                String tile = (String) object.get("tile");
                String type = (String) object.get("type");

                int x = Math.toIntExact((long) object.get("x"));
                int y = Math.toIntExact((long) object.get("y"));

                switch (type.toLowerCase()) {
                    default: break;

                    case "misc": {
                        if (tile.startsWith("playerstart")) {
                            this.setStartX(x);
                            this.setStartY(y);
                        }
                        break;
                    }

                    case "blocks": {
                        gameObject = new Block(this.getRoom(), x, y, Sprite.generateSprite(this.getResourceHandler().getResource(tile)));
                        break;
                    }

                    case "killers": {
                        if (tile.startsWith("sprCherry")) {
                            // Cherry / Apple
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
