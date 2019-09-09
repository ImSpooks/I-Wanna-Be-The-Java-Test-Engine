package me.ImSpooks.iwbtgengine.screen;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class TitleScreen extends AbstractScreen {

    public TitleScreen(Main game, GameHandler handler) {
        super(game, handler);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {

        this.getHandler().render(camera, graphics);
    }

    @Override
    public void update(Camera camera, float delta) {
        this.getHandler().update(camera, delta);
    }

    @Override
    public void preLoad() {
        this.setCamera(new Camera());
    }

    @Override
    public void initUI() {

    }
}
