package me.ImSpooks.iwbtgengine.sound;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Sound {

    @Getter private final String name;
    @Getter private final String resource;
    @Getter @Setter private int currentFrame = 0;

    public Sound(String name, String resource) {
        this.name = name;
        this.resource = resource;
    }

    public abstract Sound play();
    public abstract Sound loop(int times);
    public abstract Sound stop();
    public abstract boolean isPaused();
    public abstract Sound pause();
    public abstract Sound resume();
    public abstract Sound setVolume(double volume);
}
