package me.ImSpooks.IWBTJ.object.interfaces;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.object.ID;

public abstract class KillerObject extends GameObject {

    public KillerObject(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }
}
