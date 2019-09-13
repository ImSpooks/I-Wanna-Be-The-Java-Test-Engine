package me.ImSpooks.iwbtgengine.game.object.objects;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.HIGH)
public abstract class Interactable extends GameObject {

    @Getter @Setter private TouchAction onTouch = (kid) -> {};

    public Interactable(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        TouchAction action = this.onTouch();
        if (action != null)
            this.onTouch = this.onTouch();
    }

    public abstract TouchAction onTouch();
}
