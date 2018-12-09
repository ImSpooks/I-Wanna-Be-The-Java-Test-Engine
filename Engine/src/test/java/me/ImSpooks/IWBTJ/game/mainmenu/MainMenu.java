package me.ImSpooks.IWBTJ.game.mainmenu;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.event.EventTarget;
import me.ImSpooks.IWBTJ.event.Listener;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyPressEvent;
import me.ImSpooks.IWBTJ.game.IRoom;
import me.ImSpooks.IWBTJ.settings.Global;
import me.ImSpooks.IWBTJ.settings.Keybinds;
import me.ImSpooks.IWBTJ.sound.SoundPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MainMenu extends IRoom {

    public MainMenu(Handler handler) {
        super(handler, "/room/main/MainMenu.jmap");
    }

    public void initialize(){
        handler.getMain().getEventManager().register(new KeyInputs());

        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.open(SoundPlayer.getRegisteredSounds().getMusic("guyRock"));
        soundPlayer.start();
        SoundPlayer.currentPlaying = soundPlayer;
    }

    @Override
    public void tick() {
        tickObjects();
    }

    @Override
    public void renderRoom(Graphics g) {
        // game font
        {
            Font font = new Font(Global.fontName, Font.PLAIN, 29);
            g.setColor(Color.BLACK);
            g.setFont(font);
            String str = "I Wanna Be The Java Engine";
            drawStringShadow(g, str, Main.gameWidth / 2 - g.getFontMetrics().stringWidth(str) / 2, 100);
        }

        // "shift to start" string
        {
            Font font = new Font(Global.fontName, Font.PLAIN, 25);
            g.setColor(Color.BLACK);
            g.setFont(font);
            String str = "Press [" + KeyEvent.getKeyText(Keybinds.KEY_JUMP).toUpperCase() + "] to start";
            drawStringShadow(g, str, Main.gameWidth / 2 - g.getFontMetrics().stringWidth(str) / 2, 400);
        }
    }

    public class KeyInputs implements Listener {

        @EventTarget
        public void onKeyPress(KeyPressEvent e) {
            int key = e.getKeyCode();
            if (key == Keybinds.KEY_JUMP) {
                e.setCancelled(true); // making sure events in other classes wont run

                handler.getMain().getEventManager().unregister(this);
                removeObject();
                MainMenu.this.handler.addObject(handler.getMain().getCachedRooms().getRoom("saveSelection"));
                //MainMenu.this.handler.addObject(new ObjKid(0, 256, ID.KID, MainMenu.this.handler));
            }
        }
    }

    @Override
    public int getRoomNumber() {
        return 0;
    }


    @Override
    public void onRemove() {
    }
}