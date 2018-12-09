package me.ImSpooks.IWBTJ.game.room.stage1;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.game.IRoom;

import java.awt.*;

public class Room1 extends IRoom {
    public Room1(Handler handler, String resourcePath) {
        super(handler, resourcePath);
    }

    @Override
    public void renderRoom(Graphics g) {
        renderObjects(g);
    }

    @Override
    public int getRoomNumber() {
        return 1;
    }
}
