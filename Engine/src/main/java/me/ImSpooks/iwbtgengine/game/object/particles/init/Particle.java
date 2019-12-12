package me.ImSpooks.iwbtgengine.game.object.particles.init;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.util.image.ImageUtils;

import java.awt.*;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Particle {

    @Getter @Setter protected double x, y, velX, velY, lifespan;

    @Getter private ImageUtils imageUtils = ImageUtils.getInstance();

    public Particle(double x, double y, double velX, double velY, int lifespan) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.lifespan = lifespan;
    }

    public void update(float delta) {
        this.x += this.velX;
        this.y += this.velY;

        lifespan--;
    }

    public void createGraphics(Graphics graphics, Camera camera) {
        if (!this.canRender(camera))
            return;

        Graphics2D g2d = (Graphics2D) graphics.create();

        this.render(g2d, camera);

        g2d.dispose();
    }

    public abstract void render(Graphics graphics, Camera camera);

    public boolean canRender(Camera camera) {
        return camera != null && !(this.x < camera.getCameraX() || this.x > camera.getCameraX() + Global.GAME_WIDTH
                || this.y < camera.getCameraY() || this.y > camera.getCameraY() + Global.GAME_HEIGHT);
    }

    public boolean isAlive() {
        return this.lifespan >= 0;
    }
}
