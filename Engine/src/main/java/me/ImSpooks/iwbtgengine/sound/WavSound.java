package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class WavSound extends Sound{

    private AudioInputStream audioStream;
    @Getter private Clip audioClip;

    @Getter @Setter private int currentFrame = 0;

    public WavSound(String name, String resource) {
        super(name, resource);
    }

    public WavSound play() {
        long now = System.currentTimeMillis();
        if (this.audioClip != null) {
            this.stop();
        }
        else
            this.audioClip = this.getAudio();

        new Thread(() -> {
            audioClip.setFramePosition(this.currentFrame = 0);
            audioClip.start();

            long delay = System.currentTimeMillis() - now;
            if (delay > 20)
                Main.getInstance().getLogger().log(Level.WARNING, String.format("Took %s milis to play sound '%s'", delay, this.getName()));
        }).start();
        return this;
    }

    public WavSound loop(int times) {
        new Thread(() -> {
            audioClip.loop(times);
        }).start();
        return this;
    }

    public WavSound stop() {
        if (audioClip.isRunning())
            audioClip.stop();
        return this;
    }

    public boolean isPaused() {
        return paused;
    }

    private boolean paused;

    public WavSound pause() {
        if (paused)
            return this;
        paused = true;
        this.currentFrame = this.audioClip.getFramePosition();
        stop();
        return this;
    }
    public WavSound resume() {
        if (!paused)
            return this;

        long now = System.currentTimeMillis();

        paused = false;
        this.audioClip.setFramePosition(this.currentFrame);

        new Thread(() -> {
            audioClip.start();

            long delay = System.currentTimeMillis() - now;
            if (delay > 20)
                Main.getInstance().getLogger().log(Level.WARNING, String.format("Took %s milis to resume sound '%s'", delay, this.getName()));
        }).start();

        return this;
    }

    public WavSound setVolume(double volume) {
        FloatControl gain = (FloatControl) this.audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume) / Math.log(gain.getMaximum()) * 20);
        gain.setValue(dB);
        return this;
    }


    private Clip getAudio() {
        try {
            if (audioStream == null)
                audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(this.getResource())));

            // load the sound into memory (a Clip)

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(this.currentFrame);

            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            Main.getInstance().getLogger().log(Level.WARNING, String.format("1 Unable to load resource '%s', thrown exception: '%s'", this.getResource(), e.getClass().getSimpleName()));
            e.printStackTrace();
        }
        return null;
    }
}
