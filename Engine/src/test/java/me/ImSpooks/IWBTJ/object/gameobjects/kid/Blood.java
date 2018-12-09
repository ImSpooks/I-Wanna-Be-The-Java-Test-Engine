package me.ImSpooks.IWBTJ.object.gameobjects.kid;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.hitbox.CustomHitbox;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObjects;
import me.ImSpooks.IWBTJ.settings.Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Blood extends KidObjects {

    public BufferedImage renderedSprite = null;

    private KidObject kidObject;

    public BufferedImage setSprite(String resourcePath) {
        try {
            return renderedSprite = ImageIO.read(getClass().getResourceAsStream(resourcePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Blood(double x, double y, ID id, Handler handler, KidObject kidObject) {
        super(x, y, id, handler);
        this.kidObject = kidObject;
    }

    boolean onBlock = false;
    @Override
    public void tick() {
        x = x + velX;
        y = y + velY;

        velX = velX - (velX * 0.01);
        velY = velY + (0.1 * Global.gravity);

        if (velY > 9 * Global.gravity)
            velY = 9;
    }

    @Override
    public void render(Graphics g) {
        if (x + 4 > 0 && x < Main.gameWidth)
            if (y + 4 > 0 && y < Main.gameHeight)
                drawImage(g, renderedSprite, this.x, this.y, 0, 4, 4);
    }

    @Override
    public Hitbox setBounds() {
        return new CustomHitbox() {
            @Override
            public void initialize() {
                BufferedImage image = renderedSprite;

                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {

                        if ((image.getRGB(x,y) >>24) == 0x00)
                            continue;

                        addPixel(x, y);
                    }
                }
            }

            @Override
            public IObject getOwner() {
                return Blood.this;
            }
        };
    }
}
