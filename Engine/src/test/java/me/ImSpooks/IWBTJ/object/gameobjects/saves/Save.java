package me.ImSpooks.IWBTJ.object.gameobjects.saves;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.KidBullet;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;
import me.ImSpooks.IWBTJ.settings.Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Save extends GameObject {

    public Save(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    private int saveticks = 0;
    @Override
    public void render(Graphics g) {
        int renderFrame;
        if (saveticks < 0)
            renderFrame = 0;
        else
            renderFrame = 1; // TODO save points instellen.

        drawImage(g, getSprite(), this.x, this.y, renderFrame, getSpriteWidth(), getSpriteHeight());
    }

    @Override
    public void tick() {
        saveticks--;

        boolean canSave = false;
        if (!upsidedown() && Global.gravity > 0.0) {
            canSave = true;
        }
        else if (upsidedown() && Global.gravity < 0.0) {
            canSave = true;
        }

        if (canSave && saveticks < 30) {
            innerloop:
            for (IObject object : getObjectInRadius(this, 32, true)) {
                if (object.getId() == ID.BULLET) {
                    if (object.getBounds().doesCollide(getBounds())) {
                        saveGame();
                        break innerloop;
                    }
                }
            }
        }
    }

    protected boolean upsidedown() {
        return false;
    }

    private void saveGame() {
        saveticks = 53;

        Global.saveGame();
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
        return 1;
    }

    @Override
    public double getImageSpeed() {
        return 0;
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
                return Save.this;
            }
        };
    }

    public List<IObject> getObjectInRadius(IObject object, int maxDistance, boolean calculateMiddlepoint){
        List<IObject> objects = new ArrayList<>();

        int width = 0;
        int height = 0;

        if (object.getBounds() != null) {
            width = object.getBounds().getWitdh();
            height = object.getBounds().getHeight();
        }

        int px = (int)object.getX();
        int py = (int)object.getY();
        int sx = (int)object.getX() + width;
        int sy = (int)object.getY() + height;

        if (object.getBounds() != null) {
            px = px + object.getBounds().getStartX();
            py = py + object.getBounds().getStartY();
        }

        if (handler.getKid() != null) {
            for (KidBullet allObject : handler.getKid().bullets) {
                if (calculateMiddlepoint) {
                    if (handler.getMain().distance((px + sx) / 2, allObject.getX(), (py + sy) / 2, allObject.getY()) > maxDistance)
                        continue;
                }
                else {
                    if (handler.getMain().distance(px, allObject.getX(), py, allObject.getY()) > maxDistance)
                        continue;
                }


                if (object.getBounds().doesCollide(allObject.getBounds())) {
                    objects.add(allObject);
                }
            }
        }
        return objects;
    }
}
