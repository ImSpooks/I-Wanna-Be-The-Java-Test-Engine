package me.ImSpooks.iwbtgengine.data;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.game.item.GameItems;
import me.ImSpooks.iwbtgengine.global.Difficulty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class SaveData {

    @Getter @Setter private int saveId;

    @Getter @Setter private String roomId;
    @Getter @Setter private int x, y;
    @Getter @Setter public boolean flippedGravity;

    @Getter @Setter private long time;
    @Getter @Setter private int deaths;

    @Getter private Difficulty difficulty;

    @Getter @Setter private List<GameItems> items;


    public SaveData(int saveId) {
        this.saveId = saveId;
        this.read();
    }

    public SaveData(String roomId, int x, int y, boolean flippedGravity, long time, int deaths, Difficulty difficulty, List<GameItems> items) {
        this.items = items;
        this.roomId = roomId;
        this.x = x;
        this.y = y;
        this.flippedGravity = flippedGravity;
        this.time = time;
        this.deaths = deaths;
        this.difficulty = difficulty;
    }

    public void save() {
        //TODO write to file
    }

    public void read() {
        //TODO read file
        this.items = new ArrayList<>();
        this.roomId = "";
        this.x = -1;
        this.y = -1;
        this.flippedGravity = false;
        this.time = 0L;
        this.deaths = 0;
        this.difficulty = Difficulty.HARD;
    }

    @SuppressWarnings("unchecked")
    public String serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("roomId", this.roomId);
        jsonObject.put("x", this.x);
        jsonObject.put("y", this.y);
        jsonObject.put("flippedGravity", this.flippedGravity);
        jsonObject.put("time", this.time);
        jsonObject.put("deaths", this.deaths);

        List<Integer> itemIds = new ArrayList<>();

        for (GameItems item : this.items) {
            itemIds.add(item.getId());
        }

        jsonObject.put("difficulty", difficulty.getId());

        jsonObject.put("items", itemIds.toArray(new Integer[0]));

        return jsonObject.toString();
    }

    public static SaveData deserialize(String input) {
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject map = (JSONObject) jsonParser.parse(input);

            String roomId = map.get("roomId", String.class);
            int x = Math.toIntExact(map.get("x", Long.class));
            int y =  Math.toIntExact(map.get("y", Long.class));
            boolean flippedGravity = map.get("flippedGravity", Boolean.class);
            int time =  Math.toIntExact(map.get("time", Long.class));
            int deaths =  Math.toIntExact(map.get("deaths", Long.class));
            Difficulty difficulty = Difficulty.getFromId(Math.toIntExact(map.get("difficulty", Long.class)));

            List<GameItems> items = new ArrayList<>();

            JSONArray array = map.get("items", JSONArray.class);

            for (Object o : array) {
                int id = Integer.parseInt((String) o);
                items.add(GameItems.getFromId(id));
            }

            return new SaveData(roomId, x, y, flippedGravity, time, deaths, difficulty, items);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
