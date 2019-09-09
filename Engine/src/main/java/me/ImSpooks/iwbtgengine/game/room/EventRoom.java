package me.ImSpooks.iwbtgengine.game.room;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.event.EventHandler;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class EventRoom extends Room {

    @Getter private EventHandler eventHandler;

    public EventRoom(ReaderType type, String path, GameHandler handler) {
        super(type, path, handler);
    }

    @Override
    public void update(Camera camera, float delta) {
        this.eventHandler.update(delta);
    }
}
