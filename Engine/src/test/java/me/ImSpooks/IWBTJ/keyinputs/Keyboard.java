package me.ImSpooks.IWBTJ.keyinputs;

import me.ImSpooks.IWBTJ.event.events.keyboard.KeyHoldEvent;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyPressEvent;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyReleaseEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class Keyboard extends KeyAdapter {

    private HashMap<Integer, Long> keys;

    public Keyboard() {
        this.keys = new HashMap<>();
    }

    public void tickKeyboard() {
        HashMap<Integer, Long> keysCopy = new HashMap<>(); // keysCopy is to remove the java.util.ConcurrentModificationException crash
        keysCopy.putAll(keys);

        keysCopy.entrySet().forEach(integerLongEntry -> {
            try {
                new KeyHoldEvent(integerLongEntry.getValue(), integerLongEntry.getKey()).call();
            } catch (ConcurrentModificationException e) {
                // do nothing
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keys.containsKey(e.getKeyCode())) {
            keys.put(e.getKeyCode(), System.currentTimeMillis());

            new KeyPressEvent(System.currentTimeMillis(), e.getKeyCode()).call();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (keys.containsKey(e.getKeyCode())) {
            keys.remove(e.getKeyCode());

            new KeyReleaseEvent(System.currentTimeMillis(), e.getKeyCode()).call();
        }
    }

    public HashMap<Integer, Long> getKeys() {
        return keys;
    }

    public long removeKey(int keycode) {
        return keys.remove(keycode);
    }
}