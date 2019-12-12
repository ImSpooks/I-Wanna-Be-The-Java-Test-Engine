package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.object.player.KidState;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Walljump extends Interactable {
    
    @Getter private final boolean left;

    private int shift = 1;
    public Walljump(Room parent, double x, double y, Sprite sprite, boolean left) {
        super(parent, x, y, sprite);
        
        this.left = left;

        this.x -= shift;
        this.setWidth(this.getWidth() + shift + 1);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        if (this.canRender(camera)) {
            sprite.draw(graphics, camera, this.x + shift, this.y);
        }
    }

    public void onSlide(KidObject kid, GameHandler handler) {
        kid.setVelY(2 * Global.GRAVITY);
    }

    @Override
    public TouchAction onTouch() {
        return kid -> {
            // Everything is 'Vine'

            if (kid != null) {
                kid.setKidState(left ? KidState.SLIDING_LEFT : KidState.SLIDING_RIGHT);

                this.onSlide(kid, this.getParent().getHandler());
                kid.setXScale(left ? -1 : 1);
            }
        };
    }

    @Override
    public Hitbox getUpdatedHitbox() {
        return new Hitbox(this, Hitbox.HitboxType.SQUARE, new Rectangle(0, 0, sprite.getImage().getWidth() + shift +1, sprite.getImage().getHeight())) {
            @Override
            public java.util.List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                if (!left) {
                    for (int x = -shift; x <= sprite.getImage().getWidth() - 18; x++) {
                        for (int y = 0; y <= sprite.getImage().getHeight(); y++) {

                            // only adding outline to reduce lag

                            pixels.add(new int[] {x, y});
                        }
                    }
                }
                else {
                    for (int x = 18; x <= sprite.getImage().getWidth(); x++) {
                        for (int y = 0; y <= sprite.getImage().getHeight(); y++) {

                            // only adding outline to reduce lag

                            pixels.add(new int[] {x, y});
                        }
                    }
                }
                return pixels;
            }
        };
    }
}
