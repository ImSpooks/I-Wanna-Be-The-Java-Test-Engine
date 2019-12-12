package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 13 nov. 2019.
 * Copyright © ImSpooks
 */
public class AtlasSprite extends Sprite {

    @Getter private transient List<BufferedImage> renderedImages = new ArrayList<>();

    @Getter private int frameLength = 1;

    public AtlasSprite() {
        super(null);
        this.update(1);
    }

    private int tick;

    @Getter private int renderedFrame;

    private transient float innerDelta = 0;

    public void update(float delta) {
        innerDelta += delta;
        while (innerDelta >= 1) {
            innerDelta--;

            if (this.getFrameLength() > 0 && this.getRenderedImages().size() > 1) {
                int old = renderedFrame;


                this.onImageChange();

                if (++tick >= this.getFrameLength()) {
                    tick = 0;
                    renderedFrame++;
                }

                if (renderedFrame >= this.getRenderedImages().size())
                    renderedFrame = 0;

                if (renderedFrame != old)
                    if (this.getOnUpdate() != null)
                        this.getOnUpdate().onUpdate(delta);
            }
        }
    }

    @Override
    public BufferedImage getImage() {
        return this.renderedImages.get(renderedFrame);
    }

    public BufferedImage getOriginalImage() {
        return this.image;
    }

    public void onImageChange() {

    }

    public AtlasSprite setFrameLength(int frameLength) {
        this.frameLength = frameLength;
        return this;
    }

    public BufferedImage getKeyFrame(int index) {
        return this.renderedImages.get(index % renderedImages.size());
    }
}