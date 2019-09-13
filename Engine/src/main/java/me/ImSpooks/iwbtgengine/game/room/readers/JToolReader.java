package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.SaveBlocker;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Spike;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.JumpRefresher;
import me.ImSpooks.iwbtgengine.game.object.objects.saves.Save;
import me.ImSpooks.iwbtgengine.game.object.objects.warps.Warp;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Difficulty;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class JToolReader extends MapReader {

    public JToolReader(BufferedReader reader, Room room, Object... args) {
        super(reader, room, args);
    }

    @Override
    public void readMap() {
        this.setRoomWidth(Global.GAME_WIDTH);
        this.setRoomHeight(Global.GAME_HEIGHT);

        int lastX = 0;
        int lastY = 0;
        int lastID = 0;

        List<Integer> ids = new ArrayList<>();

        try {
            //reading room from jmap file

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("data repeated below for easy parsing by other tools")) {
                    reader.readLine();
                    line = reader.readLine();

                    String[] splittedString = line.split(" ");

                    int objects = 0;
                    while (splittedString.length >= objects && splittedString.length != objects) {
                        try {
                            int x = Integer.parseInt(splittedString[objects]);
                            int y = Integer.parseInt(splittedString[objects + 1]);
                            int id = Integer.parseInt(splittedString[objects + 2]);

                            lastX = x;
                            lastY = y;
                            lastID = id;

                            GameObject object = null;

                            if (!ids.contains(id))
                                ids.add(id);

                            if (id == 20) { // start
                                this.setStartX(x);
                                this.setStartY(y);
                            }

                            else if (id == 21) { // warp
                                object = new Warp(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("sprWarp", BufferedImage.class)));
                            }

                            else if (id == 22) { // jump refresher
                                object = new JumpRefresher(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("sprJumpRefresher", BufferedImage.class)));
                            }

                            else if (id == 1) { // block
                                object = new Block(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("sprBlock", BufferedImage.class)));
                            }

                            else if (id == 2) { // mini block
                                object = new Block(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("sprMiniblock", BufferedImage.class)));
                            }


                            else if (id >= 3 && id <= 10) {
                                String direction = "Down";

                                if (id == 3 || id == 7) { // up
                                    direction = "Up";
                                }
                                else if (id == 4 || id == 8) { // right
                                    direction = "Right";
                                }
                                else if (id == 5 || id == 9) { // left
                                    direction = "Left";
                                }

                                boolean isMini = id >= 7;

                                object = new Spike(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("spr" + (!isMini ? "Spike" : "Mini") + direction, BufferedImage.class)));
                            }

                            else if (id == 19) { // save blocker
                                object = new SaveBlocker(this.getRoom(), x, y, null);
                            }


                            /*else if (id == 18) { // killer blocker blockers
                                object = new KillerBlock(x, y, ID.KILLER_BLOCK, handler);
                                object.setSprite(getSprites().getKillerblockSprite());
                            }

                            //spikes


                            else if (id == 11) { // apple
                                object = new Cherry(x, y, ID.SPIKE, handler);
                                object.setSprite(getSprites().getCherrySprite());
                                object.setX(object.getX() - object.getSpriteWidth() / 2);
                                object.setY(object.getY() - object.getSpriteHeight() / 2);
                            }*/

                            else if (id == 12) { // save point
                                object = new Save(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("sprSave", BufferedImage.class)), Difficulty.MEDIUM, false);
                            }



                            /*else if (id == 13) { // platforms

                            }
                            else if (id == 14) { // water
                                object = new Wootar(x, y, ID.WATER, handler, Wootar.WaterType.FULL_JUMP);
                                object.setSprite(getSprites().getWaterSprite());
                            }
                            else if (id == 15) { // water
                                object = new Wootar(x, y, ID.WATER, handler, Wootar.WaterType.NO_JUMP);
                                object.setSprite(getSprites().getWaterSprite());
                            }
                            else if (id == 23) { // double jump water
                                object = new Wootar(x, y, ID.WATER, handler, Wootar.WaterType.DOUBLE_JUMP);
                                object.setSprite(getSprites().getWaterSprite());
                            }*/

                            else if (id == 17) { // vine left

                            }

                            if (object != null)
                                this.addObject(object);

                        } catch (NumberFormatException e) {
                            System.out.println("Something went wrong adding object at x = [" + splittedString[objects] + "], y = [" + splittedString[objects + 1] + "], id = [" + splittedString[objects + 2] + "]");
                            e.printStackTrace();
                        }
                        objects = objects + 3;
                    }
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Something went wrong adding object at x = [" + lastX + "], y = [" + lastY + "], id = [" + lastID + "]");
            e.printStackTrace();
        }
    }
}
