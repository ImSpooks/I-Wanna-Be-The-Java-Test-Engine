package me.ImSpooks.IWBTJ.object.gameobjects.misc;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;

public class Wootar extends GameObject {

    private WaterType waterType;

    public Wootar(double x, double y, ID id, Handler handler, WaterType waterType) {
        super(x, y, id, handler);
        this.waterType = waterType;
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
            if (handler.getMain().distance(x, kid.getX(), y, kid.getY()) > 32)
                return;
            if (getBounds().doesCollide(kid.getBounds())) {
                if (waterType == WaterType.FULL_JUMP) {
                    if (kid.getVelY() > 0)
                        kid.canJump = 2;
                }
                else if (waterType == WaterType.DOUBLE_JUMP) {
                    kid.canJump = 1;
                }
                else {
                    if (kid.canJump == 0 && kid.getVelY() > 0)
                        kid.canJump = 1;
                }

                if (kid.getVelY() > 1.6)
                    kid.setVelY(1.6);
            }
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
                return Wootar.this;
            }
        };
    }

    public WaterType getWaterType() {
        return waterType;
    }

    public enum WaterType {
        NO_JUMP,
        DOUBLE_JUMP,
        FULL_JUMP,
        ;
    }
}
