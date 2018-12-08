package me.ImSpooks.IWBTJ.object.gui;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;

public abstract class IGui extends IObject{

    public IGui(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }
}
