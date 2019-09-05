package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.image.BufferedImage;

/**
 * Created by Nick on 02 Sep 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class GIFSprite extends Sprite {

    @Getter private final GIFIcon icon;

    public GIFSprite(GIFIcon icon) {
        super(null);
        this.icon = icon;
    }


    @Getter @Setter
    private int frameLength;

    @Getter private int renderedFrame;
    private int tick;

    @Override
    public void update(float delta) {
        if (++tick >= this.getIcon().getDuration(this.renderedFrame) / (100.0 / Global.FRAME_RATE)) {
            tick = 0;
            renderedFrame++;
        }

        if (renderedFrame >= this.icon.getFrameCount()) {
            this.renderedFrame = 0;
        }
    }

    @Override
    public BufferedImage getImage() {
        return (BufferedImage) this.icon.getImage(renderedFrame);
    }
}
