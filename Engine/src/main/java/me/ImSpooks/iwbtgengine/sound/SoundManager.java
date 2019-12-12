package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.handler.ResourceManager;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class SoundManager {

    @Getter private final ResourceManager resourceManager;
    @Getter private final HashMap<String, Sound> sounds;

    /**
     * Creates the sound manager instance
     *
     * @param resourceManager Resource manager
     */
    public SoundManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.sounds = new HashMap<>();
    }

    /**
     * Play a sound file
     *
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     * @param soundFile File name in the resource manager
     * @return The sound instance
     */
    public Sound playSound(String name, String soundFile) {
        Sound currentBgm = this.getSound(name);

        if (!this.isPlaying(name, soundFile)) {
            if (currentBgm != null && currentBgm.getResource().equalsIgnoreCase(soundFile))
                return currentBgm.resume();
            else {
                stopSound(name);

                Sound sound = this.resourceManager.get(soundFile, Sound.class);
                sounds.put(name, sound);
                return sound.play();
            }
        }
        return currentBgm;
    }
    /**
     * @param soundFile File name in the resource manager
     * @see SoundManager#playAndDestroy(Sound)
     */
    public void playAndDestroy(String soundFile) {
        this.playAndDestroy(resourceManager.get(soundFile, Sound.class));
    }

    /**
     * Play a sound file and destroy it right afterwards
     *
     * @param sound Sound instance
     */
    public void playAndDestroy(Sound sound) {
        sound = sound.clone();
        try {
            sound.play();

            Field field = sound.getClass().getDeclaredField("clip");

            if (!field.isAccessible()) field.setAccessible(true);

            Clip clip = (Clip) field.get(sound);
            Sound finalSound = sound;
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    finalSound.close();
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }
    }

    /**
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     * @return {@code true} if the sound exists and is playing, {@code false} otherwise
     */
    public boolean isPlaying(String name) {
        return sounds.containsKey(name) && !sounds.get(name).isPaused();
    }
    /**
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     * @param soundFile File name in the resource manager
     * @return {@code true} if the sound exists and is playing, {@code false} otherwise
     */
    public boolean isPlaying(String name, String soundFile) {
        return sounds.containsKey(name) && sounds.get(name).getResource().equalsIgnoreCase(soundFile) && !sounds.get(name).isPaused();
    }

    /**
     * Stop a sound
     *
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     */
    public void stopSound(String name) {
        if (sounds.containsKey(name))
            sounds.get(name).stop();
        sounds.remove(name);
    }

    /**
     * Pause a sound
     *
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     */
    public void pauseSound(String name) {
        Sound sound = this.getSound(name);
        if (sound != null)
            sound.pause();
    }

    /**
     * Resume a sound
     *
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     */
    public void resumeSound(String name) {
        Sound sound = this.getSound(name);
        if (sound != null)
            sound.resume();
    }

    /**
     * @param name Name of global sound, e.g. {@code "bgm"} as background music
     * @return Sound instance with specified global name
     */
    public Sound getSound(String name) {
        return sounds.get(name);
    }
}
