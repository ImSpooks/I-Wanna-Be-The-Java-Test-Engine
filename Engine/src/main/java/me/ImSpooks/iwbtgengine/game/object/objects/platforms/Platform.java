package me.ImSpooks.iwbtgengine.game.object.objects.platforms;

import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.player.JumpType;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 16 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Platform extends Interactable {

    public Platform(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        this.setHitbox(new Hitbox(Hitbox.HitboxType.SQUARE) {
            @Override
            public java.util.List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x < sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y < sprite.getImage().getHeight(); y++) {

                        // only adding outline to reduce lag

                        pixels.add(new int[]{x, y});
                    }
                }

                return pixels;
            }
        });
    }

    @Override
    public TouchAction onTouch() {
        return new TouchAction() {
            @Override
            public void run(KidObject kid) {
                Rectangle hitbox = kid.getHitbox().getRectIfPossible();

                double add = hitbox.getHeight() / 3.0 * (Global.GRAVITY < 0 ? 2 : 1);
                double mid = kid.getY() + hitbox.getY() + add;

                if (kid.isTryJumping() && (mid >= y && mid <= y + getHeight())) {
                    kid.jump(JumpType.DOUBLE_JUMP);
                    kid.setCanJump(kid.getMaxJumps() - 1);
                    kid.setTryJumping(false);
                }

                if (kid.getVelY() < 0 || (Global.GRAVITY < 0 && kid.getVelY() > 0)) {
                    if ((Global.GRAVITY > 0 && kid.getY() + hitbox.getY() + hitbox.getHeight() + kid.getVelY() <= y + 1)
                        || (Global.GRAVITY < 0 && kid.getY() + hitbox.getY() + kid.getVelY() >= y - 1)) {

                        if (Global.GRAVITY > 0)
                            kid.setY(y - (hitbox.getY() + hitbox.getHeight()) - 1);
                        else
                            kid.setY(y + 1);

                        kid.setVelY(0);

                    }
                }
            }
        };
    }
}
