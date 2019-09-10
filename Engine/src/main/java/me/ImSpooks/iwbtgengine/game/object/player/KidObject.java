package me.ImSpooks.iwbtgengine.game.object.player;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.data.SaveData;
import me.ImSpooks.iwbtgengine.event.Event;
import me.ImSpooks.iwbtgengine.event.events.CutsceneEvent;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.objects.Interactable;
import me.ImSpooks.iwbtgengine.game.object.objects.blocks.Block;
import me.ImSpooks.iwbtgengine.game.object.objects.killer.KillerObject;
import me.ImSpooks.iwbtgengine.game.object.particles.BloodParticle;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.EventRoom;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.global.Keybinds;
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

    @Getter @Setter private boolean frozen = false;

    @Getter @Setter private int xScale = 1;

    @Getter @Setter private int maxJumps = 2;
    @Getter @Setter private int canJump;

    @Getter @Setter private double gravity = 0.4;
    @Getter @Setter public double jump = 8.1; // use 8.1 instead of 8.5
    @Getter @Setter public double jump2 = 6.6; // use 6.6 instead of 7

    @Getter private Map<String, Sprite> sprites;

    @Getter private final GameHandler handler;

    @Getter private int ticksLived = 0, ticksDead = 0;

    @Getter private KeyListener kidController;

    @Getter @Setter private KidState kidState;

    public KidObject(Room parent, double x, double y, GameHandler handler) {
        super(parent, x, y, null);
        this.handler = handler;

        this.sprites = this.getSpriteMap();

        this.setHitbox(new Hitbox(Hitbox.HitboxType.RECTANGLE) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> list = new ArrayList<>();

                Rectangle rectangle = new Rectangle(12, 11, 11, 21);//21

                for (int xx = 0; xx <= rectangle.width; xx++) {
                    for (int yy = 0; yy <= rectangle.height; yy++) {
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
    public boolean update(float delta) {
        if (this.kidState == KidState.DEAD) {
            this.ticksDead++;
            return false;
        }
        this.ticksLived++;

        if (this.sprite != null)
            this.sprite.update(delta);

        // Change velocity if player is in a state that is able to fall
        if (this.kidState.canFall()) {
            velY = velY + (gravity * Global.GRAVITY);
            if (velY > 9) {
                velY = 9;
            }
        }

        // Collision stuff

        // Return kid state to IDLE
        KidState previousState = this.kidState;
        this.kidState = KidState.IDLE;

        if (velX != 0) {
            if (this.kidState.canFall())
                this.setKidState(KidState.MOVING);

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

        if (velY != 0) {
            boolean falling = velY * Global.GRAVITY > 0;

            boolean canChangeY = false;
            if (previousState.canFall())
                canChangeY = floorCollision(falling);
            else
                canChangeY = floorCollision(false) | floorCollision(true);

            if (Math.abs(velY) < 1) {
                if (floorCollision(falling)) {

                    this.y += velY;

                    if (this.kidState.canFall()) {
                        if (falling)
                            this.kidState = KidState.FALLING;
                        else
                            this.kidState = KidState.JUMPING;
                    }
                }
            }
            else {
                for (double yvel = 0; yvel < Math.ceil(Math.abs(velY)); yvel++) {
                    if (floorCollision(falling) || !falling) {
                        if (falling) {
                            this.y++;

                            if (this.kidState.canFall())
                                this.kidState = KidState.FALLING;
                        }
                        else {
                            this.y--;

                            if (this.kidState.canFall())
                                this.kidState = KidState.JUMPING;
                        }
                    }
                }
            }
        }

        //wall


        if (velY > 0 && canJump == maxJumps) {
            canJump--;
        }

        if (this.kidState == KidState.IDLE) {
            this.setSprite(this.getSprites().get("idle"));
        }
        else if (this.kidState == KidState.MOVING) {
            this.setSprite(this.getSprites().get("running"));
        }
        else if (this.kidState == KidState.JUMPING) {
            this.setSprite(this.getSprites().get("jump"));
        }
        else if (this.kidState == KidState.FALLING) {
            this.setSprite(this.getSprites().get("fall"));
        }
        else if (this.kidState.name().startsWith("SLIDING")) {
            this.setSprite(this.getSprites().get("sliding"));
        }
        else {
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
        }
        velX = 0;

        return true;
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        sprite.draw(camera, graphics, this.x + (xScale == -1 ? 3 : 0), this.y, xScale == -1, Global.GRAVITY != 1);

        this.getHitbox().renderHitbox(camera, x, y, graphics);

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

    public void reset() {
        if (this.onReset()) {
            this.ticksDead = 0;

            SaveData data = this.getHandler().getSaveData();

            Room room = this.getHandler().getRoom();

            if (!data.getRoomId().equalsIgnoreCase(room.getInternalId()))
                room = this.getHandler().getMain().getRoomManager().getRoom(this.getHandler(), data.getRoomId());

            room.reset();

            this.x = data.getX();
            this.y = data.getY();

            this.kidState = KidState.IDLE;
            canJump = 1;
            velY = 0;
        }
    }

    public void kill() {
        if (this.onDeath()) {
            this.ticksLived = 0;
            this.kidState = KidState.DEAD;

            Random random = Global.RANDOM;
            for (int i = 0; i < 150; i++) {
                double velX = (random.nextDouble() * 2 - 1) * 8;
                double velY = (random.nextDouble() * 10 - 2) * -1;

                while (Math.sqrt(velX * velX + velY * velY) > 8) {
                    velX = (random.nextDouble() * 2 - 1) * 8;
                    velY = (random.nextDouble() * 10 - 2) * -1;
                }

                BloodParticle particle = new BloodParticle(this.x + 16, this.y + 16, velX, velY, this.getHandler().getRoom());
                this.getHandler().getParticleManager().addParticle(particle);
            }

            this.x = -32;
            this.y = -32;
        }
    }

    private boolean floorCollision(boolean falling) {
        Rectangle rectangle = this.getHitbox().getRectIfPossible();
        
        for (int i = (int) rectangle.getX(); i < rectangle.getX() + rectangle.getWidth(); i++) {
            int x = (int) this.x + i;

            for (GameObject gameObject : this.handler.getRoom().getObjectsAt(x, (int) this.y + (falling ? rectangle.getY() + rectangle.getHeight() : rectangle.getY()))) {
                if (gameObject instanceof Block) {
                    velY = 0;

                    if (falling) {
                        canJump = 2;
                        ((Block) gameObject).getOnTouch().run();
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

                            ((Interactable) gameObject).getOnTouch().run();
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean wallCollision(int xScale) {
        Rectangle rectangle = this.getHitbox().getRectIfPossible();
        
        for (int i = (int) rectangle.getY(); i < rectangle.getY() + rectangle.getHeight(); i++) {
            int y = (int) this.y + i;

            for (GameObject gameObject : this.handler.getRoom().getObjectsAt((int) this.x + (xScale == 1 ? rectangle.getX() + rectangle.getWidth() : rectangle.getWidth()), y)) {
                if (gameObject instanceof Block) {
                    return false;
                }
                else if (gameObject instanceof Interactable) {
                    if (gameObject.getHitbox() != null) {
                        if (this.getHitbox().intersects(gameObject.getHitbox(), (int) this.x, (int) this.y, (int) gameObject.getX(), (int) gameObject.getY())) {

                            if (gameObject instanceof KillerObject) {
                                this.kill();
                                break;
                            }

                            ((Interactable) gameObject).getOnTouch().run();
                        }
                    }
                }
            }
        }
        return true;
    }

    public void addDefaultKeys() {
        this.getKeyListener().add(this.kidController = new KeyListener() {
            private boolean pressedShift = false;
            long lastRelease = 0;

            private void jump(JumpType type) {
                long time = System.currentTimeMillis() - lastRelease;

                if (type != JumpType.VINE_JUMP)
                    System.out.println("Normal jump");

                // cancel jump
                if (((time >= 20 && time < 40)&& canJump > 0) || (type != null && type == JumpType.CANCEL_JUMP)) {
                    onJump(JumpType.CANCEL_JUMP);
                    velY = -(canJump-- == 1 ? jump2 : jump) * gravity;

                    kidState = KidState.JUMPING;
                }
                else if (canJump == maxJumps || (type != null && type == JumpType.FULL_JUMP)) { //main jump
                    onJump(JumpType.FULL_JUMP);
                    canJump = 1;
                    velY = -jump;
                    pressedShift = true;

                    kidState = KidState.JUMPING;
                } else if ((canJump > 0 && canJump < maxJumps) || (type != null && type == JumpType.DOUBLE_JUMP)) { //double jump
                    onJump(JumpType.DOUBLE_JUMP);
                    canJump = 0;
                    velY = -jump2;
                    pressedShift = true;

                    kidState = KidState.JUMPING;
                }
                else if (type != null && type == JumpType.VINE_JUMP) {
                    onJump(JumpType.VINE_JUMP);
                    velY = -jump;
                }
            }

            @Override
            public void onKeyPress(int keycode) {
                if (keycode == KeyEvent.VK_W && getHandler().getMain().isDebugging()) {// warp to your mouse point - only in debug mode
                    Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                    Point appLocation = getHandler().getMain().getWindow().getFrame().getLocationOnScreen();

                    setX(mousePoint.x - appLocation.x - 16 + getHandler().getMain().getScreen().getCamera().getCameraX());
                    setY(mousePoint.y - appLocation.y - 48 + getHandler().getMain().getScreen().getCamera().getCameraY());
                }

                else if (keycode == Keybinds.RESET) {
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
                    if (keycode == Keybinds.JUMP) {
                        jump(null);
                    }
                    //shoot
                    else if (keycode == Keybinds.SHOOT) {
                        onShoot();
                    }
                }

                // skip cutscene in current room
                else if (keycode == Keybinds.SKIP_CUTSCENE) {
                    if (getHandler().getRoom() instanceof EventRoom) {
                        Event currentEvent = ((EventRoom) getHandler().getRoom()).getEventHandler().getCurrentEvent();

                        if (currentEvent instanceof CutsceneEvent) {
                            ((CutsceneEvent) currentEvent).setSkip(true);
                        }
                    }
                }
            }

            @Override
            public void onKeyHold(List<Integer> keyCodes) {
                if (!keyCodes.contains(Keybinds.LEFT) && !keyCodes.contains(Keybinds.RIGHT)) {
                    return;
                }

                if (keyCodes.contains(Keybinds.LEFT)) {
                    KidState direction = KidState.SLIDING_LEFT;
                    if (keyCodes.contains(Keybinds.RIGHT)) {
                        velX = 3;
                        xScale = 1;
                    } else {
                        velX = -3;
                        xScale = -1;
                        direction = KidState.SLIDING_RIGHT;
                    }

                    if (kidState == direction && keyCodes.contains(Keybinds.JUMP)) {
                        x = x + velX * 5;
                        velX = 0;
                        jump(JumpType.VINE_JUMP);
                        System.out.println("Vine jump 2");
                    }

                } else if (keyCodes.contains(Keybinds.RIGHT)) {
                    velX = 3;
                    xScale = 1;

                    if (kidState == KidState.SLIDING_LEFT && keyCodes.contains(Keybinds.JUMP)) {
                        x = x + velX * 5;
                        velX = 0;
                        jump(JumpType.VINE_JUMP);
                        System.out.println("Vine jump 1");
                    }
                }
            }

            @Override
            public void onKeyRelease(int keycode) {
                if (keycode == Keybinds.JUMP && pressedShift && velY * Global.GRAVITY < 0) {
                    velY *= gravity;
                    pressedShift = false;
                }

                if (keycode == Keybinds.JUMP) {
                    lastRelease = System.currentTimeMillis();
                }
            }
        });
    }
}
