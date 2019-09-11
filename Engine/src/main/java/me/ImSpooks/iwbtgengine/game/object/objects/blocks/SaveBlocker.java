package me.ImSpooks.iwbtgengine.game.object.objects.blocks;

import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */

public class SaveBlocker extends GameObject {

    public SaveBlocker(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
    }
}
