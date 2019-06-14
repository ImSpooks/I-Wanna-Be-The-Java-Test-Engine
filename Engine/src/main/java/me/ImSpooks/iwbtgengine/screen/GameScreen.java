package me.ImSpooks.iwbtgengine.screen;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.event.EventHandler;
import me.ImSpooks.iwbtgengine.game.object.player.Kid;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class GameScreen extends AbstractScreen {

    @Getter private final EventHandler eventHandler;

    public GameScreen(Main game, GameHandler handler) {
        super(game, handler);

        this.eventHandler = new EventHandler();

        //TODO REMOVE
        this.getHandler().setRoom(this.getHandler().getMain().getRoomManager().getRoom("stage1_room1"));
        this.getHandler().setKid(new Kid(null, 32, 32, this.getHandler()));
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.CYAN);
        graphics.fillRect(0, 0, Global.GAME_WIDTH, Global.GAME_HEIGHT);

        graphics.setColor(new Color(0, 255, 255, 128).darker().darker());
        for (int i = 0; i < 20; i++) {
            graphics.drawLine(0, 32 * i,Global.GAME_WIDTH, 32 * i);
        }

        for (int i = 0; i < 30; i++) {
            graphics.drawLine(32 * i, 0, 32 * i, Global.GAME_HEIGHT);
        }

        this.getHandler().render(graphics);
    }

    @Override
    public void update(float delta) {
        this.eventHandler.update(delta);
        this.getHandler().update(delta);
    }

    @Override
    public void preLoad() {
        this.setCamera(new Camera());
    }

    @Override
    public void initUI() {

    }
}
