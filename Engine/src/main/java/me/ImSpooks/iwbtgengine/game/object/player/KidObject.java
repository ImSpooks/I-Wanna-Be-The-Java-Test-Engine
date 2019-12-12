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
import me.ImSpooks.iwbtgengine.game.object.objects.platforms.Platform;
import me.ImSpooks.iwbtgengine.game.object.particles.BloodParticle;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.EventRoom;
import me.ImSpooks.iwbtgengine.game.room.Room;
import me.ImSpooks.iwbtgengine.global.Global;
import me.ImSpooks.iwbtgengine.global.Keybinds;
import me.ImSpooks.iwbtgengine.handler.GameHandler;
import me.ImSpooks.iwbtgengine.keycontroller.KeyListener;
import org.tinylog.Logger;

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
    @Getter @Setter public double jump = 8.5 - this.gravity; // use 8.1 instead of 8.5
    @Getter @Setter public double jump2 = 7.0 - this.gravity; // use 6.6 instead of 7

    @Getter private Map<String, Sprite> sprites;

    @Getter private final GameHandler handler;

    @Getter private int ticksLived = 0, ticksDead = 0;

    @Getter private KeyListener kidController;

    @Getter private KidState previousState;
    @Getter @Setter private KidState kidState;

    @Getter private final Hitbox normalHitbox;
    @Getter private final Hitbox flippedHitbox;

    public KidObject(Room parent, double x, double y, GameHandler handler) {
        super(parent, x, y, null);
        this.handler = handler;

        this.sprites = this.getSpriteMap();

        Rectangle normalRectangle = new Rectangle(12, 11, 11, 21);//21
        this.setHitbox(normalHitbox = new Hitbox(this, Hitbox.HitboxType.KID, normalRectangle) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> list = new ArrayList<>();


                for (int xx = 0; xx <= normalRectangle.width; xx++) {
                    for (int yy = 0; yy <= normalRectangle.height; yy++) {
                        list.add(new int[] {(int) normalRectangle.getX() + xx, (int) normalRectangle.getY() + yy});
                    }
                }

                return list;
            }
        });

        Logger.debug("getHitbox() = " + getHitbox());

        Rectangle flippedRectangle = new Rectangle(12, 0, 11, 21);//21
        this.flippedHitbox = new Hitbox(this, Hitbox.HitboxType.KID, flippedRectangle) {
            @Override
            public List<int[]> getPixels() {
                List<int[]> list = new ArrayList<>();


                for (int xx = 0; xx <= flippedRectangle.width; xx++) {
                    for (int yy = 0; yy <= flippedRectangle.height; yy++) {
                        list.add(new int[] {(int) flippedRectangle.getX() + xx, (int) flippedRectangle.getY() + yy});
                    }
                }

                return list;
            }
        };

        this.addDefaultKeys();
    }

    @Override
    public void update(float delta) {
        this.getHandler().getRoom().resetObjects();

        if (this.kidState == KidState.DEAD) {
            this.ticksDead++;

            if (this.bloodTicks-- > 0) {
                Random random = Global.RANDOM;
                for (int i = 0; i < this.bloodParticles / this.maxBloodTicks; i++) {
                    double velX = (random.nextDouble() * 2 - 1) * 5.5;
                    double velY = (random.nextDouble() * 11 - 3) * -1;

                    while (Math.sqrt(velX * velX + velY * velY) > 8) {
                        velX = (random.nextDouble() * 2 - 1) * 8;
                        velY = (random.nextDouble() * 10 - 2) * -1;
                    }

                    BloodParticle particle = new BloodParticle(this.x + 16, this.y + 16, velX, velY, this.getHandler().getRoom());
                    this.getHandler().getParticleManager().addParticle(particle);
                }
            }
            return;
        }
        this.ticksLived++;

        if (this.sprite != null)
            this.sprite.update(delta);

        // Change velocity if player is in a state that is able to fall
        if (this.kidState.canFall()) {
            velY = velY + gravity * Global.GRAVITY;
            if (Global.GRAVITY > 0 && velY > 9) {
                velY = 9;
            }
            else if (Global.GRAVITY < 0 && velY < -9) {
                velY = -9;
            }
        }

        // Collision stuff

        // Return kid state to IDLE
        this.previousState = this.kidState;
        this.kidState = KidState.IDLE;

        boolean falling = Global.GRAVITY > 0 ? velY > 0 : velY < 0;
        if (velY != 0) {

            for (double yvel = 0; yvel <= Math.abs(velY); yvel += gravity) {
                boolean allowJump = floorCollision(falling) || !falling;

                if (allowJump) {
                    if (falling) {
                        this.y += gravity * Global.GRAVITY;

                        if (this.kidState.canFall())
                            this.kidState = KidState.FALLING;
                    }
                    else {
                        this.y -= gravity * Global.GRAVITY;

                        if (this.kidState.canFall())
                            this.kidState = KidState.JUMPING;
                    }
                }
            }
        }

        if (velX != 0) {
            if (this.kidState.canFall() && this.kidState == KidState.IDLE)
                this.kidState = KidState.MOVING;

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


        if ((Global.GRAVITY > 0 ? velY > 0 : velY < 0) && canJump == maxJumps) {
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
        tryJumping = false;
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        for (GameObject gameObject : debug) {
            if (gameObject.getUpdatedHitbox() == null)
                continue;
            gameObject.getUpdatedHitbox().renderHitbox(camera, gameObject.getX(), gameObject.getY(), graphics);
        }

        if (this.kidState == KidState.DEAD) {
            return;
        }
        debug.clear();

        if (this.kidState.name().startsWith("SLIDING")) {
            sprite.draw(graphics, camera, this.x + (xScale == -1 ? 3 : 0) + (7 * -xScale), this.y, xScale == -1, Global.GRAVITY != 1);
        }
        else {

            sprite.draw(graphics, camera, this.x + (xScale == -1 ? 3 : 0), this.y, xScale == -1, Global.GRAVITY != 1);
        }

        this.getHitbox().renderHitbox(camera, x, y, graphics);

        graphics.setColor(Color.RED);
        graphics.fillRect((int)Math.round(this.x) - camera.getCameraX(), (int)Math.round(getY() + hitbox.getRectIfPossible().getY() + hitbox.getRectIfPossible().getHeight() + Math.ceil(getVelY())) - camera.getCameraY(), 11, 10);
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

            Global.GRAVITY = !data.flippedGravity ? 1.0 : -1.0;
            this.hitbox = !data.flippedGravity ? normalHitbox : flippedHitbox;
        }
    }

    @Getter private int bloodTicks = 0;
    @Getter @Setter private int maxBloodTicks = 3;
    @Getter @Setter private int bloodParticles = 100;
    public void kill() {
        if (this.kidState != KidState.DEAD && this.onDeath()) {
            this.ticksLived = 0;
            this.kidState = KidState.DEAD;

            this.bloodTicks = this.maxBloodTicks;
        }
    }

    private List<GameObject> debug = new ArrayList<>();
    private boolean floorCollision(boolean falling) {
        List<GameObject> objects = new ArrayList<>();

        Rectangle rectangle = this.getHitbox().getRectIfPossible();
        for (int i = (int) rectangle.getX() + 1; i < rectangle.getX() + rectangle.getWidth(); i++) {
            int x = (int) this.x + i;

            double addedYFalling = 0;
            double addedYJump = 0;
            if (Global.GRAVITY > 0) {
                addedYFalling = rectangle.getY() + rectangle.getHeight();
                addedYJump = rectangle.getY();
            }
            else {
                addedYJump += rectangle.getHeight();
            }

            addedYFalling = addedYFalling + ((falling ? -gravity : gravity) * Global.GRAVITY);

            if (falling) {
                objects.addAll(this.handler.getRoom().getObjectsAt(x, this.y + addedYFalling, objects));

                this.handler.getRoom().getObjectsAt(x, this.y + addedYJump, objects).stream().filter(o -> o instanceof Interactable && !o.isSolid()).forEach(objects::add);
            }
            else {
                objects.addAll(this.handler.getRoom().getObjectsAt(x, this.y + addedYJump, objects));

                this.handler.getRoom().getObjectsAt(x, this.y + addedYFalling, objects).stream().filter(o -> o instanceof Interactable && !o.isSolid()).forEach(objects::add);
            }
        }
        
        // check for blocks FIRST

        for (GameObject gameObject : objects) {
            if (gameObject instanceof Block) {
                if (gameObject.getHitbox().getHitboxType() == Hitbox.HitboxType.CUSTOM && !this.getHitbox().intersects(gameObject.getHitbox(), this.x, this.y, gameObject.getX(), gameObject.getY()))
                    continue;

                velY = 0;

                if (falling) {
                    canJump = maxJumps;
                }

                if (((Block) gameObject).getOnTouch() != null)
                    ((Block) gameObject).getOnTouch().run(this);
                return false;
            }
            else if (gameObject instanceof Platform) {
                //((Platform) gameObject).onTouch().run(this);

                if (falling) {
                    Rectangle hitbox = this.hitbox.getRectIfPossible();
                    if ((Global.GRAVITY > 0 && this.y + hitbox.getY() + hitbox.getHeight() <= gameObject.getY() + 1 && this.velY > 0) || (Global.GRAVITY < 0 && this.y + hitbox.getY() >= gameObject.getY() + gameObject.getHeight() && this.velY < 0)) {
                        canJump = maxJumps;
                        velY = 0;
                        return false;
                    }
                }
            }
        }

        for (GameObject gameObject : objects) {
            if (gameObject instanceof Interactable) {
                if (gameObject.getHitbox() != null) {
                    if (this.getHitbox().intersects(gameObject.getHitbox(), this.x, this.y, gameObject.getX(), gameObject.getY())) {

                        if (gameObject instanceof KillerObject) {
                            ((KillerObject) gameObject).onTouch().run(this);
//                            return false;
                        }

                        if (((Interactable) gameObject).getOnTouch() != null)
                            ((Interactable) gameObject).getOnTouch().run(this);
                    }
                }
            }
        }
        return true;
    }

    private boolean wallCollision(int xScale) {

        List<GameObject> objects = new ArrayList<>();

        Rectangle rectangle = this.getHitbox().getRectIfPossible();
        for (int i = (int) rectangle.getY() + 1; i < rectangle.getY() + rectangle.getHeight(); i++) {
            int y = (int) this.y + i;
            objects.addAll(this.handler.getRoom().getObjectsAt((int) this.x + (xScale == 1 ? rectangle.getX() + rectangle.getWidth() : rectangle.getX()), y, objects));
        }

        // check for blocks FIRST

        for (GameObject gameObject : objects) {
            if (gameObject instanceof Block) {
                if (gameObject.getHitbox().getHitboxType() == Hitbox.HitboxType.CUSTOM && !this.getHitbox().intersects(gameObject.getHitbox(), this.x, this.y, gameObject.getX(), gameObject.getY()))
                    continue;

                if (((Block) gameObject).getOnTouch() != null)
                    ((Block) gameObject).getOnTouch().run(this);
                return false;
            }
        }
        for (GameObject gameObject : objects) {
            if (gameObject instanceof Interactable) {
                if (gameObject.getHitbox() != null) {
                    if (this.getHitbox().intersects(gameObject.getHitbox(), this.x - xScale, this.y, gameObject.getX(), gameObject.getY())) {

                        if (gameObject instanceof KillerObject) {
                            ((KillerObject) gameObject).onTouch().run(this);
                            debug.add(gameObject);
//                            return false;
                        }

                        if (((Interactable) gameObject).getOnTouch() != null)
                            ((Interactable) gameObject).getOnTouch().run(this);
                    }
                }
            }
        }
        return true;
    }



    private long lastRelease = 0;
    private boolean pressedShift = false;
    public void jump(JumpType type) {
        if (this.kidState == KidState.DEAD)
            return;

        long time = System.currentTimeMillis() - lastRelease;

        boolean jumped = false;

        tryJumping = true;

        // cancel jump
        if (type == JumpType.VINE_JUMP) {
            onJump(JumpType.VINE_JUMP);
            velY = -(9 - gravity);
            pressedShift = true;

            jumped = true;
        }
        else if (((time >= 20 && time < 40)&& canJump > 0) || type == JumpType.CANCEL_JUMP) {
            onJump(type = JumpType.CANCEL_JUMP);
            velY = -(canJump-- == 1 ? jump2 : jump) * gravity;

            jumped = true;
        }
        else if (canJump == maxJumps || type == JumpType.FULL_JUMP) { //main jump
            onJump(type = JumpType.FULL_JUMP);
            canJump = maxJumps - 1;
            velY = -jump;
            pressedShift = true;

            jumped = true;
        } else if ((canJump > 0 && canJump < maxJumps) || type == JumpType.DOUBLE_JUMP) { //double jump
            onJump(type = JumpType.DOUBLE_JUMP);
            canJump--;
            velY = -jump2;
            pressedShift = true;

            jumped = true;
        }

        y -= Global.GRAVITY * 2;

        if (jumped) {
            kidState = KidState.JUMPING;
            velY = velY * Global.GRAVITY;
        }
    }

    @Getter @Setter private boolean tryJumping = false;
    private void addDefaultKeys() {
        this.getKeyListener().add(this.kidController = new KeyListener() {
            private boolean pressedLeftOnVine = false;

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
                        // if player is riding a right vine -> jump
                        if (kidState == KidState.SLIDING_RIGHT && keyCodes.contains(Keybinds.JUMP) && previousState != KidState.IDLE && previousState != KidState.MOVING && !pressedLeftOnVine) {
                            pressedLeftOnVine = true;
                            velX = -3;
                            xScale = -1;

                            x = x - 15;
                            velX = 0;
                            jump(JumpType.VINE_JUMP);
                            return;
                        }
                    } else {
                        velX = -3;
                        xScale = -1;
                        direction = KidState.SLIDING_RIGHT;
                    }

                    // if player is riding a vine in the opposite direction of moving -> jump
                    if (kidState == direction && keyCodes.contains(Keybinds.JUMP) && previousState != KidState.IDLE && previousState != KidState.MOVING) {
                        x = x + (xScale == 1 ? 15 : -15);
                        velX = 0;
                        jump(JumpType.VINE_JUMP);
                    }

                } else if (keyCodes.contains(Keybinds.RIGHT)) {
                    velX = 3;
                    xScale = 1;

                    // if player is riding a left side vine -> jump
                    if (kidState == KidState.SLIDING_LEFT && keyCodes.contains(Keybinds.JUMP) && previousState != KidState.IDLE && previousState != KidState.MOVING) {
                        x = x + 15;
                        velX = 0;
                        jump(JumpType.VINE_JUMP);
                    }
                }
            }

            @Override
            public void onKeyRelease(int keycode) {
                if (keycode == Keybinds.JUMP && pressedShift && velY * Global.GRAVITY < 0) {
                    velY = (velY - gravity * Global.GRAVITY) * 0.45;
                    pressedShift = false;
                }

                if (keycode == Keybinds.JUMP) {
                    lastRelease = System.currentTimeMillis();
                }
                if (keycode == Keybinds.LEFT) {
                    pressedLeftOnVine = false;
                }
            }
        });
    }
}
