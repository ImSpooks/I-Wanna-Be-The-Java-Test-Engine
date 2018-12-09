package me.ImSpooks.IWBTJ.object.interfaces;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.event.EventTarget;
import me.ImSpooks.IWBTJ.event.Listener;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyHoldEvent;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyPressEvent;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyReleaseEvent;
import me.ImSpooks.IWBTJ.game.mainmenu.SaveSelection;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Blood;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Bow;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.KidBullet;
import me.ImSpooks.IWBTJ.save.SaveFile;
import me.ImSpooks.IWBTJ.settings.Global;
import me.ImSpooks.IWBTJ.settings.Keybinds;
import me.ImSpooks.IWBTJ.sound.SoundPlayer;
import me.ImSpooks.IWBTJ.utils.SomeValues;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class KidObject extends KidObjects {

    protected boolean death;
    protected boolean frozen = false;

    public int xScale;
    public List<KidBullet> bullets = new ArrayList<>();
    private List<Blood> blood = new ArrayList<>();

    private Bow bow = null;
    private boolean checkDifficulty = false;

    public int canJump = 0;

    public double gravity = 0.4 * Global.gravity;
    public double jump = 8.1 * Global.gravity; // use 8.1 instead of 8.5
    public double jump2 = 6.6 * Global.gravity; // use 6.6 instead of 7

    private KeyInputs keyInputs = null;

    public KidObject(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
        this.death = false;
        this.xScale = 1;
    }

    @Override
    public void initialize() {
        for (int bloodAmount = 0; bloodAmount < (int) (50 * 2.5); bloodAmount++) {
            Blood blood = new Blood(-150, -150, ID.MISC, handler, this);
            BufferedImage image = blood.setSprite("/sprites/kid/sprBlood.png");
            blood.renderedSprite = scaleImage(image.getSubimage(4 * SomeValues.getRandom(0, 2), 0, 4, 4), 2);
            this.blood.add(blood);
            handler.addObject(blood);
        }

        KidBullet.amount = 4;
    }

    @Override
    public void tick() {
        if (!checkDifficulty && SaveSelection.selected != null && SaveSelection.selected.getDifficulty() == SaveFile.Difficulty.MEDIUM) {
            checkDifficulty = true;
            bow = new Bow(0, 0, ID.MISC, handler, this);
            handler.addObject(bow);
        }
    }

    public void resetKid() {
        if (deathSound != null) {
            deathSound.stop();
        }
        if (SoundPlayer.currentPlaying != null) {
            SoundPlayer.currentPlaying.resume();
        }
        if (keyInputs == null) {
            handler.getMain().getEventManager().register(keyInputs = new KeyInputs());
        }
        canJump = 1;
        SaveFile file = SaveSelection.selected;

        this.death = false;
        this.x = file.getPlayerX();
        this.y = file.getPlayerY();
        Global.gravity = file.isFlipGravity() ? -1.0 : 1.0;

        for (KidBullet bullet : bullets) {
            bullet.removeObject(true);
        }
        bullets.clear();
        KidBullet.amount = 0;

        blood.forEach(blood -> {
            blood.setX(-150);
            blood.setY(-150);
            blood.setVelX(0);
            blood.setVelY(0);
        });

        velX = 0;
        velY = 0;
    }

    private SoundPlayer deathSound = null;
    public void handleDeath() {
        SoundPlayer.currentPlaying.pause();

        deathSound = SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getMusic("onDeath"));
        SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidDeath"));
        for(Blood blood : blood) {
            blood.setX(x + 16);
            blood.setY(y + 23);

            blood.setVelX(SomeValues.getRandom(-4.0, 4.0));
            blood.setVelY((0.1 + SomeValues.getRandom(-4.0, 2.1)) * Global.gravity);
        }

        this.death = true;
        this.x = -150;
        this.y = -150;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }

    public boolean isDeath() {
        return death;
    }

    private boolean pressedShift = false;
    public class KeyInputs implements Listener {

        @EventTarget
        public void onKeyPress(KeyPressEvent e) {
            int key = e.getKeyCode();


            if (key == KeyEvent.VK_W && handler.getMain().isDebuging()) {// warp to your mouse point - only in debug mode
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                Point appLocation = Main.getInstance().frame.getLocationOnScreen();

                x = mousePoint.x - appLocation.x - 16;
                y = mousePoint.y - appLocation.y - 48;
            }

            else if (key == Keybinds.KEY_RESET) {
                resetKid();
            }

            if (!frozen) {
                if (handler.getMain().isDebuging()) { // change alignment - only in debug mode
                    if (key == KeyEvent.VK_A) {
                        x = x - 1;
                    } else if (key == KeyEvent.VK_D) {
                        x = x + 1;
                    }
                }

                if (e.isKeyDown(Keybinds.KEY_UP)) {

                } else if (e.isKeyDown(Keybinds.KEY_DOWN)) {

                }

                //jump
                if (key == Keybinds.KEY_JUMP) {
                    if (KidObject.this.canJump == 2) { //main jump
                        KidObject.this.canJump = 1;

                        SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidJump")).start();
                        velY = -jump;

                        pressedShift = true;
                    } else if (KidObject.this.canJump == 1) { //double jump
                        KidObject.this.canJump = 0;

                        SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidDJump")).start();
                        velY = -jump2;

                        pressedShift = true;
                    }
                }

                //shoot
                else if (key == Keybinds.KEY_SHOOT) {
                    if (KidBullet.amount < 4) {
                        KidBullet bullet = new KidBullet(KidObject.this.getX(), KidObject.this.getY(), ID.BULLET, KidObject.this.handler, KidObject.this);
                        KidObject.this.handler.addObject(bullet);
                        bullets.add(bullet);

                        SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidShoot")).start();
                    }
                }
            }

            else if (key == Keybinds.KEY_SKIP) {
                //TODO skip cutscene
            }
        }


        @EventTarget
        public void onKeyHold(KeyHoldEvent e) {
            // move mechanic
            if (e.isKeyDown(Keybinds.KEY_LEFT)) {
                if (e.isKeyDown(Keybinds.KEY_RIGHT)) {
                    KidObject.this.velX = 3;
                    KidObject.this.xScale = 1;
                } else {
                    KidObject.this.velX = -3;
                    KidObject.this.xScale = -1;
                }
            } else if (e.isKeyDown(Keybinds.KEY_RIGHT)) {
                KidObject.this.velX = 3;
                KidObject.this.xScale = 1;
            }
        }

        @EventTarget
        public void onKeyRelease(KeyReleaseEvent e) { // when shift release reduce y velocity
            if (e.getKeycode() == Keybinds.KEY_JUMP && pressedShift && velY * Global.gravity < 0) {
                velY *= gravity;
                pressedShift = false;
            }
        }
    }
}
