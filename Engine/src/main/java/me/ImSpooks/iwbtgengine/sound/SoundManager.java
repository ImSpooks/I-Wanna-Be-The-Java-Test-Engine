package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.Main;

import java.util.HashMap;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class SoundManager {

    private final Main instance;

    @Getter private final HashMap<String, Sound> sounds;

    public SoundManager(Main instance) {
        this.instance = instance;
        this.sounds = new HashMap<>();
    }

    public void playSound(Sound sound) {
        sound.play();
    }

    public Sound playSound(String name, Sound sound) {
        stopSound(name);

        sounds.put(name, sound);
        return sound.play();
    }

    public Sound reloadSound(String name, String soundFile) {
        Sound currentBgm = this.getSound(name);

        if (!this.isPlaying(name, soundFile)) {
            if (currentBgm != null && currentBgm.getName().equalsIgnoreCase(soundFile))
                return currentBgm.resume();
            else
                return this.playSound(name, soundFile);
        }

        return currentBgm;
    }

    public Sound playSound(String name, String soundFile) {
        stopSound(name);

        Sound sound = this.instance.getResourceHandler().get(soundFile, Sound.class);
        sounds.put(name, sound);
        return sound.play();
    }

    public Sound addSound(String name, Sound sound) {
        sounds.put(sound.getName(), sound);
        return sound;
    }

    public Sound playSound(String name) {
        stopSound(name);

        sounds.put(name, this.instance.getResourceHandler().get(name, Sound.class));
        return sounds.get(name).play();
    }

    public void loopSound(String name, int times) {
        sounds.get(name).loop(times);
    }

    public boolean isPlaying(String name) {
        return sounds.containsKey(name) && !sounds.get(name).isPaused();
    }

    public boolean isPlaying(String name, String file) {
        return sounds.containsKey(name) && sounds.get(name).getName().equalsIgnoreCase(file) && !sounds.get(name).isPaused();
    }

    public void stopSound(String name) {
        if (sounds.containsKey(name))
            sounds.get(name).stop();
        sounds.remove(name);
    }

    public void pauseSound(String name) {
        Sound sound = this.getSound(name);
        if (sound != null)
            sound.pause();
    }

    public void resumeSound(String name) {
        Sound sound = this.getSound(name);
        if (sound != null)
            sound.resume();
    }

    public Sound getSound(String name) {
        return sounds.get(name);
    }
}
