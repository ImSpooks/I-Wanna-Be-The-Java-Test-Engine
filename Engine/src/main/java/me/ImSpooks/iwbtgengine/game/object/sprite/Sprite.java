package me.ImSpooks.iwbtgengine.game.object.sprite;

import lombok.Getter;

import java.awt.image.BufferedImage;

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
}
