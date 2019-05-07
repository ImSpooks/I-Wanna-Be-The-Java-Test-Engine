package me.ImSpooks.iwbtgengine.game.object.player;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.sprite.AnimatedSprite;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        
        spritesMap.put("idle", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_idle", BufferedImage.class), 32, 32, 4, 5));
        spritesMap.put("running", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_running", BufferedImage.class), 32, 32, 4, 2));
        spritesMap.put("fall", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_fall", BufferedImage.class), 32, 32, 2, 2));
        spritesMap.put("jump", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_jump", BufferedImage.class), 32, 32, 2, 2));
        spritesMap.put("sliding", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_sliding", BufferedImage.class), 32, 32, 2, 2));

        return spritesMap;
    }
}
