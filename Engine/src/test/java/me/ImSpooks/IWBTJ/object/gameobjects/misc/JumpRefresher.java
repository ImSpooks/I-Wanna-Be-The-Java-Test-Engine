package me.ImSpooks.IWBTJ.object.gameobjects.misc;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.CustomHitbox;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;
import me.ImSpooks.IWBTJ.utils.SomeValues;

import java.awt.*;
import java.awt.image.BufferedImage;

public class JumpRefresher extends GameObject{

    public JumpRefresher(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    @Override
    public int getSpriteWidth() {
        return 13;
    }

    @Override
    public int getSpriteHeight() {
        return 13;
    }

    @Override
    public int getFrameLength() {
        return 0;
    }

    @Override
    public double getImageSpeed() {
        return 0;
    }

    int ticksAway = 0;

    @Override
    public void tick() {
        if (ticksAway == 0) {
            Kid kid = handler.getKid();
            if (kid != null && !kid.isDeath()) {
                if (handler.getMain().distance(x, kid.getX(), y, kid.getY()) > SomeValues.maxDistance(32, 32))
                    return;
                if (getBounds().doesCollide(kid.getBounds())) {
                    if (kid.canJump == 0)
                        kid.canJump = 1;
                    ticksAway = 100;
                }
            }
        }
        if (ticksAway > 0)
            ticksAway--;
    }

    @Override
    public void render(Graphics g) {
        if (ticksAway > 0) {
            BufferedImage cloneImage = cloneimage(getSprite());
            int alpha = 25;
            for (int cx=0;cx<cloneImage.getWidth();cx++) {
                for (int cy=0;cy<cloneImage.getHeight();cy++) {
                    int color = cloneImage.getRGB(cx, cy);

                    int mc = (alpha << 24) | 0x00ffffff;
                    int newcolor = color & mc;
                    cloneImage.setRGB(cx, cy, newcolor);
                }

            }
            g.drawImage(cloneImage, (int)x, (int)y, null);
        }
        else {
            g.drawImage(getSprite(), (int)x, (int)y, null);
        }
    }

    @Override
    public Hitbox setBounds() {
        return new CustomHitbox() {
            @Override
            public void initialize() {
                BufferedImage image = getSprite();

                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {

                        if ((image.getRGB(x,y) >>24) == 0x00)
                            continue;

                        addPixel(x, y);
                    }
                }
            }

            @Override
            public IObject getOwner() {
                return JumpRefresher.this;
            }
        };
    }
}
