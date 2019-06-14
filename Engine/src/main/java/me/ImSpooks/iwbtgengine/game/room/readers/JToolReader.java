package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Spike;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        int lastX = 0;
        int lastY = 0;
        int lastID = 0;

        System.out.println("Initializing room " + this.getClass().getName());
        long now = System.currentTimeMillis();

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

                            }

                            /*else if (id == 22) { // jump refresher
                                object = new JumpRefresher(x, y, ID.MISC, handler);
                                object.setSprite(getSprites().getJumpRefresher());
                                object.setX(object.getX() - object.getSpriteWidth() / 2);
                                object.setY(object.getY() - object.getSpriteHeight() / 2);
                            }*/

                            else if (id == 1) { // block
                                object = new Block(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("block_texture_default", BufferedImage.class)));
                            }

                            else if (id == 2) { // mini block
                                object = new Block(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("block-mini_texture_default", BufferedImage.class)));
                            }


                            else if (id >= 3 && id <= 10) {
                                String direction = "down";

                                if (id == 3 || id == 7) { // up
                                    direction = "up";
                                }
                                else if (id == 4 || id == 8) { // right
                                    direction = "right";
                                }
                                else if (id == 5 || id == 9) { // left
                                    direction = "left";
                                }

                                boolean isMini = id >= 7;

                                object = new Spike(this.getRoom(), x, y, new Sprite(this.getResourceHandler().get("spike-" + (isMini ? "mini-" : "") + direction + "_texture_default", BufferedImage.class)));
                            }

                            /*else if (id == 19) { // save blocker
                                object = new SaveBlocker(x, y, ID.MISC, handler);
                                object.setSprite(getSprites().getSaveBlocker());
                            }


                            else if (id == 18) { // killer blocker blockers
                                object = new KillerBlock(x, y, ID.KILLER_BLOCK, handler);
                                object.setSprite(getSprites().getKillerblockSprite());
                            }

                            //spikes


                            else if (id == 11) { // apple
                                object = new Cherry(x, y, ID.SPIKE, handler);
                                object.setSprite(getSprites().getCherrySprite());
                                object.setX(object.getX() - object.getSpriteWidth() / 2);
                                object.setY(object.getY() - object.getSpriteHeight() / 2);
                            }

                            else if (id == 12) { // save point
                                object = new Save(x, y, ID.SPIKE, handler);
                                object.setSprite(getSprites().getSaveSprite(SaveFile.Difficulty.HARD, ObjectDirection.DOWN));
                            }



                            else if (id == 13) { // platforms

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
                                this.getObjects().add(object);

                        } catch (NumberFormatException e) {
                            System.out.println("Something went wrong adding object at x = [" + splittedString[objects] + "], y = [" + splittedString[objects + 1] + "], id = [" + splittedString[objects + 2] + "]");
                            e.printStackTrace();
                        }
                        objects = objects + 3;
                    }
                    break;
                }
            }

            Collections.sort(ids);
            System.out.println(Arrays.toString(ids.toArray(new Integer[ids.size()])));

        } catch (Exception e) {
            System.out.println("Something went wrong adding object at x = [" + lastX + "], y = [" + lastY + "], id = [" + lastID + "]");
            e.printStackTrace();
        }
        System.out.println("Took " + (System.currentTimeMillis() - now) + " ms to initialize room " + this.getClass().getName());
    }
}
