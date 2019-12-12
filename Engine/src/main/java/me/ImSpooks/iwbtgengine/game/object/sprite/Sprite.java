package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.sprite.gif.GifIcon;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.ResourceManager;
import me.ImSpooks.iwbtgengine.util.image.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public static Sprite generateSprite(ResourceManager.CachedResource<?> cachedResource) {
        if (cachedResource == null) {
            throw new NullPointerException("Cached image is null");
        }
        switch (cachedResource.getObject().getClass().getSimpleName()) {
            case "BufferedImage":    return new Sprite((BufferedImage) cachedResource.getObject());
            case "GifIcon":          return new GIFSprite((GifIcon) cachedResource.getObject());
        }
        return null;
    }

    /**
     * Draws image
     *
     * @param graphics Game graphics
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Render width
     * @param height Render height
     * @param flipHorizontal Horizontal flip
     * @param flipVertical Vertical flip
     */
    public void draw(Graphics graphics, double x, double y, double width, double height, boolean flipHorizontal, boolean flipVertical) {
        BufferedImage image = this.getImage();

        if (flipHorizontal)
            image = imageUtils.flipImageHorizontally(image);
        if (flipVertical)
            image = imageUtils.flipImageVertically(image);

        graphics.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, double x, double y, double width, double height) {
        this.draw(graphics, x, y, width, height, false, false);
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, double x, double y) {
        this.draw(graphics, x, y, this.getImage().getWidth(), this.getImage().getHeight());
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, double x, double y, boolean flipHorizontal, boolean flipVertical) {
        this.draw(graphics, x, y, this.getImage().getWidth(), this.getImage().getHeight(), flipHorizontal, flipVertical);
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, Camera camera, double x, double y, double width, double height, boolean flipHorizontal, boolean flipVertical) {
        this.draw(graphics,x - camera.getCameraX(), y - camera.getCameraY(), width, height, flipHorizontal, flipVertical);
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, Camera camera, double x, double y, boolean flipHorizontal, boolean flipVertical) {
        this.draw(graphics, camera, x, y, this.getImage().getWidth(), this.getImage().getHeight(), flipHorizontal, flipVertical);
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, Camera camera, double x, double y, double width, double height) {
        this.draw(graphics, camera, x, y, width, height, false, false);
    }

    /**
     * @see Sprite#draw(Graphics, double, double, double, double, boolean, boolean)
     */
    public void draw(Graphics graphics, Camera camera, double x, double y) {
        this.draw(graphics, camera, x, y, this.getImage().getWidth(), this.getImage().getHeight());
    }

    public void setOnUpdate(SpriteUpdate onUpdate) {
        this.onUpdate = onUpdate;
        this.onUpdate.onUpdate(Global.FRAME_RATE / 1000.0f);
    }
}
