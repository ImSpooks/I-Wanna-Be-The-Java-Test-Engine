package me.ImSpooks.iwbtgengine.game.object.sprite.gif;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 10 dec. 2019.
 * Copyright © ImSpooks
 */
public class GifIcon {

    /**
     * List of frames
     */
    @Getter @Setter private List<BufferedImage> frames;

    /**
     * List of delays
     */
    @Getter @Setter private List<Integer> delays;

    public GifIcon(List<BufferedImage> frames, List<Integer> delay) {
        this.frames = frames;
        this.delays = delay;
    }

    public GifIcon() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public BufferedImage getFrame(int frame) {
        return this.frames.get(frame);
    }

    public Integer getDelay(int frame) {
        return this.delays.get(frame);
    }
}