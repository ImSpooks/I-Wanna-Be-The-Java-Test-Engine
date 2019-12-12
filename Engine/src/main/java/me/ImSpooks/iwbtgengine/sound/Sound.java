package me.ImSpooks.iwbtgengine.sound;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Sound {

    private final String name;
    private final String resource;

    public Sound(String name, String resource) {
        this.name = name;
        this.resource = resource;
    }

    /**
     * Play the current sound instance
     *
     * @return Current sound instance
     */
    public abstract Sound play();

    /**
     * Loop the audio x amount of times
     * Use {@link javax.sound.sampled.Clip#LOOP_CONTINUOUSLY} for an infinite loop
     *
     * @param times Amount of loop times
     * @return Current sound instance
     */
    public abstract Sound loop(int times);

    /**
     * Stops the current playing audio
     *
     * @return Current sound instance
     */
    public abstract Sound stop();

    /**
     * @return {@code true} if the sound instance is paused, {@code false} otherwise
     */
    public abstract boolean isPaused();

    /**
     * Pause the sound instance
     *
     * @return Current sound instance
     */
    public abstract Sound pause();

    /**
     * Resume the sound instace
     *
     * @return Current sound instance
     */
    public abstract Sound resume();

    /**
     * Change the volume of the sound
     *
     * @param volume New volume between 0-1 (percentage based {@link float})
     */
    public abstract Sound setVolume(float volume);

    /**
     * Close the audio file
     */
    public abstract void close();

    /**
     *
     *
     * @return Cloned sound instance
     */
    public abstract Sound clone();

    /**
     * @return Name of the sound file
     */
    public String getName() {
        return name;
    }

    /**
     * @return Resource path of the sound file
     */
    public String getResource() {
        return resource;
    }
}
