package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.RenderPriority;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 11 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = RenderPriority.HIGHEST)
public class Water extends GameObject {
    
    @Getter @Setter private WaterType waterType;

    public Water(Room parent, double x, double y, Sprite sprite, WaterType waterType) {
        super(parent, x, y, sprite);
        this.waterType = waterType;
    }
    
    public enum WaterType {
        FULL_JUMP,
        DOUBLE_JUMP,
        SINGLE_JUMP
        ;
    }
}
