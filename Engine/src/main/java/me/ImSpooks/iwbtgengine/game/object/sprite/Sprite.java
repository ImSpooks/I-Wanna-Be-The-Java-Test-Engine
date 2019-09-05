package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Sprite {

    @Getter protected BufferedImage image;

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

    public void draw(Camera camera, Graphics graphics, int x, int y) {
        graphics.drawImage(this.getImage(), x - camera.getCameraX(), y - camera.getCameraY(), null);
    }

    public void draw(Camera camera, Graphics graphics, int x, int y, boolean flipHorizontal, boolean flipVertical) {
        BufferedImage image = this.getImage();

        if (flipHorizontal)
            image = ImageUtils.getInstance().flipImage(image, true);
        if (flipVertical)
            image = ImageUtils.getInstance().flipImage(image, false);

        graphics.drawImage(image, x - camera.getCameraX(), y - camera.getCameraY(), null);
    }
}
