package me.ImSpooks.iwbtgengine.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.event.EventHandler;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class GameScreen extends AbstractScreen {

    @Getter private final EventHandler eventHandler;

    @Getter private Stage uiStage;
    @Getter private Camera camera;
    private Viewport gameViewport;

    public GameScreen(Main game) {
        super(game);

        this.eventHandler = new EventHandler();
    }

    @Override
    public void render(float v) {

    }

    @Override
    public void update(float delta) {
        this.eventHandler.update(delta);
    }

    private int uiScale = 2;
    private void initUI() {
        this.camera = new Camera();

        this.uiStage = new Stage(this.gameViewport = new ScreenViewport());
        this.uiStage.getViewport().update(Gdx.graphics.getWidth() / uiScale, Gdx.graphics.getHeight() / uiScale);
    }



    @Override
    public void show() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
