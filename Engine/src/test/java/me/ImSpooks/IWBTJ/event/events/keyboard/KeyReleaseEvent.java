package me.ImSpooks.IWBTJ.event.events.keyboard;

import me.ImSpooks.IWBTJ.event.Event;

public class KeyReleaseEvent extends Event {

    private final long when;
    private final int keycode;

    public KeyReleaseEvent(long when, int keycode) {
        this.when = when;
        this.keycode = keycode;
    }

    public long getWhen() {
        return when;
    }

    public int getKeycode() {
        return keycode;
    }
}
