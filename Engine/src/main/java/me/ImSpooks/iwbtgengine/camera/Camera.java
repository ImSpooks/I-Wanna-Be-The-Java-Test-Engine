package me.ImSpooks.iwbtgengine.camera;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Camera {
    private float cameraX = 0f, cameraY = 0f;

    private float toX = 0f, toY = 0f;

    @Getter @Setter private boolean smoothTransition = false;

    public void update(float delta) {
        if (smoothTransition) {
            if (Math.abs(this.toX - this.cameraX) < 1) this.cameraX = this.toX;
            else this.cameraX += (this.toX - this.cameraX) * .125;

            if (Math.abs(this.toY - this.cameraY) < 1) this.cameraY = this.toY;
            else this.cameraY += (this.toY - this.cameraY) * .125;
        }
        else {
            this.cameraX = this.toX;
            this.cameraY = this.toY;
        }
    }

    public void setCameraPosition(float newCamX, float newCamY) {
        this.toX = newCamX;
        this.toY = newCamY;
    }

    public int getCameraX() {
        return Math.round(cameraX);
    }

    public int getCameraY() {
        return Math.round(cameraY);
    }

    public void setCameraX(float cameraX) {
        this.toX = cameraX;
    }

    public void setCameraY(float cameraY) {
        this.toY = cameraY;
    }
}
