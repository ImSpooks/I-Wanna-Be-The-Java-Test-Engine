package me.ImSpooks.iwbtgengine.game.object.objects.triggers;

import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.LOWEST)
public class Trigger extends Interactable {

    public Trigger(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
        this.setVisible(false);
    }

    @Override
    public void update(float delta) {
        this.x += this.velX;
        this.y += this.velY;
    }

    // Will be handled per room, so we can ignore this here
    @Override
    public TouchAction onTouch() {
        return null;
    }
}