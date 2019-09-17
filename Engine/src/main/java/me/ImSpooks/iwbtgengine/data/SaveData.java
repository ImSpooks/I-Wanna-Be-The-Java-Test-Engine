package me.ImSpooks.iwbtgengine.data;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.item.GameItems;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Difficulty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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

    @Getter private File file;

    public SaveData(int saveId) {
        this.saveId = saveId;

        this.file = new File(Main.getInstance().getFileManager().getDirectory() + File.separator + "saves", "save" + saveId);
        if (!file.exists()) {
            try {
                if (!new File(Main.getInstance().getFileManager().getDirectory() + File.separator + "saves").exists())
                    new File(Main.getInstance().getFileManager().getDirectory() + File.separator + "saves").mkdir();

                file.createNewFile();

                // creates a default save file if save file doesn't exist
                Room defaultRoom = Main.getInstance().getRoomManager().getRoomsById().get(0);

                this.roomId = defaultRoom.getInternalId();
                this.x = defaultRoom.getMap().getStartX();
                this.y = defaultRoom.getMap().getStartY();
                this.flippedGravity = false;
                this.time = 0L;
                this.deaths = 0;
                this.difficulty = Difficulty.HARD;
                this.items = new ArrayList<>();

                this.save();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.read();
    }

    public SaveData(int saveId, String roomId, int x, int y, boolean flippedGravity, long time, int deaths, Difficulty difficulty, List<GameItems> items) {
        this.saveId = saveId;
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

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file))) {
            String output = this.serialize();


            bufferedOutputStream.write(Base64.getEncoder().encode(output.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        //TODO read file

        StringBuilder output = new StringBuilder();

        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file))) {
            int ch = bufferedInputStream.read();
            while(ch != -1) {
                output.append((char) ch);
                ch = bufferedInputStream.read();
            }
        } catch (IOException e) {
           e.printStackTrace();
        }

        String decoded = new String(Base64.getDecoder().decode(output.toString()));

        SaveData data = SaveData.deserialize(decoded);

        this.items = data.getItems();
        this.roomId = data.getRoomId();
        this.x = data.getX();
        this.y = data.getY();
        this.flippedGravity = data.isFlippedGravity();
        this.time = data.getTime();
        this.deaths = data.getDeaths();
        this.difficulty = data.getDifficulty();
    }

    @SuppressWarnings("unchecked")
    public String serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("saveId", this.saveId);
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

            int saveId = Math.toIntExact(map.get("saveId", Long.class));
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

            return new SaveData(saveId, roomId, x, y, flippedGravity, time, deaths, difficulty, items);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
