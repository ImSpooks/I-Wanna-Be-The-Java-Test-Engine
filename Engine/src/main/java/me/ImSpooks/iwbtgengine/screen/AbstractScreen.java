package me.ImSpooks.iwbtgengine.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.ImSpooks.iwbtgengine.Main;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class AbstractScreen implements Screen {

    private final Main game;

    public AbstractScreen(Main game) {
        this.game = game;
    }

    public Main getGame() {
        return game;
    }

    public abstract void update(float delta);

    public abstract Stage getUiStage();
}
