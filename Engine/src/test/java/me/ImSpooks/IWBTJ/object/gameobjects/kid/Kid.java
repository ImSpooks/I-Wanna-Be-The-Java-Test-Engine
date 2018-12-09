package me.ImSpooks.IWBTJ.object.gameobjects.kid;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.hitbox.RectangleHitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.blocks.Block;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;
import me.ImSpooks.IWBTJ.object.interfaces.KidObject;
import me.ImSpooks.IWBTJ.settings.Global;
import me.ImSpooks.IWBTJ.utils.SomeValues;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Kid extends KidObject {

    public boolean onGround = false;

    private double maxSpeed = 3;
    private double maxVSpeed = 9;

    public Kid(double x, double y, ID id, Handler handler) {
        super(x, y, id, handler);
    }

    public long lastJump;
    public double jumpY;

    @Override
    public void tick() {
        super.tick();
        if (frozen || isDeath())
            return;


        /*if (velY != 0) { // debug message for jump heights and frame times
            if (velY == -jump || velY == -3.24) {
                jumpY = 0;
                lastJump = System.currentTimeMillis();
                System.out.println("jumped");
            }
            //System.out.println("y = " + y);
            if (jumpY > y) {
                jumpY = y;
            }
        }
        else {
            if (lastJump != -1) {
                System.out.println("jump took " + ((System.currentTimeMillis() - lastJump) / 20) + " frames");
                System.out.println("jumped " + (jumpY) + " pixels high");
                lastJump = -1;
                jumpY = 0;
            }
        }
        if (!onGround) {
            System.out.println("vspeed = " + velY);
        }//*/

        // adds 0.4 to the y velocity
        velY = velY + gravity;
        if (velY > maxVSpeed) {
            velY = maxVSpeed;
        }


        double lastX = x;
        innerloop: for (int i = 0; i < Math.abs(velX); i++) {
            if (Global.currentRoom != null) {

                for (IObject object : Global.currentRoom.getObjectInRadius(this, SomeValues.maxDistance(32, 32))) {
                    if (object.getId() == ID.BLOCK) {
                        x = x + xScale * 2;

                        if (object.getBounds().doesCollide(getBounds())) {
                            velX = 0;

                            x = lastX - xScale * 2;

                            break innerloop;
                        }

                        x = lastX - xScale * 2;
                    }
                }
            }
            this.x = x + (velX >= 0 ? 1 : -1);
            lastX = x;
        }

        List<GameObject> blocks = new ArrayList<>();
        if (Global.currentRoom != null) {
            int x = (int) Math.floor(this.x / 32) * 32 - 32;
            int y = (int) Math.floor(this.y / 32) * 32 - 32;
            for (GameObject object : Global.currentRoom.getObjects()) {
                if (!(object instanceof Block))
                    continue;
                for (int xx = 0; xx <= (32 * 2); xx++) {
                    for (int yy = 0; yy <= (32 * 3); yy++) {
                        if (object.getX() == x + xx && object.getY() == y + yy) {
                            blocks.add(object);
                        }
                    }
                }

            }
        }

        //velY = 0;
        innerloop: for (int i = 0; i < Math.ceil(Math.abs(velY)); i++) {
            if (Global.currentRoom != null) {
                if (velY < 0) {
                    onGround = false;
                    y = y - 1;
                    for (int[] tPixels : getBounds().getTopPixels()) {
                        for (IObject object : blocks) {
                            if (object.getId() == ID.BLOCK) {
                                if (object.getBounds().doesCollide((int)Math.floor(x) + tPixels[0] - xScale, (int)Math.floor(y) + tPixels[1])){
                                    velY = 0;
                                    break innerloop;
                                }
                            }
                        }
                    }
                    y = y + 1;
                }
                else if (velY > 0) {
                    onGround = false;
                    y = y + 2;
                    for (IObject object : blocks) {
                        if (object.getId() == ID.BLOCK && object.getBounds().doesCollide(getBounds())) {
                            onGround = true;
                            canJump = 2;
                            velY = 0;
                            y = y - 2;
                            break innerloop;
                        }
                    }
                    y = y - 2;
                }
            }
            this.y = y + (velY >= 0 ? 1 : -1);

            if (velY > 0 && canJump == 2)
                canJump = 1;
        }//*/

        //reset velX
        lastVelX = (int)velX;
        velX = 0;
    }

    private int lastVelX = 0;
    public int getLastVelX() {
        return lastVelX;
    }

    private int renderFrame = 0;
    private int renderTick = 0;

    @Override
    public Hitbox setBounds() { // hitbox
        return new RectangleHitbox() {
            @Override
            public void initialize() {
                Rectangle rectangle = new Rectangle(12, 11, 11, 21);//21

                for (int xx = 0; xx <= rectangle.width; xx++) {
                    for (int yy = 0; yy <= rectangle.height; yy++) {
                        addPixel(rectangle.getX() + xx, rectangle.getY() + yy);
                    }
                }
            }

            @Override
            public IObject getOwner() {
                return Kid.this;
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


        //System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());

        //TODO add sliding sprite
        // getting sprite
        if (this.velX == 0 && this.velY == 0) { // idle
            resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerIdle.png");
            image_speed = 0.2;
            frameLength = 3;
        } else if (this.velY != 0) {
            if (this.velY < 0) { // jumping
                resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerJump.png");
            } else { // falling
                resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerFall.png");
            }
            frameLength = 1;
            image_speed = 0.5;
        } else {
            frameLength = 3;
            resourcePath = getClass().getResourceAsStream("/sprites/kid/sprPlayerRunning.png");
            image_speed = 0.5;
        }

        try {
            image = ImageIO.read(resourcePath);

            if (image == null) {
                throw new NullPointerException("Render image can not be null");
            }

            int renderX = (int) x;

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

        /*if (Global.currentRoom != null) {
            int x = (int) Math.floor(this.x / 32) * 32 - 32;
            int y = (int) Math.floor(this.y / 32) * 32 - 32;
            for (GameObject object : Global.currentRoom.getObjects()) {
                if (!(object instanceof Block))
                    continue;
                for (int xx = 0; xx <= (32 * 3); xx++) {
                    for (int yy = 0; yy <= (32 * 3); yy++) {
                        if (object.getX() == x + xx && object.getY() == y + yy) {
                            object.renderHitbox(g);
                        }
                    }
                }
            }
        }*/
    }
}
