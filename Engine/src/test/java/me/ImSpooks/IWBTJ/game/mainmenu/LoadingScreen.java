package me.ImSpooks.IWBTJ.game.mainmenu;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.RenderManager;
import me.ImSpooks.IWBTJ.game.IRoom;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;

public class LoadingScreen extends IRoom {

    public LoadingScreen(Handler handler) {
        super(handler, null);
    }

    public void initialize() {
        try { // force render frame
            Method method = RenderManager.class.getDeclaredMethod("render");
            method.setAccessible(true);
            method.invoke(handler.getMain().renderManager);
            method.invoke(handler.getMain().renderManager); // render twice because 1 value is null
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/sprites/loadingScreen.png"));
            g.drawImage(image, 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderRoom(Graphics g) {

    }

    @Override
    public int getRoomNumber() {
        return 0;
    }

    @Override
    public void onRemove() {
    }
}
