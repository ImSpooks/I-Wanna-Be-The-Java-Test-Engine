package me.ImSpooks.iwbtgengine.game.room.init;

import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.objects.warps.Warp;
import me.ImSpooks.iwbtgengine.game.room.ReaderType;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Nick on 02 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class TestRoom extends Room {

    public TestRoom(GameHandler handler) {
        super(ReaderType.ENGINE, "/room/level/stage1/rStage01.json", handler);
//        super(ReaderType.ENGINE, "/room/level/stage1/DefaultRoom.json", handler);

        //this.background = Main.getInstance().getResourceHandler().get("test background");
        this.color = new Color(128, 255, 128);
    }

    @Override
    public void update(Camera camera, float delta) {
        super.update(camera, delta);
    }

    @Override
    public void onLoad() {
        //this.getHandler().getSoundManager().playSound("bgm", "/resources/sounds/music/musGuyRock.wav");
        this.getHandler().getSoundManager().playSound("bgm", "/resources/sounds/music/BC3.brstm").setVolume(0.2f);

        AtomicBoolean touched = new AtomicBoolean(false);

        for (GameObject gameObject : this.getObjectsById("trigger_1")) {
            Trigger trigger = (Trigger) gameObject;


            trigger.setOnTouch(kid -> {
                if (!touched.get()) {
                    touched.set(true);


                    for (GameObject gameObject1 : getObjectsById("triggerblock1")) {
                        gameObject1.removeObject();
                    }
                }
            });
        }

        for (GameObject gameObject : this.getObjectsById("warp_1")) {
            Warp warp = (Warp) gameObject;
            warp.setOnTouch(kid -> {
                Room room = this.getHandler().getMain().getRoomManager().getRoom(this.getHandler(), "stage1_room2");
                this.getHandler().setRoom(room);

                this.getHandler().getKid().setPosition(room.getMap().getStartX(), room.getMap().getStartY());
            });
        }

        super.onLoad();
    }
}
