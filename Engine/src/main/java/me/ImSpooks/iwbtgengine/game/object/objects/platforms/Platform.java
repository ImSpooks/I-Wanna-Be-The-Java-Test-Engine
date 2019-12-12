package me.ImSpooks.iwbtgengine.game.object.objects.platforms;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.player.JumpType;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.*;

/**
 * Created by Nick on 16 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Platform extends Interactable {

    public Platform(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
    }

    @Override
    public TouchAction onTouch() {
        return kid -> {
            Rectangle hitbox = kid.getUpdatedHitbox().getRectIfPossible();

            /*double part = 4;
            double add = Global.GRAVITY > 0 ? hitbox.getHeight() / part : hitbox.getHeight() / part * (part - 1);
            double mid = kid.getY() + hitbox.getY() + add;

            mid -= getParent().getHandler().getMain().getScreen().getCamera().getCameraY();*/

            /*if (kid.isTryJumping() && kid.getCanJump() < kid.getMaxJumps()) {
                kid.jump(JumpType.DOUBLE_JUMP);
                kid.setCanJump(kid.getMaxJumps() - 1);
                kid.setTryJumping(false);
            }*/

            if (Global.GRAVITY > 0 ? ((kid.getY() + hitbox.getY() + hitbox.getHeight()) - kid.getVelY()/2 >= y) : ((kid.getY() + hitbox.getY()) - kid.getVelY()/2 <= y + this.getHeight() - 1)) {
                if (Global.GRAVITY > 0
                        ? velY <= 0
                        : velY >= 0) {
                    kid.setY(kid.getY() + this.velY);
                }

                if (kid.isTryJumping() && kid.getCanJump() < kid.getMaxJumps()) {
                    kid.jump(JumpType.DOUBLE_JUMP);
                    kid.setCanJump(kid.getMaxJumps() - 1);
                    kid.setTryJumping(false);
                }
            }

            if (kid.getVelY() < 0 || (Global.GRAVITY < 0 && kid.getVelY() > 0)) {
                if ((Global.GRAVITY > 0 && (kid.getY() + hitbox.getY() + hitbox.getHeight()) + kid.getVelY() <= y)
                    || (Global.GRAVITY < 0 && kid.getY() + hitbox.getY() + hitbox.getHeight() + kid.getVelY() >= y - 1)) {
                    if (Global.GRAVITY > 0) {
                        boolean canJump = true;
                        for (int i = (int) hitbox.getX() + 1; i < hitbox.getX() + hitbox.getWidth(); i++) {
                            int x = (int) kid.getX() + i;

                            for (GameObject gameObject : getParent().getObjectsAt(x, y - 1)) {
                                if (gameObject.isSolid()) {
                                    canJump = false;
                                    break;
                                }
                            }
                        }

                        if (canJump)
                            kid.setY(y - (hitbox.getY() + hitbox.getHeight()) + 1);
                    }
                    else {
                        boolean canJump = true;
                        for (int i = (int) hitbox.getX() + 1; i < hitbox.getX() + hitbox.getWidth(); i++) {
                            int x = (int) kid.getX() + i;

                            for (GameObject gameObject : getParent().getObjectsAt(x, y + getHeight() + 1)) {
                                if (gameObject.isSolid()) {
                                    canJump = false;
                                    break;
                                }
                            }
                        }

                        if (canJump)
                            kid.setY(y + getHeight() + 1);
                    }

                    kid.setVelY(0);

                }
            }
        };
    }
}
