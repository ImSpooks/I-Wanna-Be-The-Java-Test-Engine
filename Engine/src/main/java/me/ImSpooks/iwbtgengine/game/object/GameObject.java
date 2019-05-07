package me.ImSpooks.iwbtgengine.game.object;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.KeyController.KeyListener;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class GameObject {

    @Getter @Setter private Room parent;

    @Getter @Setter protected double x, y;
    @Getter @Setter protected double velX, velY;

    @Getter @Setter protected Sprite sprite;
    @Getter @Setter protected Hitbox hitbox;

    @Getter @Setter private int width, height;

    @Getter private List<KeyListener> keyListener = new ArrayList<>();

    public GameObject(Room parent, double x, double y, Sprite sprite) {
        this.parent = parent;
        this.x = x;
        this.y = y;

        this.sprite = sprite;
    }

    public void removeObject() {
        this.onRemove();
        parent.getMap().getObjects().remove(this);
    }

    public void update(float delta) {
        if (this.sprite != null) {
            this.sprite.update(delta);
        }
    }

    public void render(Graphics graphics) {
        if (this.sprite != null) {
            graphics.drawImage(sprite.getImage(), (int) this.x, (int) this.y, null);
        }
    }

    public void onRemove() {

    }
}