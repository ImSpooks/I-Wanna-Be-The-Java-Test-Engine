package me.ImSpooks.iwbtgengine.camera;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Camera {
    private float cameraX = 0f;
    private float cameraY = 0f;

    public void update(float newCamX, float newCamY) {
        this.cameraX = newCamX;
        this.cameraY = newCamY;
    }

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }
}
