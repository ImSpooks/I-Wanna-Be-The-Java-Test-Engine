package me.ImSpooks.iwbtgengine.game.object.player;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.KillerObject;
import me.ImSpooks.iwbtgengine.game.object.objects.triggers.Trigger;
import me.ImSpooks.iwbtgengine.game.object.player.subplayer.Blood;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.handler.GameHandler;
import me.ImSpooks.iwbtgengine.keycontroller.KeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Nick on 04 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class KidObject extends GameObject {

    @Getter @Setter private boolean death = false;
    @Getter @Setter private boolean frozen = false;

    @Getter @Setter private int xScale = 1;

    @Getter @Setter private int canJump;

    @Getter @Setter private double gravity = 0.4;
    @Getter @Setter public double jump = 8.1; // use 8.1 instead of 8.5
    @Getter @Setter public double jump2 = 6.6; // use 6.6 instead of 7

    @Getter private Map<String, Sprite> sprites;

    @Getter private final GameHandler handler;

    @Getter private int ticksLived = 0, ticksDead = 0;


    public KidObject(Room parent, double x, double y, GameHandler handler) {
        super(parent, x, y, null);
        this.handler = handler;

        this.sprites = this.getSpriteMap();

        this.setHitbox(new Hitbox(Hitbox.HitboxType.RECTANGLE) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> list = new ArrayList<>();

                Rectangle rectangle = new Rectangle(12, 11, 11, 21);//21

                for (int xx = 0; xx < rectangle.width; xx++) {
                    for (int yy = 0; yy < rectangle.height; yy++) {
                        list.add(new int[] {(int) rectangle.getX() + xx, (int) rectangle.getY() + yy});
                    }
                }

                return list;
            }
        });

        this.setSprite(this.getSprites().get("idle"));

        this.addDefaultKeys();
    }

    @Override
    public void update(float delta) {
        if (this.isDeath()) {
            this.ticksDead++;
            return;
        }
        this.ticksLived++;

        super.update(delta);

        velY = velY + (gravity * Global.GRAVITY);
        if (velY > 9) {
            velY = 9;
        }

        // Collision stuff

        if (velY > 0.4 || velY < 0.4) {
            //System.out.println("y = " + velY);
        }

        if (velY != 0) {
            //floor
            boolean falling = velY * Global.GRAVITY > 0;

            if (Math.abs(velY) < 1) {
                if (floorCollision(falling) || !falling) {
                    this.y += velY;
                }
            }
            else {
                for (double yvel = 0; yvel < Math.ceil(Math.abs(velY)); yvel++) {
                    if (floorCollision(falling) || !falling) {
                        if (falling) {
                            this.y++;
                        }
                        else {
                            this.y--;
                        }
                    }
                }
            }
        }

        if (velX != 0) {
            boolean left = velX < 0;
            for (int i = 0; i < Math.ceil(Math.abs(velX)); i++) {
                if (wallCollision(left ? -1 : 1)) {
                    if (left) {
                        this.x--;
                    }
                    else {
                        this.x++;
                    }
                }
                else {
                    break;
                }
            }

            onMove();
        }

        //wall


        if (velY > 0 && canJump == 2) {
            canJump = 1;
        }

        if (velX == 0 && velY == 0) {
            this.setSprite(this.getSprites().get("idle"));
        }
        else if (velY != 0) {
            if (velY < 0) {
                this.setSprite(this.getSprites().get("jump"));
            }
            else {
                this.setSprite(this.getSprites().get("fall"));
            }
        }
        else {
            this.setSprite(this.getSprites().get("running"));
        }


        velX = 0;
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        sprite.draw(camera, graphics, (int) this.x, (int) this.y, xScale == -1, Global.GRAVITY != 1);

        //this.getHitbox().renderHitbox((int) x, (int) y, graphics);

        /*for (GameObject gameObject : getHandler().getRoom().getObjects()) {
            if (gameObject instanceof Block)
                gameObject.getHitbox().renderHitbox((int)gameObject.getX(), (int)gameObject.getY(), graphics);
        }//*/
    }

    public abstract void onMove();

    public abstract void onJump(JumpType type);

    public abstract void onShoot();

    public abstract boolean onDeath();

    public abstract boolean onReset();

    public abstract Map<String, Sprite> getSpriteMap();

    private void reset() {
        if (this.onReset()) {
            this.ticksDead = 0;

            getHandler().getRoom().reset();
            x = getHandler().getRoom().getMap().getStartX();
            y = getHandler().getRoom().getMap().getStartY();

            this.setDeath(false);
            canJump = 1;
            velY = 0;
        }
    }

    private void kill() {
        if (this.onDeath()) {
            this.ticksLived = 0;
            this.setDeath(true);

            Random random = new Random();
            for (int i = 0; i < 150; i++) {
                Blood blood = new Blood(this.handler.getRoom(), this.x + 16, this.y + 16, new Sprite(this.handler.getMain().getResourceHandler().get("sprBlood" + (random.nextInt(3) + 1))));

                blood.setVelX((random.nextDouble() * 2 - 1) * 8);
                blood.setVelY((random.nextDouble() * 10 - 2) * -1);
                while (Math.sqrt(blood.getVelX() * blood.getVelX() + blood.getVelY() * blood.getVelY()) > 8) {
                    blood.setVelX((random.nextDouble() * 2 - 1) * 8);
                    blood.setVelY((random.nextDouble() * 10 - 2) * -1);
                }

                this.handler.getRoom().addObject(blood);
            }

            this.x = -32;
            this.y = -32;
        }
    }

    private boolean floorCollision(boolean falling) {
        for (int i = 12; i < 12 + 11; i++) {
            int x = (int) this.x + i;

            for (GameObject gameObject : this.handler.getRoom().getObjectsAt(x, (int) this.y + (falling ? 32 : 10))) {
                if (gameObject instanceof Block) {
                    velY = 0;

                    if (falling) {
                        canJump = 2;
                        ((Block) gameObject).getOnTouch().onTouch();
                        return false;
                    }

                    break;
                }
                else if (gameObject instanceof Interactable) {
                    if (gameObject.getHitbox() != null) {
                        if (gameObject.getHitbox().intersects(this.getHitbox(), (int) this.x, (int) this.y, (int) gameObject.getX(), (int) gameObject.getY())) {

                            if (gameObject instanceof KillerObject) {
                                this.kill();
                                break;
                            }

                            if (gameObject instanceof Trigger)
                                ((Trigger) gameObject).getOnTouch().onTouch();
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean wallCollision(int xScale) {
        for (int i = 12; i < 12 + 20; i++) {
            int y = (int) this.y + i;

            for (GameObject gameObject : this.handler.getRoom().getObjectsAt((int) this.x + (xScale == 1 ? 13 + 10 : 10), y)) {
                if (gameObject instanceof Block) {
                    return false;
                }
                else if (gameObject instanceof Interactable) {
                    if (gameObject.getHitbox() != null) {
                        if (gameObject.getHitbox().intersects(this.getHitbox(), (int) this.x, (int) this.y, (int) gameObject.getX(), (int) gameObject.getY())) {

                            if (gameObject instanceof KillerObject) {
                                this.kill();
                                break;
                            }

                            if (gameObject instanceof Trigger)
                                ((Trigger) gameObject).getOnTouch().onTouch();
                        }
                    }
                }
            }
        }
        return true;
    }

    public void addDefaultKeys() {
        // default keys
        this.getKeyListener().add(new KeyListener() {
            private boolean pressedShift = false;
            long lastRelease = 0;

            @Override
            public void onKeyPress(int keycode) {
                if (keycode == KeyEvent.VK_W && getHandler().getMain().isDebugging()) {// warp to your mouse point - only in debug mode
                    Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                    Point appLocation = getHandler().getMain().getWindow().getFrame().getLocationOnScreen();

                    setX(mousePoint.x - appLocation.x - 16 + getHandler().getMain().getScreen().getCamera().getCameraX());
                    setY(mousePoint.y - appLocation.y - 48 + getHandler().getMain().getScreen().getCamera().getCameraY());
                }

                else if (keycode == KeyEvent.VK_R) {
                    reset();
                }

                if (!frozen) {
                    if (getHandler().getMain().isDebugging()) { // change alignment - only in debug mode
                        if (keycode == KeyEvent.VK_A) {
                            velX -= 1;
                        } else if (keycode == KeyEvent.VK_D) {
                            velX += 1;
                        }
                    }

                    /*if (e.isKeyDown(Keybinds.KEY_UP)) {

                    } else if (e.isKeyDown(Keybinds.KEY_DOWN)) {

                    }*/

                    //jump
                    if (keycode == KeyEvent.VK_SHIFT) {
                        long time = System.currentTimeMillis() - lastRelease;

                        // cancel jump
                        if (time >= 20 && time < 40 && canJump > 0) {
                            onJump(JumpType.CANCEL_JUMP);

                            velY = -(canJump-- == 1 ? jump2 : jump) * gravity;
                        }

                        else if (canJump == 2) { //main jump
                            onJump(JumpType.FULL_JUMP);

                            canJump = 1;

                            //SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidJump")).start();
                            velY = -jump;

                            pressedShift = true;
                        } else if (canJump == 1) { //double jump
                            onJump(JumpType.DOUBLE_JUMP);

                            canJump = 0;

                            //SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidDJump")).start();
                            velY = -jump2;

                            pressedShift = true;
                        }
                    }
                    //shoot
                    else if (keycode == KeyEvent.VK_Z) {
                        onShoot();
                    }
                }

                /*else if (keycode == Keybinds.KEY_SKIP) {
                    //TODO skip cutscene if one is executed
                }*/
            }

            @Override
            public void onKeyHold(int keycode) {
                if (keycode != KeyEvent.VK_LEFT && keycode != KeyEvent.VK_RIGHT) {
                    return;
                }

                if (getHandler().getMain().getKeyController().getKeys().containsKey(KeyEvent.VK_LEFT)) {
                    if (getHandler().getMain().getKeyController().getKeys().containsKey(KeyEvent.VK_RIGHT)) {
                        velX = 3;
                        xScale = 1;
                    } else {
                        velX = -3;
                        xScale = -1;
                    }
                } else if (getHandler().getMain().getKeyController().getKeys().containsKey(KeyEvent.VK_RIGHT)) {
                    velX = 3;
                    xScale = 1;
                }
            }

            @Override
            public void onKeyRelease(int keycode) {
                if (keycode == KeyEvent.VK_SHIFT && pressedShift && velY * Global.GRAVITY < 0) {
                    velY *= gravity;
                    pressedShift = false;
                }

                if (keycode == KeyEvent.VK_SHIFT) {
                    lastRelease = System.currentTimeMillis();
                }
            }
        });
    }
}
