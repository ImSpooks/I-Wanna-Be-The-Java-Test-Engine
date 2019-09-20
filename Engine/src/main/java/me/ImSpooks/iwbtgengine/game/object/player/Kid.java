package me.ImSpooks.iwbtgengine.game.object.player;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.player.subplayer.Bullet;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFIcon;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFSprite;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Kid extends KidObject {

    private List<Bullet> bullets;
    private int maxBullets = 4;

    public Kid(Room parent, double x, double y, GameHandler handler) {
        super(parent, x, y, handler);
        this.bullets = new ArrayList<>();
    }

    private Sprite deathSprite;

    @Override
    public void render(Camera camera, Graphics graphics) {
        super.render(camera, graphics);

        bullets.forEach(bullet -> bullet.render(camera, graphics));


        if (this.deathSprite != null) {
            int width = this.deathSprite.getImage().getWidth();
            int height = this.deathSprite.getImage().getHeight();

            graphics.drawImage(this.deathSprite.getImage(), (int) ((Global.GAME_WIDTH / 2.0) - (width / 2.0)), (int) ((Global.GAME_HEIGHT / 2.0) - (height / 2.0)), null);
        }
    }

    @Override
    public boolean update(float delta) {
        super.update(delta);

        if (this.deathSprite != null)
            this.deathSprite.update(delta);

        Iterator<Bullet> iterator = this.bullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (bullet.getParent() != Main.getInstance().getHandler().getRoom()
                    || bullet.getX() < 0 || bullet.getX() > bullet.getParent().getMap().getRoomWidth()
                    || bullet.getY() < 0 || bullet.getY() > bullet.getParent().getMap().getRoomHeight()) {
                iterator.remove();
                continue;
            }

            bullet.update(delta);
        }

        //render death screen after 25 frames
        if (this.getTicksDead() > Global.FRAME_RATE / 2 && this.deathSprite == null) {
            this.deathSprite = Sprite.generateSprite(this.getHandler().getMain().getResourceHandler().getResource("sprGameOver"));
        }

        return true;
    }

    @Override
    public void onMove() {

    }

    @Override
    public void onJump(JumpType type) {
        this.getHandler().getSoundManager().playSound("snd" + (type == JumpType.VINE_JUMP || getCanJump() == getMaxJumps() ? "" : "D") + "Jump");
    }

    @Override
    public boolean onDeath() {
        this.getHandler().getSoundManager().playSound("sndDeath");

        this.getHandler().getSoundManager().playSound("deathBgm", "musOnDeath");
        this.getHandler().getSoundManager().pauseSound("bgm");
        return true;
    }

    @Override
    public boolean onReset() {
        this.getHandler().getSoundManager().stopSound("deathBgm");
        this.deathSprite = null;
        return true;
    }

    @Override
    public void onShoot() {
        if (bullets.size() < maxBullets) {
            Bullet bullet = new Bullet(getHandler().getRoom(), this.getX() + 14 + (14 * this.getXScale()), this.getY() + 20, Sprite.generateSprite(getHandler().getMain().getResourceHandler().getResource("sprBullet")), this.getXScale(), this);
            bullets.add(bullet);

            this.getHandler().getSoundManager().playSound("sndShoot");
        }
    }

    @Override
    public Map<String, Sprite> getSpriteMap() {
        Map<String, Sprite> spritesMap = new HashMap<>();

        //spritesMap.put("idle", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_idle", BufferedImage.class), 32, 32, 4, 5));
        spritesMap.put("idle", new GIFSprite(getHandler().getMain().getResourceHandler().get("sprPlayerIdle", GIFIcon.class)));
        spritesMap.put("running", new GIFSprite(getHandler().getMain().getResourceHandler().get("sprPlayerRunning", GIFIcon.class)));
        spritesMap.put("fall", new GIFSprite(getHandler().getMain().getResourceHandler().get("sprPlayerFall", GIFIcon.class)));
        spritesMap.put("jump", new GIFSprite(getHandler().getMain().getResourceHandler().get("sprPlayerJump", GIFIcon.class)));
        spritesMap.put("sliding", new GIFSprite(getHandler().getMain().getResourceHandler().get("sprPlayerSliding", GIFIcon.class)));

        return spritesMap;
    }
}
