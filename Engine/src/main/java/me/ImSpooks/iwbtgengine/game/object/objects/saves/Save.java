package me.ImSpooks.iwbtgengine.game.object.objects.saves;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.data.SaveData;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectPriority;
import me.ImSpooks.iwbtgengine.game.object.init.ObjectsPriority;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Difficulty;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;

/**
 * Created by Nick on 13 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@ObjectPriority(renderPriority = ObjectsPriority.HIGHEST)
public class Save extends GameObject {

    @Getter private final Difficulty difficulty;
    @Getter private final boolean flip;

    private boolean canSave;
    
    public Save(Room parent, double x, double y, Sprite sprite, Difficulty difficulty, boolean flip) {
        super(parent, x, y, sprite);
        this.difficulty = difficulty;
        this.flip = flip;
    }

    private int saveTicks = 0;

    @Override
    public boolean update(float delta) {
        if ((Global.GRAVITY > 0 && !flip) || (Global.GRAVITY < 0 && flip)) {
            if (saveTicks-- < 30 && !canSave)
                canSave = true;
        }
        else if (canSave)
            canSave = false;

        return true;
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        if (this.canRender(camera)) {
            int frame = 0;
            if (saveTicks > 0) frame = 1;

            graphics.drawImage(this.getImageUtils().getSubImage(this.getSprite().getImage(), frame, 0, 32, 32), (int) this.x - camera.getCameraX(), (int) this.y - camera.getCameraY(), null);
        }
    }

    public void save(GameHandler handler, KidObject kid) {
        saveTicks = 53;
        canSave = false;

        SaveData data = handler.getSaveData();

        data.setRoomId(this.getParent().getInternalId());
        data.setX((int) kid.getX());
        data.setY((int) kid.getY());
        data.setFlippedGravity(Global.GRAVITY < 0);


        data.save();
    }

    public boolean canSave() {
        return canSave;
    }
}
