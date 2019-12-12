package me.ImSpooks.iwbtgengine.game.room.readers;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.SaveBlocker;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Cherry;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.KillerBlock;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.Spike;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.JumpRefresher;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.Walljump;
import me.ImSpooks.iwbtgengine.game.object.objects.misc.Water;
import me.ImSpooks.iwbtgengine.game.object.objects.platforms.Platform;
import me.ImSpooks.iwbtgengine.game.object.objects.saves.Save;
import me.ImSpooks.iwbtgengine.game.object.objects.warps.Warp;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Difficulty;
import me.ImSpooks.iwbtgengine.global.Global;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 * @deprecated
 */
@Deprecated
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
            // discontinued

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
                                object = new Warp(this.getRoom(), x, y, this.getSprite("sprites/warps/default/sprWarp.png"));
                            }

                            else if (id == 22) { // jump refresher
                                object = new JumpRefresher(this.getRoom(), x, y, this.getSprite("sprites/misc/default/sprJumpRefresher.png"));
                            }

                            else if (id == 1) { // block
                                object = new Block(this.getRoom(), x, y, this.getSprite("sprites/blocks/default/sprBlock.png"));
                            }

                            else if (id == 2) { // mini block
                                object = new Block(this.getRoom(), x, y, this.getSprite("sprites/blocks/default/sprMiniblock.png"));
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

                                object = new Spike(this.getRoom(), x, y, this.getSprite("sprites/killers/default/spr" + (!isMini ? "Spike" : "Mini") + direction + ".png"));
                            }

                            else if (id == 19) { // save blocker
                                object = new SaveBlocker(this.getRoom(), x, y, null);
                            }


                            else if (id == 18) { // killer blocker
                                object = new KillerBlock(this.getRoom(), x, y, this.getSprite("sprites/killers/default/sprKillerBlock.png"));
                            }

                            //spikes


                            else if (id == 11) { // apple
                                object = new Cherry(this.getRoom(), x, y, this.getSprite("sprites/killers/default/sprCherry.gif"));

                                object.setX(object.getX() - object.getSprite().getImage().getWidth() / 2.0);
                                object.setY(object.getY() - object.getSprite().getImage().getHeight() / 2.0);
                            }

                            else if (id == 12) { // save point
                                object = new Save(this.getRoom(), x, y, this.getSprite("sprites/saves/default/sprSave.png"), Difficulty.MEDIUM, false);
                            }



                            else if (id == 13) { // platforms
                                object = new Platform(this.getRoom(), x, y, this.getSprite("sprites/platforms/default/sprMovingPlatform.png"));
                            }
                            else if (id == 14) { // water
                                object = new Water(this.getRoom(), x, y, this.getSprite("sprites/misc/default/sprWater.png"), Water.WaterType.FULL_JUMP);
                            }
                            else if (id == 15) { // water
                                object = new Water(this.getRoom(), x, y, this.getSprite("sprites/misc/default/sprWater.png"), Water.WaterType.SINGLE_JUMP);
                            }
                            else if (id == 23) { // double jump water
                                object = new Water(this.getRoom(), x, y, this.getSprite("sprites/misc/default/sprWater.png"), Water.WaterType.DOUBLE_JUMP);
                            }

                            else if (id == 17) { // vine left
                                object = new Walljump(this.getRoom(), x, y, this.getSprite("sprites/misc/default/sprWalljumpR.png"), false);
                            }
                            else if (id == 18) {
                                object = new Walljump(this.getRoom(), x, y, this.getSprite("sprites/misc/default/sprWalljumpL.png"), true);
                            }

                            if (object != null)
                                this.addObject(object);
                            else if (id != 20) Logger.warn("Unknown object id found with id \"{}\"", id);

                        } catch (NumberFormatException e) {
                            Logger.warn(e, "Something went wrong adding object at x = [{}], y = [{}], id = [{}]", splittedString[objects], splittedString[objects + 1], splittedString[objects + 2]);
                        }
                        objects = objects + 3;
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Logger.warn(e, "Something went wrong adding object at x = [{}], y = [{}], id = [{}]", lastX, lastY, lastID);
        }
    }
}