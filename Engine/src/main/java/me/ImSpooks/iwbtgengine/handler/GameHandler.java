package me.ImSpooks.iwbtgengine.handler;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.data.SaveData;
import me.ImSpooks.iwbtgengine.event.EventHandler;
import me.ImSpooks.iwbtgengine.game.object.particles.init.ParticleManager;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.sound.SoundManager;

import java.awt.*;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class GameHandler {

    @Getter private final Main main;

    @Getter private Room room;

    @Getter @Setter private KidObject kid;
    @Getter @Setter private SaveData saveData;

    @Getter private final ParticleManager particleManager;
    @Getter private final EventHandler eventHandler;

    public GameHandler(Main main) {
        this.main = main;
        this.particleManager = new ParticleManager(this);
        this.eventHandler = new EventHandler(this);

        this.saveData = new SaveData(1);

        Global.GRAVITY = !this.saveData.flippedGravity ? 1.0 : -1.0;
    }

    public void render(Camera camera, Graphics graphics) {
        if (this.room != null) {
            room.render(camera, graphics);
        }
        if (kid != null) {
            kid.render(camera, graphics);
        }
        this.particleManager.render(camera, graphics);
    }

    public void update(Camera camera, float delta) {
        this.eventHandler.update(delta);

        if (this.room != null) {
            room.update(camera, delta);
        }
        if (kid != null) {
            kid.update(delta);
        }

        this.particleManager.update(delta);
    }

    public void setRoom(Room room) {
        room.onLoad();
        this.room = room;
    }

    public SoundManager getSoundManager() {
        return this.getMain().getSoundManager();
    }
}
