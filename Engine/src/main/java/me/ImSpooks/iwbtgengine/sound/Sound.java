package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
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
public class Sound {

    //TODO create interface for wav, ogg and mp3 files

    @Getter private final String name;
    @Getter private final String resource;

    private AudioInputStream audioStream;
    @Getter private Clip audioClip;

    public Sound(String name, String resource) {
        this.name = name;
        this.resource = resource;
    }

    public Sound play() {
        long now = System.currentTimeMillis();
        if (this.audioClip != null) {
            this.stop();
        }
        this.audioClip = this.getAudio();

        new Thread(() -> {
            audioClip.start();
            System.out.println(String.format("Took %s milis to play sound '%s'", System.currentTimeMillis() - now, this.name));
        }).start();
        return this;
    }

    public Sound loop(int times) {
        new Thread(() -> {
            audioClip.loop(times);
        }).start();
        return this;
    }

    public Sound stop() {
        if (audioClip.isRunning())
            audioClip.stop();
        return this;
    }

    private boolean paused;
    private int currentFrame;

    public Sound pause() {
        if (paused)
            return this;
        paused = true;
        this.currentFrame = this.audioClip.getFramePosition();
        stop();
        return this;
    }
    public Sound resume() {
        if (!paused)
            return this;
        paused = false;
        this.audioClip.setFramePosition(this.currentFrame);
        play();
        return this;
    }

    public Sound setVolume(double volume) {
        FloatControl gain = (FloatControl) this.audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume) / Math.log(gain.getMaximum()) * 20);
        gain.setValue(dB);
        return this;
    }


    private Clip getAudio() {
        try {
            audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(resource)));

            // load the sound into memory (a Clip)

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);

            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            Main.getInstance().getLogger().log(Level.WARNING, String.format("1 Unable to load resource '%s', thrown exception: '%s'", resource, e.getClass().getSimpleName()));
            e.printStackTrace();
        }
        return null;
    }
}
