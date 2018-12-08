package me.ImSpooks.IWBTJ.object.gameobjects.kid;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bow extends KidObjects {

    private KidObject kid;

    public Bow(double x, double y, ID id, Handler handler, KidObject kid) {
        super(x, y, id, handler);
        this.kid = kid;
    }

    private double lastX = -1;
    private double lastY = -1;

    @Override
    public void tick() {
        if (waitFrame()) {
            if (lastX == -1) {
                lastX = kid.getX();
            }
            if (lastY == -1) {
                lastY = kid.getY();
            }

            x = lastX;
            y = lastY;

            lastX = kid.getX();
            lastY = kid.getY();
        }
        else {
            x = kid.getX();
            y = kid.getY();
        }
    }

    @Override
    public void render(Graphics g) {
        if (x + 4 > 0 && x < Main.gameWidth)
            if (y + 4 > 0 && y < Main.gameHeight) {
                if (kid.xScale == -1) {
                    g.drawImage(flipImage(image, true), (int) kid.getX() + kid.getBounds().getStartX() + 5, (int) kid.getY() + 8, null);
                } else {
                    g.drawImage(image, (int) kid.getX() + 7, (int) kid.getY() + 8, null);
                }
            }
    }

    private BufferedImage image;
    @Override
    public void initialize() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/sprites/kid/sprBow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Hitbox setBounds() {
        return new RectangleHitbox() {
            @Override
            public void initialize() {

            }

            @Override
            public IObject getOwner() {
                return Bow.this;
            }
        };
    }

    private boolean waitFrame() {
        return false;
    }
}