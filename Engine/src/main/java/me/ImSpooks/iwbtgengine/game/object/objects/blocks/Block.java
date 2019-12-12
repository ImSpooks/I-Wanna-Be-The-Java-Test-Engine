package me.ImSpooks.iwbtgengine.game.object.objects.blocks;

import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.LOW, colisionPriority = ObjectsPriority.HIGHEST)
public class Block extends Interactable {

    public Block(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
    }

    @Override
    public TouchAction onTouch() {
        return null;
    }

    @Override
    public void update(float delta) {
        this.x += this.velX;
        this.y += this.velY;
    }


    @Override
    public boolean isSolid() {
        return true;
    }
}
