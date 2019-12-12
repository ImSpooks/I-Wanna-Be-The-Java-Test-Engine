package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.helpers.NumberConversions;
import org.hackyourlife.gcn.dsp.BRSTM;
import org.hackyourlife.gcn.dsp.FileFormatException;
import org.hackyourlife.gcn.dsp.input.InputData;
import org.hackyourlife.gcn.dsp.player.BrstmPlayer;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Created by Nick on 19 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class BrstmSound extends Sound {

    @Getter private BrstmPlayer player;

    public BrstmSound(String name, String resource) {
        super(name, resource);

        try {
            this.player = new BrstmPlayer(new BRSTM(InputData.getInputData(this.getClass().getResourceAsStream(resource))));
        } catch (FileFormatException | IOException e) {
            Logger.warn(e);
        }
    }


    @Override
    public BrstmSound play() {
        this.player.start();
        return this;
    }

    @Override
    public BrstmSound loop(int times) {
        throw new UnsupportedOperationException("Brstm audio files cannot be looped manually");
    }

    @Override
    public BrstmSound stop() {
        try {
            this.player.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public boolean isPaused() {
        return this.player.isPaused();
    }

    @Override
    public BrstmSound pause() {
        this.player.pause();
        return this;
    }

    @Override
    public BrstmSound resume() {
        this.player.resume();
        return this;
    }

    @Override
    public BrstmSound setVolume(float volume) {
        this.player.setVolume(NumberConversions.clamp(volume, 0.0F, 1.0F));
        return this;
    }

    @Override
    public void close() {
        try {
            this.player.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Sound clone() {
        return new BrstmSound(this.getName(), this.getResource());
    }
}
