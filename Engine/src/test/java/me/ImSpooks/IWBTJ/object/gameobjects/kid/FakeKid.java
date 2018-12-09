package me.ImSpooks.IWBTJ.object.gameobjects.kid;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class FakeKid extends GameObject {

    public int xScale = 1;

    public FakeKid(int x, int y, ID id, Handler handler) {
        super(x, y, id, handler);
        velY = -1;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void tick() {
        //velX = new Random().nextInt((1 + 1) + 1) - 1;
        //velY = new Random().nextInt((1 + 1) + 1) - 1;
    }

    private int renderFrame = 0;
    private int renderTick = 0;

    @Override
    public Hitbox setBounds() { // hitbox
        return new RectangleHitbox() {
            @Override
            public void initialize() {
            }

            @Override
            public IObject getOwner() {
                return FakeKid.this;
            }
        };
        //return new Rectangle((int)(this.x + 12), (int)(this.y + 11), 11, 21);
    }

    @Override
    public void render(Graphics g) {
        BufferedImage image;
        InputStream resourcePath;

        double image_speed;
        int frameLength;


        //TODO add sliding sprite
        // getting sprite
        if (this.velX == 0 && this.velY == 0) { // idle
            resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerIdle.png");
            image_speed = 0.2;
            frameLength = 3;
        }
        else if (this.velY != 0) {
            if (this.velY < 0) { // jumping
                resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerJump.png");
            }
            else { // falling
                resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerFall.png");
            }
            frameLength = 1;
            image_speed = 0.5;
        }
        else {
            frameLength = 3;
            resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerRunning.png");
            image_speed = 0.5;
        }

        try {
            image = ImageIO.read(resourcePath);

            if (image == null) {
                throw new NullPointerException("Render image can not be null");
            }

            int renderX = (int)x;

            if (this.xScale == -1) {
                renderX = renderX + 3;
            }

            //rendering sprite
            drawImage(g, xScale == -1 ? flipImage(image, true) : image, renderX, this.y, renderFrame, 32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderTick++;
        if (renderTick >= 1.0 / image_speed) {
            renderTick = 0;
            renderFrame++;
        }

        if (renderFrame > frameLength)
            renderFrame = 0;
    }

    @Override
    public int getSpriteWidth() {
        return 0;
    }

    @Override
    public int getSpriteHeight() {
        return 0;
    }

    @Override
    public int getFrameLength() {
        return 0;
    }

    @Override
    public double getImageSpeed() {
        return 0;
    }
}
