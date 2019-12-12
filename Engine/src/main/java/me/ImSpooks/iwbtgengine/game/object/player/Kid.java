package me.ImSpooks.iwbtgengine.game.object.player;

import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.game.object.player.subplayer.Bullet;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFSprite;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.object.sprite.gif.GifIcon;
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

        try {
            bullets.forEach(bullet -> bullet.render(camera, graphics));
        } catch (ConcurrentModificationException ignored) {}

        if (this.deathSprite != null && getKidState() == KidState.DEAD) {
            int width = this.deathSprite.getImage().getWidth();
            int height = this.deathSprite.getImage().getHeight();

            graphics.drawImage(this.deathSprite.getImage(), (int) ((Global.GAME_WIDTH / 2.0) - (width / 2.0)), (int) ((Global.GAME_HEIGHT / 2.0) - (height / 2.0)), null);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (this.deathSprite != null)
            this.deathSprite.update(delta);

        Iterator<Bullet> iterator = this.bullets.iterator();

        while (iterator.hasNext()) {
            try {
                Bullet bullet = iterator.next();
                if (bullet.getParent() != Main.getInstance().getHandler().getRoom()
                        || bullet.getX() < 0 || bullet.getX() > bullet.getParent().getMap().getRoomWidth()
                        || bullet.getY() < 0 || bullet.getY() > bullet.getParent().getMap().getRoomHeight()) {
                    iterator.remove();
                    continue;
                }

                bullet.update(delta);
            } catch (ConcurrentModificationException ignored) {}
        }

        //render death screen after 25 frames
        if (this.getTicksDead() > Global.FRAME_RATE / 2 && this.deathSprite == null) {
            this.deathSprite = Sprite.generateSprite(this.getHandler().getMain().getResourceManager().getResource("/resources/sprites/kid/default/sprGameOver.png"));
        }
    }

    @Override
    public void onMove() {

    }

    @Override
    public void onJump(JumpType type) {
        this.getHandler().getSoundManager().playAndDestroy("/resources/sounds/sfx/snd" + (type == JumpType.VINE_JUMP || getCanJump() == getMaxJumps() ? "" : "D") + "Jump.wav");
    }

    @Override
    public boolean onDeath() {
        this.getHandler().getSoundManager().playAndDestroy("/resources/sounds/sfx/sndDeath.wav");

        this.getHandler().getSoundManager().playSound("deathBgm", "/resources/sounds/music/musOnDeath.wav");
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
            Bullet bullet = new Bullet(getHandler().getRoom(), this.getX() + 14 + (14 * this.getXScale()), this.getY() + 20, Sprite.generateSprite(getHandler().getMain().getResourceManager().getResource("/resources/sprites/kid/default/sprBullet.gif")), this.getXScale(), this);
            bullets.add(bullet);

            this.getHandler().getSoundManager().playAndDestroy("/resources/sounds/sfx/sndShoot.wav");
        }
    }

    @Override
    public Map<String, Sprite> getSpriteMap() {
        Map<String, Sprite> spritesMap = new HashMap<>();

        //spritesMap.put("idle", new AnimatedSprite(Main.getInstance().getResourceHandler().get("player_default_idle", BufferedImage.class), 32, 32, 4, 5));
        spritesMap.put("idle", new GIFSprite(getHandler().getMain().getResourceManager().get("/resources/sprites/kid/default/sprPlayerIdle.gif", GifIcon.class)));
        spritesMap.put("running", new GIFSprite(getHandler().getMain().getResourceManager().get("/resources/sprites/kid/default/sprPlayerRunning.gif", GifIcon.class)));
        spritesMap.put("fall", new GIFSprite(getHandler().getMain().getResourceManager().get("/resources/sprites/kid/default/sprPlayerFall.gif", GifIcon.class)));
        spritesMap.put("jump", new GIFSprite(getHandler().getMain().getResourceManager().get("/resources/sprites/kid/default/sprPlayerJump.gif", GifIcon.class)));
        spritesMap.put("sliding", new GIFSprite(getHandler().getMain().getResourceManager().get("/resources/sprites/kid/default/sprPlayerSliding.gif", GifIcon.class)));

        return spritesMap;
    }
}
