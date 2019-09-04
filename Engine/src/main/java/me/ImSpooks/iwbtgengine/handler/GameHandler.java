package me.ImSpooks.iwbtgengine.handler;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class GameHandler {

    @Getter private final Main main;

    @Getter private Room room;

    @Getter @Setter private KidObject kid;

    public GameHandler(Main main) {
        this.main = main;
    }

    public void render(Graphics graphics) {
        if (this.room != null) {
            room.render(graphics);
        }
        if (kid != null) {
            kid.render(graphics);
        }
    }

    public void update(float delta) {
        if (this.room != null) {
            room.update(delta);
        }
        if (kid != null) {
            kid.update(delta);
        }
    }

    public void setRoom(Room room) {
        this.room = room;
        //room.reset();
    }
}
