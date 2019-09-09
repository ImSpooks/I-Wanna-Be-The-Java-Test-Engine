package me.ImSpooks.iwbtgengine.game.object.objects.killer;

import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class KillerObject extends Interactable {

    public KillerObject(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
    }

    @Override
    public boolean update(float delta) {
        if (super.update(delta)) {
            this.x += this.velX;
            this.y += this.velY;

            return true;
        }

        return false;
    }
}
