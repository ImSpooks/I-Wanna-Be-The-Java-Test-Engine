package me.ImSpooks.iwbtgengine.keycontroller;


import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.GameObject;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class KeyController extends KeyAdapter {

    private final Main main;
    private HashMap<Integer, Long> keys;

    public KeyController(Main main) {
        this.keys = new HashMap<>();
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    public void tickKeyboard() {
        if (main.getHandler() == null)
            return;

        try {
            List<Integer> keyList = new ArrayList<>(keys.keySet());

            for (GameObject gameObject : main.getHandler().getRoom().getObjects()) {
                if (!gameObject.getKeyListener().isEmpty()) {
                    gameObject.getKeyListener().forEach(keyListener -> keyListener.onKeyHold(keyList));
                }
            }

            if (main.getHandler().getKid() != null) {
                if (!main.getHandler().getKid().getKeyListener().isEmpty()) {
                    main.getHandler().getKid().getKeyListener().forEach(keyListener -> keyListener.onKeyHold(keyList));
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException thrown, but whatever TODO REMOVE THIS MESSAGE");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(-1);
            return;
        }

        if (main.getHandler() == null)
            return;

        if (!keys.containsKey(e.getKeyCode())) {
            keys.put(e.getKeyCode(), System.currentTimeMillis());

            for (GameObject gameObject : main.getHandler().getRoom().getObjects()) {
                if (!gameObject.getKeyListener().isEmpty()) {
                    gameObject.getKeyListener().forEach(keyListener -> keyListener.onKeyPress(e.getKeyCode()));
                }
            }

            if (main.getHandler().getKid() != null) {
                if (!main.getHandler().getKid().getKeyListener().isEmpty()) {
                    main.getHandler().getKid().getKeyListener().forEach(keyListener -> keyListener.onKeyPress(e.getKeyCode()));
                }
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (main.getHandler() == null)
            return;

        if (keys.containsKey(e.getKeyCode())) {
            keys.remove(e.getKeyCode());


            for (GameObject gameObject : main.getHandler().getRoom().getObjects()) {
                if (!gameObject.getKeyListener().isEmpty()) {
                    gameObject.getKeyListener().forEach(keyListener -> keyListener.onKeyRelease(e.getKeyCode()));
                }
            }

            if (main.getHandler().getKid() != null) {
                if (!main.getHandler().getKid().getKeyListener().isEmpty()) {
                    main.getHandler().getKid().getKeyListener().forEach(keyListener -> keyListener.onKeyRelease(e.getKeyCode()));
                }
            }
        }
    }

    public HashMap<Integer, Long> getKeys() {
        return keys;
    }

    public long removeKey(int keycode) {
        return keys.remove(keycode);
    }
}
