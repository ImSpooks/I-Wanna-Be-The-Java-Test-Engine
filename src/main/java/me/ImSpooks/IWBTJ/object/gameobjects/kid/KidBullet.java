package me.ImSpooks.IWBTJ.object.gameobjects.kid;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.blocks.SaveBlocker;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObject;
import me.ImSpooks.IWBTJ.settings.Global;

public class KidBullet extends GameObject {

    public KidObject shooter;
    public static int amount = 0;

    private int bulletSpeed = 16;

    public KidBullet(double x, double y, ID id, Handler handler, KidObject shooter) {
        super(x, y, id, handler);

        setSprite("/sprites/kid/sprBullet.png");

        this.shooter = shooter;
        // adds one to the bullet count
        amount++;

        //spawns bullet at the right location
        if (shooter.xScale == 1) this.x = x + 32;

        this.y = y + 23;

        this.velX = bulletSpeed * shooter.xScale;
    }

    @Override
    public void tick() {
        for (int i = 0; i < Math.abs(bulletSpeed); i+=4) {
            if (Global.currentRoom != null) {
                x = x + (velX > 0 ? 4 : -4);
                for (IObject object : Global.currentRoom.getObjectInRadius(this, 32, true)) {
                    if ((object.getId() == ID.BLOCK || object instanceof SaveBlocker) && getBounds().doesCollide(object.getBounds())) {
                        if (object instanceof SaveBlocker) {
                            ((SaveBlocker) object).ticks = 32;
                        }
                        removeObject(false);
                        return;
                    }
                }
                x = x - (velX > 0 ? 4 : -4);
            }
            this.x = x + (velX > 0 ? 4 : -4);
        }

        if (x < 0 || x > Main.gameWidth) {
            removeObject(false);
            return;
        }
        if (y < 0 || y > Main.gameHeight) {
            removeObject(false);
        }
    }

    public void removeObject(boolean reset) {
        removeObject();
        if (!reset) {
            shooter.bullets.remove(this);
            amount--;
        }
    }

    @Override
    public Hitbox setBounds() {
        return new RectangleHitbox() {
            @Override
            public void initialize() {
                for (int x = 0; x <= 4; x++) {
                    for (int y = 0; y <= 4; y++) {
                        addPixel(x, y);
                    }
                }
            }

            @Override
            public IObject getOwner() {
                return KidBullet.this;
            }
        };
    }

    @Override
    public int getFrameLength() {
        return 1;
    }

    @Override
    public double getImageSpeed() {
        return 0.5;
    }

    @Override
    public int getSpriteWidth() {
        return 4;
    }

    @Override
    public int getSpriteHeight() {
        return 4;
    }
}
