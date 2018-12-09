package me.ImSpooks.IWBTJ.event.events.keyboard;

import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.event.Event;

public class KeyHoldEvent extends Event {

    private final long when;
    private final int keyCode;

    public KeyHoldEvent(long when, int keycode) {
        this.when = when;
        this.keyCode = keycode;
    }

    public long getWhen() {
        return when;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isKeyDown(int keycode) {
        return Main.getInstance().getKeyboard().getKeys().containsKey(keycode);
    }

    public long getPressTimeMilis(int keycode) {
        return System.currentTimeMillis() - Main.getInstance().getKeyboard().getKeys().get(keycode);
    }
}