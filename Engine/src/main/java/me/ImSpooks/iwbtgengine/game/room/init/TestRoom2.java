package me.ImSpooks.iwbtgengine.game.room.init;

import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.warps.Warp;
import me.ImSpooks.iwbtgengine.game.room.ReaderType;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

/**
 * Created by Nick on 02 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class TestRoom2 extends Room {

    public TestRoom2(GameHandler handler) {
        super(ReaderType.JTOOL, "/room/level/stage1/room1.jmap", handler);

        //this.background = Main.getInstance().getResourceHandler().get("test background");
        this.shiftBackgroundImage = true;
    }

    @Override
    public void update(Camera camera, float delta) {
        super.update(camera, delta);
    }

    @Override
    public void onLoad() {
        this.getHandler().getSoundManager().playSound("bgm", "/resources/sounds/music/Test.wav").setVolume(0.2f);

        for (GameObject gameObject : this.getObjects()) {
            if (!(gameObject instanceof Warp)) continue;

            Warp warp = (Warp) gameObject;
            warp.setOnTouch(kid -> {
                Room room = this.getHandler().getMain().getRoomManager().getRoom(this.getHandler(), "stage1_room1");
                this.getHandler().setRoom(room);
                this.getHandler().getKid().setPosition(room.getMap().getStartX(), room.getMap().getStartY());
            });
        }

        super.onLoad();
    }
}
