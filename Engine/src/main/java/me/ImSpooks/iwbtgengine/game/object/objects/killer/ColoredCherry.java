package me.ImSpooks.iwbtgengine.game.object.objects.killer;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nick on 06 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ColoredCherry extends Cherry {
    
    @Getter @Setter private Color color;

    public ColoredCherry(Room parent, double x, double y, Sprite sprite, Color color) {
        super(parent, x, y, sprite);

        this.color = color;
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        if (color == null) {
            super.render(camera, graphics);
            return;
        }
        if (this.canRender(camera)) {
            BufferedImage image = sprite.getImage();

            float riprobbierotton = 5000;
            Color color = new Color(Color.HSBtoRGB(System.currentTimeMillis() % (long) riprobbierotton / riprobbierotton, 1, 1));

            BufferedImage toDraw = this.getImageUtils().getColoringUtils().addColor(image, color, 0.4f);

            graphics.setColor(Color.GREEN);
            graphics.drawImage(toDraw, (int) x - camera.getCameraX(), (int) y - camera.getCameraY(), null);
            graphics.setColor(Color.CYAN);
        }
    }
}
