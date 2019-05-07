package me.ImSpooks.iwbtgengine.screen;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class AbstractScreen {

    /**
     * Instance of the main class
     */
    @Getter private final Main game;

    /**
     * Instance of the game handler class
     */
    @Getter private final GameHandler handler;

    /**
     * Camera instance
     */
    @Getter @Setter private Camera camera;


    public AbstractScreen(Main game, GameHandler handler) {
        this.game = game;
        this.handler = handler;
    }

    /**
     * Called when a screen updates.
     *
     * @param delta Framerate of the screen update
     */
    public abstract void update(float delta);

    /**
     * Called when a screen renders.
     *
     * @param graphics Graphics of the game
     */
    public abstract void render(Graphics graphics);

    /**
     * Called before initUI(). In here you load the stuff you will need when initializing the UI.
     */
    public abstract void preLoad();

    /**
     * Called when the screen is created.
     * In here you should initialize everything needed for that screen.
     */
    public abstract void initUI();
}
