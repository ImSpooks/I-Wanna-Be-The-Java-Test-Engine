package me.ImSpooks.IWBTJ.object.gameobjects.killers;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.object.interfaces.KillerObject;
import me.ImSpooks.IWBTJ.utils.SomeValues;

import java.awt.*;

public class KillerBlock extends KillerObject {

    public KillerBlock(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    @Override
    public int getSpriteWidth() {
        return 32;
    }

    @Override
    public int getSpriteHeight() {
        return 32;
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
        Kid kid = handler.getKid();
        if (kid != null && !kid.isDeath()) {
            if (handler.getMain().distance(x, kid.getX(), y, kid.getY()) > SomeValues.maxDistance(32, 32))
                return;
            if (getBounds().doesCollide(kid.getBounds())) {
                kid.handleDeath();
            }
        }
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
                return KillerBlock.this;
            }
        };
    }
}
