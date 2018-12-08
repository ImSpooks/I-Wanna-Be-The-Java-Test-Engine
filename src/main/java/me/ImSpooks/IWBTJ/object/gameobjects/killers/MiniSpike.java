package me.ImSpooks.IWBTJ.object.gameobjects.killers;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.object.ID;

import java.awt.*;

public class MiniSpike extends Spike {

    public MiniSpike(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    @Override
    public int getSpriteWidth() {
        return 16;
    }

    @Override
    public int getSpriteHeight() {
        return 16;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public Hitbox setBounds() {
        return super.setBounds();
    }
}
