package me.ImSpooks.iwbtgengine.sound;

import org.hackyourlife.gcn.dsp.BRSTMPlayer;

/**
 * Created by Nick on 19 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class BrstmSound extends Sound {

    public BrstmSound(String name, String resource) {
        super(name, resource);
    }

    private BRSTMPlayer player;

    @Override
    public Sound play() {
        if (this.player != null) {
            try {
                this.player.stop();
            } catch (IllegalStateException ignored) {}
        }
        this.player = new BRSTMPlayer(this.getClass().getResourceAsStream(this.getResource()));
        this.player.start();
        return this;
    }

    @Override
    public Sound loop(int times) {
        throw new UnsupportedOperationException("Brstm sounds doesnt support looping.");
    }

    @Override
    public Sound stop() {
        if (this.player != null)
            this.player.stop();
        return this;
    }

    @Override
    public boolean isPaused() {
        return this.player.isPaused();
    }

    @Override
    public Sound pause() {
        this.player.pause();
        return this;
    }

    @Override
    public Sound resume() {
        this.player.resume();
        return this;
    }

    @Override
    public Sound setVolume(double volume) {
        throw new UnsupportedOperationException("Brstm sounds doesnt support changing volume.");
    }
}
