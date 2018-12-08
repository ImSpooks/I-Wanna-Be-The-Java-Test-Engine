package me.ImSpooks.IWBTJ.object.gameobjects.blocks;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SaveBlocker extends GameObject {

    public SaveBlocker(double x, double y, ID id, Handler handler) {
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
        if (ticks > 0)
            ticks--;
    }

    public int ticks = 0;

    @Override
    public void render(Graphics g) {
        if (ticks > 0) {
            int alpha = (int) Math.floor(ticks * (255.0 / 25.0 / 2));
            if (ticks > 25)
                alpha = (int) Math.floor(25 * (255.0 / 25.0 / 2));

            BufferedImage imageCopy = cloneimage(getSprite());

            for (int cx=0;cx<imageCopy.getWidth();cx++) {
                for (int cy=0;cy<imageCopy.getHeight();cy++) {
                    int color = imageCopy.getRGB(cx, cy);

                    int mc = (alpha << 24) | 0x00ffffff;
                    int newcolor = color & mc;
                    imageCopy.setRGB(cx, cy, newcolor);
                }

            }

            drawImage(g, imageCopy, this.x, this.y, 0, getSpriteWidth(), getSpriteHeight());
        }
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
                return SaveBlocker.this;
            }
        };
    }

    @Override
    public int getSpriteWidth() {
        return 32;
    }

    @Override
    public int getSpriteHeight() {
        return 32;
    }
}