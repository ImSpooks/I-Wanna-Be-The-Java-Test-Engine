package me.ImSpooks.IWBTJ.object.gameobjects.blocks;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;

import java.awt.*;

public class MiniBlock extends Block {

    public MiniBlock(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    @Override
    public int getFrameLength() {
        return 0;
    }

    @Override
    public double getImageSpeed() {
        return 0;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public Hitbox setBounds() {
        return new RectangleHitbox() {
            @Override
            public void initialize() {
                for (int x = 0; x <= getSpriteWidth(); x++) {
                    for (int y = 0; y <= getSpriteHeight(); y++) {
                        addPixel(x, y);
                    }
                }
            }

            @Override
            public IObject getOwner() {
                return MiniBlock.this;
            }
        };
    }

    @Override
    public int getSpriteWidth() {
        return 16;
    }

    @Override
    public int getSpriteHeight() {
        return 16;
    }
}