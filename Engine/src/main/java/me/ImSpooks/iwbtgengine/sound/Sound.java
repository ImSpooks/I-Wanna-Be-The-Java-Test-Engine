package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Sound {

    @Getter private final String name;
    @Getter private final Clip audioClip;

    public Sound(String name, Clip audioClip) {
        this.name = name;
        this.audioClip = audioClip;
    }

    public void play() {
        new Thread(audioClip::start).start();
    }

    public void loop(int times) {
        new Thread(() -> {
            audioClip.loop(times);
        }).start();
    }

    public void stop() {
        if (audioClip.isRunning())
            audioClip.stop();
    }

    private boolean paused;
    private int currentFrame;

    public void pause() {
        if (paused)
            return;
        paused = true;
        this.currentFrame = this.audioClip.getFramePosition();
        stop();
    }
    public void resume() {
        if (!paused)
            return;
        paused = false;
        this.audioClip.setFramePosition(this.currentFrame);
        play();
    }

    public Sound setVolume(double volume) {
        FloatControl gain = (FloatControl) this.audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume) / Math.log(gain.getMaximum()) * 20);
        gain.setValue(dB);
        return this;
    }
}
