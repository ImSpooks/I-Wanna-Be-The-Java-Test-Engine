package me.ImSpooks.IWBTJ.object.interfaces;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class GameObject extends IObject {

    public GameObject(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    private int renderTick = 0;
    private int renderFrame = 0;

    private BufferedImage fullSprite = null;
    private BufferedImage renderedSprite = null;
    private String spriteResourcepath = "";


    @Override
    public void render(Graphics g) {
        drawImage(g, getSprite(), this.x, this.y, renderFrame, getSpriteWidth(), getSpriteHeight());

        /*int sx = (renderFrame%20) * getSpriteWidth();
        int sy = getSpriteHeight() * (renderFrame / 20);

        this.renderedSprite = getSprite().getSubimage(sx, sy, sx + getSpriteWidth(), sy + getSpriteHeight());*/

        if (getFrameLength() > 0 && getImageSpeed() > 0) {
            renderTick++;
            if (renderTick >= 1.0 / getImageSpeed()) {
                renderTick = 0;
                renderFrame++;

                if (this instanceof MultipleSprites) {
                    ((MultipleSprites)this).onSpriteChange();
                }
            }

            if (renderFrame > getFrameLength())
                renderFrame = 0;
        }
    }

    public abstract int getSpriteWidth();
    public abstract int getSpriteHeight();
    public abstract int getFrameLength();
    public abstract double getImageSpeed();


    public BufferedImage getCurrentSprite() {
        if (renderedSprite == null)
            renderedSprite = fullSprite;

        return renderedSprite;
    }

    public BufferedImage getSprite() {
        return fullSprite;
    }

    public BufferedImage setSprite(String resourcePath) {
        try {
            this.spriteResourcepath = resourcePath;
            return fullSprite = ImageIO.read(getClass().getResourceAsStream(resourcePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSpriteResourcepath() {
        return spriteResourcepath;
    }
}
