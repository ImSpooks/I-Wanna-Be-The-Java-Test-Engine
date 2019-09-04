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

    public Sound addSound(String name, Sound sound) {
        sounds.put(sound.getName(), sound);
        return sound;
    }

    public void playSound(String name) {
        sounds.get(name).play();
    }

    public void loopSound(String name, int times) {
        sounds.get(name).loop(times);
    }

    public void stopSound(String name) {
        sounds.get(name).stop();
        sounds.remove(name);
    }

    public Sound getSound(String name) {
        return sounds.get(name);
    }
}
