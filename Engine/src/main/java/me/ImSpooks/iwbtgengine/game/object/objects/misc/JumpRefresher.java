package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class JumpRefresher extends Interactable {
    
    @Getter @Setter private int deactivatedTicks = 0;

    public JumpRefresher(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        this.setHitbox(new Hitbox() {
            @Override
            public java.util.List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x < sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y < sprite.getImage().getHeight(); y++) {

                        if ((sprite.getImage().getRGB(x,y) >>24) == 0x00)
                            continue;

                        pixels.add(new int[] {x, y});
                    }
                }

                return pixels;
            }
        });
    }

    @Override
    public TouchAction onTouch() {
        return kid -> {
            if (deactivatedTicks > 0)
                return;

            if (kid.getCanJump() < kid.getMaxJumps() - 1)
                kid.setCanJump(kid.getCanJump() + 1);

            deactivatedTicks = 100;
        };
    }

    @Override
    public boolean update(float delta) {
        if (deactivatedTicks > 0)
            deactivatedTicks--;

        return super.update(delta);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        if (this.canRender(camera)) {

            if (deactivatedTicks > 0) {
                BufferedImage cloneImage = this.getImageUtils().cloneimage(sprite.getImage());
                graphics.drawImage(this.getImageUtils().getColoringUtils().setAlpha(cloneImage, 40), (int) x - camera.getCameraX(), (int) y - camera.getCameraY(), null);
            }
            else {
                sprite.draw(camera, graphics, x, y);
            }
        }
    }
}
