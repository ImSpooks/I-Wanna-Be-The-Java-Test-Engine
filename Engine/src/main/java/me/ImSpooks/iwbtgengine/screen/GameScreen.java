package me.ImSpooks.iwbtgengine.screen;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.data.SaveData;
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


    public GameScreen(Main game, GameHandler handler) {
        super(game, handler);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        graphics.setFont(this.getGame().getResourceManager().get("/resources/font/Determination.ttf", Font.class));
        graphics.setColor(Color.CYAN);
        graphics.fillRect(0, 0, Global.GAME_WIDTH, Global.GAME_HEIGHT);

        graphics.setColor(new Color(0, 255, 255, 128).darker().darker());

        if (this.getGame().isDebugging()) {
            for (int i = (int) Math.floor(camera.getCameraX() / 32.0); i < (int) Math.ceil(camera.getCameraX() / 32.0) + 26; i++) {
                graphics.drawLine(32 * i - camera.getCameraX(), 0, 32 * i - camera.getCameraX(), Global.GAME_HEIGHT);
            }

            for (int i = (int) Math.floor(camera.getCameraY() / 32.0); i < (int) Math.floor(camera.getCameraY() / 32.0) + 20; i++) {
                graphics.drawLine(0, 32 * i - camera.getCameraY(), Global.GAME_WIDTH, 32 * i - camera.getCameraY());
            }
        }

        this.getHandler().render(camera, graphics);
    }

    @Override
    public void update(Camera camera, float delta) {
        this.getCamera().update(delta);
        this.getHandler().update(camera, delta);
    }

    @Override
    public void preLoad() {
        this.setCamera(new Camera());

        SaveData data = this.getHandler().getSaveData();

        this.getHandler().setRoom(this.getHandler().getMain().getRoomManager().getRoom(this.getHandler(), data.getRoomId()));

        this.getHandler().setKid(new Kid(null, data.getX(), data.getY(), this.getHandler()));
        this.getHandler().getKid().reset();
    }

    @Override
    public void initUI() {

    }
}
