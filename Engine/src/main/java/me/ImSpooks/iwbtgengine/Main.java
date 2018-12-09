package me.ImSpooks.iwbtgengine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import lombok.Getter;
import me.ImSpooks.iwbtgengine.screen.GameScreen;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Main extends Game {


    @Getter private static String baseFolder = "src/main/resources/";

    @Getter private static Main instance;

    @Getter private GameScreen screen;

    public Main() {
        instance = this;


    }

    @Override
    public void create() {

        this.setScreen(this.screen = new GameScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.screen.update(Gdx.graphics.getDeltaTime());

        super.render();
    }
}
