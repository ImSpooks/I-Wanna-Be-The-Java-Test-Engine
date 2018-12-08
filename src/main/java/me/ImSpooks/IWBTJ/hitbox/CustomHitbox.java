package me.ImSpooks.IWBTJ.hitbox;


public abstract class CustomHitbox extends Hitbox {

    public boolean doesCollide(Hitbox hitbox) {
        for (int[] pixels1 : getPixels()) {
            for (int[] pixels2 : hitbox.getPixels()) {
                if (getOwner().getX() + pixels1[0] == hitbox.getOwner().getX() + pixels2[0] && getOwner().getY() + pixels1[1] == hitbox.getOwner().getY() + pixels2[1]) {
                    return true;
                }

                /*if (hitbox.getOwner() instanceof Kid) {
                    if (getOwner().getX() + pixels1[0] == hitbox.getOwner().getX() - ((Kid) hitbox.getOwner()).xScale + pixels2[0] && getOwner().getY() + pixels1[1] == hitbox.getOwner().getY() - 1 + pixels2[1]) {
                        return true;
                    }
                }
                else {
                    if (getOwner().getX() + pixels1[0] == hitbox.getOwner().getX() + pixels2[0] && getOwner().getY() + pixels1[1] == hitbox.getOwner().getY() + pixels2[1]) {
                        return true;
                    }
                }*/
            }
        }
        return false;
    }

    public boolean doesCollide(int x, int y) {
        for (int[] pixels1 : getPixels()) {
            if (getOwner().getX() + pixels1[0] == x && getOwner().getY() + pixels1[1] == y) {
                return true;
            }
        }
        return false;
    }
}
