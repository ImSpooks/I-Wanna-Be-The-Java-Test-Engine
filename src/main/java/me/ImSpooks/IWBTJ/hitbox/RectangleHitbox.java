package me.ImSpooks.IWBTJ.hitbox;

import java.awt.*;

public abstract class RectangleHitbox extends Hitbox {

    public boolean doesCollide(Hitbox hitbox) {
        if (hitbox instanceof RectangleHitbox) {
            Rectangle rectangle1 = new Rectangle((int)getOwner().getX() + getStartX(), (int)getOwner().getY() + getStartY(), getWitdh(), getHeight());
            Rectangle rectangle2 = new Rectangle((int)hitbox.getOwner().getX() + hitbox.getStartX(), (int)hitbox.getOwner().getY() + hitbox.getStartY(), hitbox.getWitdh(), hitbox.getHeight());
            return rectangle1.intersects(rectangle2);
        }
        else {
            for (int[] pixels1 : getPixels()) {
                for (int[] pixels2 : hitbox.getPixels()) {
                    if (getOwner().getX() + pixels1[0] == hitbox.getOwner().getX() + pixels2[0] && getOwner().getY() + pixels1[1] == hitbox.getOwner().getY() + pixels2[1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean doesCollide(int x, int y) {
        for (int[] pixels1 : getPixels()) {
            if (getOwner().getX() + pixels1[0] == x && getOwner().getY() + pixels1[1] == y) {
                //System.out.println((getOwner().getX() + pixels1[0]) + " == " + x + " && " + (getOwner().getY() + pixels1[1]) + " == " + y);
                return true;
            }
        }
        return false;
    }
}
