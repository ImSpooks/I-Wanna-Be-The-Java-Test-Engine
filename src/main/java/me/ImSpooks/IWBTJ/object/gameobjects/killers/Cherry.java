package me.ImSpooks.IWBTJ.object.gameobjects.killers;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.CustomHitbox;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.object.interfaces.KillerObject;
import me.ImSpooks.IWBTJ.object.interfaces.MultipleSprites;
import me.ImSpooks.IWBTJ.utils.SomeValues;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cherry extends KillerObject implements MultipleSprites {

    public Cherry(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    @Override
    public int getSpriteWidth() {
        return 21;
    }

    @Override
    public int getSpriteHeight() {
        return 24;
    }

    @Override
    public int getFrameLength() {
        return 1;
    }

    @Override
    public double getImageSpeed() {
        return 0.035;
    }

    @Override
    public void tick() {
        double lastX = x;
        Kid kid = handler.getKid();
        if (kid != null && !kid.isDeath()) {
            x = x + kid.getLastVelX();
            if (handler.getMain().distance(x, kid.getX(), y, kid.getY()) < SomeValues.maxDistance(32, 32)) {
                if (getBounds().doesCollide(kid.getBounds())) {
                    kid.handleDeath();
                }
            }
        }
        x = lastX;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public boolean onSpriteChange() {
        getBounds().clear();
        setBounds();
        return false;
    }

    @Override
    public Hitbox setBounds() {
        return new CustomHitbox() {
            @Override
            public void initialize() {
                BufferedImage image = getCurrentSprite();

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
                return Cherry.this;
            }
        };
    }
}
