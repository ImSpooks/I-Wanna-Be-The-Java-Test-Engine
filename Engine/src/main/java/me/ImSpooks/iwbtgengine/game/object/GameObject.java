package me.ImSpooks.iwbtgengine.game.object;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.sprite.EmptySprite;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.keycontroller.KeyListener;
import me.ImSpooks.iwbtgengine.util.image.ImageUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority
public abstract class GameObject {

    @Getter @Setter private Room parent;

    @Getter @Setter protected double x, y;
    @Getter @Setter protected double velX, velY;

    @Getter @Setter protected Sprite sprite;
    @Getter @Setter protected Hitbox hitbox;

    @Getter @Setter private int width, height;

    @Getter private List<KeyListener> keyListener = new ArrayList<>();

    @Getter @Setter private String customId = "";

    @Getter private ImageUtils imageUtils = ImageUtils.getInstance();

    @Getter @Setter private boolean visible = true;

    public GameObject(Room parent, double x, double y, Sprite sprite) {
        this.parent = parent;
        this.x = x;
        this.y = y;

        this.sprite = sprite;

        if (this.sprite != null) {
            this.width = sprite.getImage().getWidth();
            this.height = sprite.getImage().getHeight();

            this.updateHitbox();
        }
        else {
            this.sprite = new EmptySprite();
        }
    }

    public void removeObject() {
        this.onRemove();
        parent.getMap().getObjects().remove(this);
    }

    public void update(float delta) {
        this.sprite.update(delta);
    }

    public void render(Camera camera, Graphics graphics) {
        if (this.canRender(camera)) {
            sprite.draw(graphics, camera, this.x, this.y);

            //if (this.getHitbox() != null) this.getHitbox().renderHitbox(camera, (int) this.x, (int) this.y, graphics);
        }
    }

    public boolean canRender(Camera camera) {
        return camera != null && this.sprite != null && !(this.x + this.getWidth() < camera.getCameraX() || this.x > camera.getCameraX() + Global.GAME_WIDTH
                    || this.y + this.getHeight() < camera.getCameraY() || this.y > camera.getCameraY() + Global.GAME_HEIGHT);
    }

    public void onRemove() {}

    public void setPosition(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public boolean isSolid() {
        return false;
    }

    public void updateHitbox() {
        this.hitbox = this.getUpdatedHitbox();
    }

    public Hitbox getUpdatedHitbox() {
        return new Hitbox(this, Hitbox.HitboxType.SQUARE, new Rectangle(0, 0, sprite.getImage().getWidth(), sprite.getImage().getHeight())) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x < sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y < sprite.getImage().getHeight(); y++) {

                        // only adding outline to reduce lag

                        pixels.add(new int[]{x, y});
                    }
                }

                return pixels;
            }
        };
    }

    @Override
    public String toString() {
        return String.format("%s[x: %s, y: %s]", this.getClass().getSimpleName(), x, y);
    }
}