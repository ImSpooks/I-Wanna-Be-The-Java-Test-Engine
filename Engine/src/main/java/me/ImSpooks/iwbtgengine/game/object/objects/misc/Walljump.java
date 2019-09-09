package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Walljump extends Interactable {
    
    @Getter private boolean left;
    
    public Walljump(Room parent, double x, double y, Sprite sprite, boolean left) {
        super(parent, x, y, sprite);
        
        this.left = left;
        
        this.x -= 1;
        this.setWidth(this.getWidth() + 1);
        
        this.setOnTouch(() -> {
            //TODO vine mechanics
        });
    }
}
