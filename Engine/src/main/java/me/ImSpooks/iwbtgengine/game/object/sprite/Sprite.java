package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.util.image.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Sprite {

    @Getter @Setter protected BufferedImage image;
    @Getter private SpriteUpdate onUpdate;

    @Getter private ImageUtils imageUtils = ImageUtils.getInstance();

    public Sprite(BufferedImage image) {
        this.image = image;
    }

    public void update(float delta) {

    }

    public static Sprite generateSprite(List<Object> value) {
        switch (((Class) value.get(1)).getSimpleName()) {
            case "BufferedImage": return new Sprite((BufferedImage) value.get(0));
            case "GIFIcon":   return new GIFSprite((GIFIcon) value.get(0));
            default:    return null;
        }
    }

    public void draw(Camera camera, Graphics graphics, double x, double y) {
        this.draw(camera, graphics, x, y, false, false);
    }

    public void draw(Camera camera, Graphics graphics, double x, double y, boolean flipHorizontal, boolean flipVertical) {
        BufferedImage image = this.getImage();

        if (flipHorizontal)
            image = this.imageUtils.flipImage(image, true);
        if (flipVertical)
            image = this.imageUtils.flipImage(image, false);

        graphics.drawImage(image, (int) x - camera.getCameraX(), (int) y - camera.getCameraY(), null);
    }

    public void setOnUpdate(SpriteUpdate onUpdate) {
        this.onUpdate = onUpdate;
        this.onUpdate.onUpdate(Global.FRAME_RATE / 1000.0f);
    }
}
