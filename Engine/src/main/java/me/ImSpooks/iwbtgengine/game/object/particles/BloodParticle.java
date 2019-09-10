package me.ImSpooks.iwbtgengine.game.object.particles;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.particles.init.Particle;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class BloodParticle extends Particle {

    private Sprite sprite;
    @Getter private Room room;

    public BloodParticle(double x, double y, double velX, double velY, Room room) {
        super(x, y, velX, velY, Integer.MAX_VALUE);

        BufferedImage image = room.getHandler().getMain().getResourceHandler().get("sprBlood" + (Global.RANDOM.nextInt(3) + 1), BufferedImage.class);
        this.room = room;

        if (Global.RANDOM.nextBoolean())
            image = this.getImageUtils().flipImage(image, true);
        if (Global.RANDOM.nextBoolean())
            image = this.getImageUtils().flipImage(image, false);

        this.sprite = new Sprite(image);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        sprite.draw(camera, graphics, this.x, this.y);
    }

    private boolean onBlock = false;

    @Override
    public void update(float delta) {
        super.update(delta);
        if (this.isAlive()) {

            if (this.velX != 0 || this.velY != 0) {
                if (!onBlock) {
                    this.x += this.velX;
                    this.y += this.velY;

                    this.velX *= 0.99;
                    this.velY += 0.275;

                    /*for (GameObject gameObject : room.getObjectsAt((int) this.x, (int) this.y)) {
                        if (gameObject instanceof Block) {
                            this.velX = 0;
                            this.velY = 0;

                            this.onBlock = true;
                            break;
                        }
                    }*/
                }
            }
        }
    }
}
