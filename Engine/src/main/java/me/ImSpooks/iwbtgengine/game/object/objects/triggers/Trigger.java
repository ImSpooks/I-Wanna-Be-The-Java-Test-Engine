package me.ImSpooks.iwbtgengine.game.object.objects.triggers;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.init.TouchAction;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 04 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.LOWEST)
public class Trigger extends Interactable {

    @Getter @Setter private boolean visible = false;

    public Trigger(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        this.setHitbox(new Hitbox(this, Hitbox.HitboxType.SQUARE, new Rectangle(0, 0, sprite.getImage().getWidth(), sprite.getImage().getHeight())) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x <= sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y <= sprite.getImage().getHeight(); y++) {
                        pixels.add(new int[]{x, y});
                    }
                }

                return pixels;
            }
        });
    }

    @Override
    public boolean update(float delta) {
        if (super.update(delta)) {
            this.x += this.velX;
            this.y += this.velY;

            return true;
        }

        return false;
    }

    // Will be handled per room, so we can ignore this here
    @Override
    public TouchAction onTouch() {
        return null;
    }
}