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
        super((BufferedImage) icon.getImage());
        this.icon = icon;
    }


    @Getter @Setter
    private int frameLength;

    @Getter private int renderedFrame;
    private int tick;

    @Override
    public void update(float delta) {
        int old = renderedFrame;

        if (++tick >= this.getIcon().getDuration(this.renderedFrame) / (100.0 / Global.FRAME_RATE)) {
            tick = 0;
            renderedFrame++;
        }

        if (renderedFrame >= this.icon.getFrameCount()) {
            this.renderedFrame = 0;
        }

        if (renderedFrame != old)
            if (this.getOnUpdate() != null)
                this.getOnUpdate().onUpdate(delta);
    }

    @Override
    public BufferedImage getImage() {
        return (BufferedImage) this.icon.getImage(renderedFrame);
    }

    @Override
    public void setImage(BufferedImage image) {
        super.setImage(image);
        this.icon.setImage(this.getImage());
    }

    public BufferedImage getOriginalImage() {
        return this.image;
    }
}
