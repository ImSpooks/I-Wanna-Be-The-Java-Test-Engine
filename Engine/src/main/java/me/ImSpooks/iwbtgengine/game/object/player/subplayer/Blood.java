package me.ImSpooks.iwbtgengine.game.object.player.subplayer;

import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.*;
import java.util.Random;

/**
 * Created by Nick on 06 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Blood extends GameObject {

    private boolean flipX, flipY;

    public Blood(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        Random random = new Random();

        this.flipX = random.nextBoolean();
        this.flipY = random.nextBoolean();
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        if (this.sprite != null) {
            // Make sure to render objects only in field of view of the camera, to avoid rendering objects off screen to reduce lag
            if (!(this.x + this.getWidth() < camera.getCameraX() || this.x > camera.getCameraX() + Global.GAME_WIDTH
                    || this.y + this.getHeight() < camera.getCameraY() || this.y > camera.getCameraY() + Global.GAME_HEIGHT)) {
                sprite.draw(camera, graphics, (int) this.x, (int) this.y, this.flipX, this.flipY);

                //if (this.getHitbox() != null) this.getHitbox().renderHitbox(camera, (int) this.x, (int) this.y, graphics);
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        this.x += this.velX;
        this.y += this.velY;

        if (this.velX != 0 || this.velY != 0) {
            for (GameObject gameObject : this.getParent().getObjectsAt((int) this.x, (int) this.y)) {
                if (gameObject instanceof Block) {
                    this.velX = 0;
                    this.velY = 0;
                    return;
                }
            }

            this.velX *= 0.99;
            this.velY += 0.275;
        }
    }
}
