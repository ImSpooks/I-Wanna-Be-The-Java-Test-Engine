package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class AnimatedSprite extends Sprite {

    private BufferedImage renderedImage;

    @Getter private int width, height;
    @Getter @Setter private int frameLength, frameCount;

    public AnimatedSprite(BufferedImage image, int width, int height, int frameCount, int frameLength) {
        super(image);
        this.width = width;
        this.height = height;
        this.frameCount = frameCount;
        this.frameLength = frameLength;

    }

    private int tick;

    @Getter private int renderedFrame;

    private HashMap<Integer, BufferedImage> cache = new HashMap<>();

    public void update(float delta) {
        if (this.getFrameLength() > 0 && this.getFrameCount() > 0) {
            if (renderedFrame >= getFrameCount())
                renderedFrame = 0;

            if (!cache.containsKey(renderedFrame)) {
                cache.put(renderedFrame, ImageUtils.getInstance().getSubImage(this.getOriginalImage(), renderedFrame, 0, width, height));
            }

            renderedImage = cache.get(renderedFrame);

            this.onImageChange();

            if (++tick >= this.getFrameLength()) {
                tick = 0;
                renderedFrame++;
            }

        }
    }

    public void clearCache() {
        this.cache.clear();
    }

    @Override
    public BufferedImage getImage() {
        return this.renderedImage;
    }

    public BufferedImage getOriginalImage() {
        return this.image;
    }

    public void onImageChange() {

    }
}
