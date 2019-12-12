package me.ImSpooks.iwbtgengine;

import me.ImSpooks.iwbtgengine.global.Global;
import org.tinylog.Logger;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class RenderManager implements Runnable {

    private Main main;

    public RenderManager(Main instance) {
        this.main = instance;
    }

    @Override
    public void run() {
        double tps = Global.FRAME_RATE;
        long lastRender = System.nanoTime();
        double ns = 1000000000.0 / tps;
        double delta = 0;

        long timer = System.currentTimeMillis();
        int frames = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastRender) / ns;
            lastRender = now;
            while (delta >= 1) {
                update((float) delta / 1000.0f);
                render();
                frames++;

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                Logger.debug("{} frames per second ", frames);
                frames = 0;
            }
        }
    }

    private void update(float delta) {
        if (this.main.getScreen() != null && this.main.getScreen().getCamera() != null)
            this.main.getScreen().update(this.main.getScreen().getCamera(), delta);
        this.main.getKeyController().tickKeyboard();
    }

    private void render() {
        this.main.requestFocus();
        BufferStrategy bs = this.main.getBufferStrategy();
        if (bs == null) {
            this.main.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        //g.fillRect(0, 0, Global.GAME_WIDTH, Global.GAME_HEIGHT);

        if (this.main.getScreen() != null && this.main.getScreen().getCamera() != null)
            this.main.getScreen().render(this.main.getScreen().getCamera(), g);

        g.dispose();
        bs.show();
    }
}