package me.ImSpooks.iwbtgengine.game.object.objects.blocks;

import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.LOW, colisionPriority = ObjectsPriority.HIGHEST)
public class InvisibleBlock extends Block {

    public InvisibleBlock(Room parent, double x, double y, int width, int height) {
        super(parent, x, y, null);

        this.setWidth(width);
        this.setHeight(height);

        if (this.getUpdatedHitbox() == null)
            this.updateHitbox();
    }

    @Override
    public void update(float delta) {
        this.x += this.velX;
        this.y += this.velY;
    }
}
