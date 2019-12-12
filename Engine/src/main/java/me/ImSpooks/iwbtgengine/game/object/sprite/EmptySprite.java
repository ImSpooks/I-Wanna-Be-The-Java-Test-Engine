package me.ImSpooks.iwbtgengine.game.object.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class EmptySprite extends Sprite {

    public EmptySprite() {
        super(null);
        this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public BufferedImage getImage() {
        return super.getImage();
    }

    @Override
    public void draw(Graphics graphics, double x, double y, double width, double height, boolean flipHorizontal, boolean flipVertical) {
        //throw new UnsupportedOperationException("Cannot render empty sprite.");
    }
}
