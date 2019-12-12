package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
import lombok.Setter;
import org.tinylog.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class WavSound extends Sound {

    @Getter private Clip clip;
    @Getter @Setter private int currentFrame = 0;
    @Getter private boolean paused;

    public WavSound(String name, String resource) {
        super(name, resource);
    }

    @Override
    public WavSound play() {
        long now = System.currentTimeMillis();
        if (this.clip != null) {
            this.stop();
        }
        else
            this.clip = this.getAudio();

        assert clip != null;
        clip.setFramePosition(this.currentFrame = 0);
        new Thread(() -> {
            clip.start();

            long delay = System.currentTimeMillis() - now;
            if (delay > 20)
                Logger.warn("Took {} milis to play sound '{}'", delay, this.getName());
        }).start();
        return this;
    }

    @Override
    public WavSound loop(int times) {
        new Thread(() -> clip.loop(times)).start();
        return this;
    }

    @Override
    public WavSound stop() {
        if (clip.isRunning())
            clip.stop();
        return this;
    }

    @Override
    public WavSound pause() {
        if (paused)
            return this;
        paused = true;
        this.currentFrame = this.clip.getFramePosition();
        stop();
        return this;
    }

    @Override
    public WavSound resume() {
        if (!paused)
            return this;

        long now = System.currentTimeMillis();

        paused = false;
        this.clip.setFramePosition(this.currentFrame);

        new Thread(() -> {
            clip.start();

            long delay = System.currentTimeMillis() - now;
            if (delay > 20)
                Logger.warn("Took {} milis to resume sound '{}'", delay, this.getName());
        }).start();

        return this;
    }

    @Override
    public WavSound setVolume(float volume) {
        FloatControl gain = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume) / Math.log(gain.getMaximum()) * 20);
        gain.setValue(dB);
        return this;
    }

    @Override
    public void close() {
        this.stop();
        this.clip.close();
    }

    @Override
    public Sound clone() {
        return new WavSound(this.getName(), this.getResource());
    }

    private Clip getAudio() {
        Clip clip = null;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(this.getResource())));

            // load the sound into memory (a Clip)

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            Logger.warn(e, "Unable to load resource \"{}\"", this.getResource());
            if (clip != null) clip.close();
        }
        return null;
    }
}
