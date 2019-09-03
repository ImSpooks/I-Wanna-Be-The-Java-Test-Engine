package me.ImSpooks.iwbtgengine;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.game.room.RoomManager;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;
import me.ImSpooks.iwbtgengine.handler.ResourceHandler;
import me.ImSpooks.iwbtgengine.keycontroller.KeyController;
import me.ImSpooks.iwbtgengine.screen.AbstractScreen;
import me.ImSpooks.iwbtgengine.screen.GameScreen;

import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Main extends Canvas {


    // Instance of the main class
    @Getter private static Main instance;

    // Location of the base folder (must be empty when building)
    @Getter private static final String baseFolder = "src/main/resources/";

    // Resource Handler
    @Getter private final ResourceHandler resourceHandler = new ResourceHandler(this);

    // Game screen
    @Getter private AbstractScreen screen;

    // Logger
    @Getter private final Logger logger = Logger.getLogger(this.getClass().getName());

    // Key Controller
    @Getter private KeyController keyController;

    // Frame of the game
    @Getter private Window window;

    // Render manager
    @Getter private RenderManager renderManager;

    // Room manager + cached rooms
    @Getter private RoomManager roomManager;


    public Main() {
        instance = this;
        create();
    }

    public void create() {
        long startTime = System.currentTimeMillis();
        logger.info("Loading game...");

        this.window = new Window(this, Global.GAME_NAME, Global.GAME_WIDTH, Global.GAME_HEIGHT);

        this.addKeyListener(this.keyController = new KeyController(this));

        this.resourceHandler.initialize();

        this.roomManager = new RoomManager();

        this.setScreen(new GameScreen(this, new GameHandler(this)));

        Thread thread = new Thread(this.renderManager = new RenderManager(this));
        thread.start();

        logger.info("Finished loading (took " + ((double)(System.currentTimeMillis() - startTime) / 1000.0) + "s)");
    }

    public void setScreen(AbstractScreen screen) {
        (this.screen = screen).preLoad();
        this.screen.initUI();
    }

    public GameHandler getHandler() {
        return this.screen.getHandler();
    }

    public boolean isDebugging() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
    }
}
