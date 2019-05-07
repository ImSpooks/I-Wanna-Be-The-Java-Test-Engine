package me.ImSpooks.iwbtgengine.screen;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.event.EventHandler;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class TitleScreen extends AbstractScreen {

    @Getter private final EventHandler eventHandler;

    public TitleScreen(Main game, GameHandler handler) {
        super(game, handler);

        this.eventHandler = new EventHandler();
    }

    @Override
    public void render(Graphics graphics) {
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
