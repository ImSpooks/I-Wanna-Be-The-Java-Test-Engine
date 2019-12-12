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
            //if (main.running) {
                long now = System.nanoTime();
                delta += (now - lastRender) / ns;
                lastRender = now;
                while(delta >= 1) {
                    update((float)delta / 1000.0f);
                    render();

                    delta = 0;
                    frames++;
                }

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    Logger.debug("{} frames per second ", frames);
                    frames = 0;

                    /*if (SaveSelection.selected != null) {
                        SaveFile file = SaveSelection.selected;

                        file.setTime(file.getTime() + 1);

                        long time = file.getTime();
                        long lhours = time / 3600;
                        long lminutes = (time % 3600) / 60;
                        long lseconds = time % 60;

                        String hours = String.valueOf(lhours).length() == 1 ? "0" + lhours : String.valueOf(lhours);
                        String minutes = String.valueOf(lminutes).length() == 1 ? "0" + lminutes : String.valueOf(lminutes);
                        String seconds = String.valueOf(lseconds).length() == 1 ? "0" + lseconds : String.valueOf(lseconds);

                        Main.getInstance().frame.setTitle(Main.getInstance().title + " - Save " + (Integer.parseInt(file.getFile().getFileName().substring(file.getFile().getFileName().length() - 1)) + 1) + " - Deaths: " + file.getDeath() + "  Time: " + hours + ":" + minutes + ":" + seconds);
                    }*/
                }
            //}
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
        if (bs == null){
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