package me.ImSpooks.iwbtgengine.game.room.init;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.events.TouchObject;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.room.ReaderType;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Nick on 02 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class TestRoom extends Room {

    public TestRoom() {
        super(ReaderType.ENGINE, "/room/level/stage1/DefaultRoom.json");

        this.background = Main.getInstance().getResourceHandler().get("test background");
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void onLoad() {
        AtomicBoolean touched = new AtomicBoolean(false);

        for (GameObject gameObject : this.getObjectsById("trigger1")) {
            Trigger trigger = (Trigger) gameObject;


            trigger.setOnTouch(new TouchObject() {
                @Override
                public void onTouch() {
                    if (!touched.get()) {
                        touched.set(true);


                        for (GameObject gameObject : getObjectsById("triggerspike1")) {
                            gameObject.setVelX(3);
                        }
                    }
                }
            });
        }
    }
}
