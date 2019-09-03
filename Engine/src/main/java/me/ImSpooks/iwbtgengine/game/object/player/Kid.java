package me.ImSpooks.iwbtgengine.game.object.player;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFIcon;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFSprite;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Kid extends KidObject {

    public Kid(Room parent, double x, double y, GameHandler handler) {
        super(parent, x, y, handler);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public Map<String, Sprite> getSpriteMap() {
        Map<String, Sprite> spritesMap = new HashMap<>();

        //spritesMap.put("idle", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_idle", BufferedImage.class), 32, 32, 4, 5));
        spritesMap.put("idle", new GIFSprite(Main.getInstance().getResourceHandler().get("sprPlayerIdle", GIFIcon.class)));
        spritesMap.put("running", new GIFSprite(Main.getInstance().getResourceHandler().get("sprPlayerRunning", GIFIcon.class)));
        spritesMap.put("fall", new GIFSprite(Main.getInstance().getResourceHandler().get("sprPlayerFall", GIFIcon.class)));
        spritesMap.put("jump", new GIFSprite(Main.getInstance().getResourceHandler().get("sprPlayerJump", GIFIcon.class)));
        spritesMap.put("sliding", new GIFSprite(Main.getInstance().getResourceHandler().get("sprPlayerSliding", GIFIcon.class)));

        //TODO running animation is too slow

        return spritesMap;
    }
}
