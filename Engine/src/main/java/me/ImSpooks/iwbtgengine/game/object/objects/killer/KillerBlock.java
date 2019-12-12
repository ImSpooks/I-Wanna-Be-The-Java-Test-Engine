package me.ImSpooks.iwbtgengine.game.object.objects.killer;

import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(colisionPriority = ObjectsPriority.LOWEST)
public class KillerBlock extends KillerObject {

    public KillerBlock(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
    }
}
