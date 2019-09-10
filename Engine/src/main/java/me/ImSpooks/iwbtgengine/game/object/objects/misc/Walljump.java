package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.object.player.KidState;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
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

        this.setHitbox(new Hitbox(Hitbox.HitboxType.SQUARE) {
            @Override
            public java.util.List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                if (!left) {
                    for (int x = -1; x <= sprite.getImage().getWidth() - 18; x++) {
                        for (int y = 0; y <= sprite.getImage().getHeight(); y++) {

                            // only adding outline to reduce lag

                            pixels.add(new int[] {x, y});
                        }
                    }
                }
                else {
                    for (int x = 18; x <= sprite.getImage().getWidth() + 1; x++) {
                        for (int y = 0; y <= sprite.getImage().getHeight(); y++) {

                            // only adding outline to reduce lag

                            pixels.add(new int[] {x, y});
                        }
                    }
                }


                return pixels;
            }
        });

        System.out.println("this.getHitbox() = " + this.getHitbox());
        
        this.setOnTouch(() -> {
            //TODO cant jump on vine without having a vine itself under it

            KidObject kid = this.getParent().getHandler().getKid();

            if (kid != null) {
                kid.setKidState(left ? KidState.SLIDING_LEFT : KidState.SLIDING_RIGHT);

                this.onSlide(kid, this.getParent().getHandler());
                kid.setXScale(left ? -1 : 1);
            }
        });
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        if (this.canRender(camera)) {
            sprite.draw(camera, graphics, this.x + shift, this.y);

            this.getHitbox().renderHitbox(camera, (int) this.x, (int) this.y, graphics);
        }
    }

    public void onSlide(KidObject kid, GameHandler handler) {
        kid.setVelY(2);
    }
}
