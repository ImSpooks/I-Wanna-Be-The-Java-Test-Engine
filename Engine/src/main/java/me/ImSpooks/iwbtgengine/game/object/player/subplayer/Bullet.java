package me.ImSpooks.iwbtgengine.game.object.player.subplayer;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.SaveBlocker;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Bullet extends GameObject {

    @Getter private int xScale;
    @Getter private int ticksAlive = 0;

    public Bullet(Room parent, double x, double y, Sprite sprite, int xScale) {
        super(parent, x, y, sprite);
        this.xScale = xScale;
    }

    @Override
    public boolean update(float delta) {
        if (super.update(delta)) {
            this.x += 12 * xScale;

            for (GameObject gameObject : this.getParent().getObjectsAt((int) this.x, (int) this.y)) {
                if (gameObject instanceof Block || gameObject instanceof SaveBlocker) {
                    this.x = -1000;
                    this.y = -1000;
                    break;
                }
            }

            if (ticksAlive++ > Global.FRAME_RATE) {
                this.x = -1000;
                this.y = -1000;
            }
            return true;
        }
        this.x = -1000;
        this.y = -1000;

        return false;
    }
}
