package me.ImSpooks.iwbtgengine.game.object.objects;

import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class Interactable extends GameObject {

    public Interactable(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
    }
}