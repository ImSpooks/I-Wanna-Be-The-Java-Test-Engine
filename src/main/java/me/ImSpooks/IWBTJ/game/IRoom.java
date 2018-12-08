package me.ImSpooks.IWBTJ.game;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.IObject;
import me.ImSpooks.IWBTJ.object.gameobjects.blocks.Block;
import me.ImSpooks.IWBTJ.object.gameobjects.blocks.MiniBlock;
import me.ImSpooks.IWBTJ.object.gameobjects.blocks.SaveBlocker;
import me.ImSpooks.IWBTJ.object.gameobjects.killers.Cherry;
import me.ImSpooks.IWBTJ.object.gameobjects.killers.KillerBlock;
import me.ImSpooks.IWBTJ.object.gameobjects.killers.MiniSpike;
import me.ImSpooks.IWBTJ.object.gameobjects.killers.Spike;
import me.ImSpooks.IWBTJ.object.gameobjects.misc.JumpRefresher;
import me.ImSpooks.IWBTJ.object.gameobjects.misc.Wootar;
import me.ImSpooks.IWBTJ.object.gameobjects.saves.Save;
import me.ImSpooks.IWBTJ.object.interfaces.GameObject;
import me.ImSpooks.IWBTJ.save.SaveFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class IRoom extends IObject {

    private List<GameObject> objects;

    private int startX = 32, startY = 32;

    public IRoom(Handler handler, String resourcePath) {
        super(0, 0, ID.ROOM, handler);

        objects = new ArrayList<>();
        readJmapFile(resourcePath);
    }

    @Override
    public void tick() {
        tickObjects();
    }

    public abstract int getRoomNumber();

    private void readJmapFile(String resourcePath) {
        if (resourcePath == null || resourcePath.isEmpty() || !resourcePath.endsWith(".jmap"))
            return;

        int lastX = 0;
        int lastY = 0;
        int lastID = 0;

        System.out.println("Initializing room " + this.getClass().getName());
        long now = System.currentTimeMillis();

        List<Integer> ids = new ArrayList<>();

        try {
            //reading room from jmap file
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourcePath)));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("data repeated below for easy parsing by other tools")) {
                    reader.readLine();
                    line = reader.readLine();

                    String[] splittedString = line.split(" ");

                    int objects = 0;
                    while (splittedString.length >= objects && splittedString.length != objects) {
                        try {
                            int x = Integer.parseInt(splittedString[objects]);
                            int y = Integer.parseInt(splittedString[objects + 1]);
                            int id = Integer.parseInt(splittedString[objects + 2]);

                            lastX = x;
                            lastY = y;
                            lastID = id;
                            GameObject object = null;

                            if (!ids.contains(id))
                                ids.add(id);

                            if (id == 20) { // start
                                startX = x;
                                startY = y;
                            }

                            else if (id == 21) { // warp

                            }

                            else if (id == 22) { // jump refresher
                                object = new JumpRefresher(x, y, ID.MISC, handler);
                                object.setSprite(getSprites().getJumpRefresher());
                                object.setX(object.getX() - object.getSpriteWidth() / 2);
                                object.setY(object.getY() - object.getSpriteHeight() / 2);
                            }



                            else if (id == 1) { // block
                                object = new Block(x, y, ID.BLOCK, handler);
                                object.setSprite(getSprites().getBlockSprite());
                            }

                            else if (id == 2) { // mini block
                                object = new MiniBlock(x, y, ID.BLOCK, handler);
                                object.setSprite(getSprites().getMiniblockSprite());
                            }

                            else if (id == 19) { // save blocker
                                object = new SaveBlocker(x, y, ID.MISC, handler);
                                object.setSprite(getSprites().getSaveBlocker());
                            }


                            else if (id == 18) { // killer blocker blockers
                                object = new KillerBlock(x, y, ID.KILLER_BLOCK, handler);
                                object.setSprite(getSprites().getKillerblockSprite());
                            }

                            //spikes
                            else if (id >= 3 && id <= 10) {
                                ObjectDirection direction;

                                if (id == 3 || id == 7) { // up
                                    direction = ObjectDirection.UP;
                                }
                                else if (id == 4 || id == 8) { // right
                                    direction = ObjectDirection.RIGHT;
                                }
                                else if (id == 5 || id == 9) { // left
                                    direction = ObjectDirection.LEFT;
                                }
                                else  { // down
                                    direction = ObjectDirection.DOWN;
                                }

                                if (id >= 7) {
                                    object = new MiniSpike(x, y, ID.SPIKE, handler);
                                    object.setSprite(getSprites().getMinispikeSprite(direction));
                                }
                                else {
                                    object = new Spike(x, y, ID.SPIKE, handler);
                                    object.setSprite(getSprites().getSpikeSprite(direction));
                                }
                            }


                            else if (id == 11) { // apple
                                object = new Cherry(x, y, ID.SPIKE, handler);
                                object.setSprite(getSprites().getCherrySprite());
                                object.setX(object.getX() - object.getSpriteWidth() / 2);
                                object.setY(object.getY() - object.getSpriteHeight() / 2);
                            }

                            else if (id == 12) { // save point
                                object = new Save(x, y, ID.SPIKE, handler);
                                object.setSprite(getSprites().getSaveSprite(SaveFile.Difficulty.HARD, ObjectDirection.DOWN));
                            }



                            else if (id == 13) { // platforms

                            }
                            else if (id == 14) { // water
                                object = new Wootar(x, y, ID.WATER, handler, Wootar.WaterType.FULL_JUMP);
                                object.setSprite(getSprites().getWaterSprite());
                            }
                            else if (id == 15) { // water
                                object = new Wootar(x, y, ID.WATER, handler, Wootar.WaterType.NO_JUMP);
                                object.setSprite(getSprites().getWaterSprite());
                            }
                            else if (id == 23) { // double jump water
                                object = new Wootar(x, y, ID.WATER, handler, Wootar.WaterType.DOUBLE_JUMP);
                                object.setSprite(getSprites().getWaterSprite());
                            }

                            else if (id == 17) { // vine left

                            }

                            if (object != null)
                                this.objects.add(object);

                        } catch (NumberFormatException e) {
                            System.out.println("Something went wrong adding object at x = [" + splittedString[objects] + "], y = [" + splittedString[objects + 1] + "], id = [" + splittedString[objects + 2] + "]");
                            e.printStackTrace();
                        }
                        objects = objects + 3;
                    }
                    break;
                }
            }

            Collections.sort(ids);
            System.out.println(Arrays.toString(ids.toArray(new Integer[ids.size()])));

        } catch (Exception e) {
            System.out.println("Something went wrong adding object at x = [" + lastX + "], y = [" + lastY + "], id = [" + lastID + "]");
            e.printStackTrace();
        }
        System.out.println("Took " + (System.currentTimeMillis() - now) + " ms to initialize room " + this.getClass().getName());
    }

    public abstract void renderRoom(Graphics g);

    @Override
    public void render(Graphics g) {
        if (getSprites().getBackgroundSprite() == null || getSprites().getBackgroundSprite().isEmpty()){
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, Main.gameWidth, Main.gameHeight);

            g.setColor(new Color(0, 255, 255, 128).darker().darker());
            for (int i = 0; i < 20; i++) {
                g.drawLine(0, 32 * i, Main.gameWidth, 32 * i);
            }

            for (int i = 0; i < 30; i++) {
                g.drawLine(32 * i, 0, 32 * i, Main.gameHeight);
            }
        }
        else {
            try {
                BufferedImage image = ImageIO.read(getClass().getResourceAsStream(getSprites().getBackgroundSprite()));
                g.drawImage(image, 0, 0, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        renderRoom(g);
        renderObjects(g);
    }

    //public abstract GameObject addedObject();

    protected void tickObjects() {
        objects.forEach(gameObject -> gameObject.tick());
    }
    protected void renderObjects(Graphics g) {
        objects.forEach(gameObject -> gameObject.render(g));
    }

    @Override
    public void onRemove() {
        objects.clear();
        handler.getKid().bullets.forEach(kidBullet -> kidBullet.onRemove());
    }

    @Override
    public Hitbox setBounds() {
        return null;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public Sprites getSprites() {
        return new Sprites() {
            public String getBlockSprite() {
                return "/sprites/blocks/sprBlock.png";
            }

            public String getMiniblockSprite() {
                return "/sprites/blocks/sprMiniblock.png";
            }

            public String getSpikeSprite(ObjectDirection direction) {
                return "/sprites/killers/sprSpike" + direction.getInFileName() + ".png";
            }

            public String getMinispikeSprite(ObjectDirection direction) {
                return "/sprites/killers/sprMini" + direction.getInFileName() + ".png";
            }

            public String getCherrySprite() {
                return "/sprites/killers/sprCherry.png";
            }

            @Override
            public String getSaveSprite(SaveFile.Difficulty difficulty, ObjectDirection direction) {
                return "/sprites/saves/sprSave" + difficulty.getInternalName() + (direction == ObjectDirection.UP ? "Flip" : "") + ".png";
            }

            public String getBackgroundSprite() {
                return null;
            }

            @Override
            public String getWaterSprite() {
                return "/sprites/misc/sprWater.png";
            }

            @Override
            public String getKillerblockSprite() {
                return "/sprites/blocks/sprKillerBlock.png";
            }

            @Override
            public String getSaveBlocker() {
                return "/sprites/blocks/sprSaveBlocker.png";
            }

            @Override
            public String getWarpSprite() {
                return "/sprites/warps/sprWarp.png";
            }

            @Override
            public String getJumpRefresher() {
                return "/sprites/misc/sprJumpRefresher.png";
            }
        };
    }

    public abstract  class Sprites {
        public abstract String getBackgroundSprite();

        public abstract String getBlockSprite();
        public abstract String getMiniblockSprite();
        public abstract String getSpikeSprite(ObjectDirection direction);
        public abstract String getMinispikeSprite(ObjectDirection direction);
        public abstract String getCherrySprite();
        public abstract String getSaveSprite(SaveFile.Difficulty difficulty, ObjectDirection direction);
        public abstract String getWaterSprite();
        public abstract String getKillerblockSprite();
        public abstract String getSaveBlocker();
        public abstract String getWarpSprite();
        public abstract String getJumpRefresher();
    }



    public enum ObjectDirection {
        DOWN("Down"),
        LEFT("Left"),
        RIGHT("Right"),
        UP("Up"),;

        private final String inFileName;

        ObjectDirection(String inFileName) {
            this.inFileName = inFileName;
        }

        public String getInFileName() {
            return inFileName;
        }
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public List<IObject> getObjectsAt(int x, int y) {
        List<IObject> objects = new ArrayList<>();

        for (IObject allObject : getObjects()) {
            if (!allObject.getBounds().doesCollide(x, y))
                continue;
            objects.add(allObject);
        }

        return objects;
    }

    public List<IObject> getObjectInRadius(int x, int y, int radius, double maxDistance){
        List<IObject> objects = new ArrayList<>();

        int px = x - radius;
        int py = y - radius;
        int sx = x + radius;
        int sy = y + radius;

        for (IObject allObject : getObjects()) {
            if (handler.getMain().distance((px + sx) / 2, allObject.getX(), (py + sy) / 2, allObject.getY()) > maxDistance)
                continue;


            for (int xx = px; xx <= sx; xx++) {
                for (int yy = py; yy <= sy; yy++) {
                    if (allObject.getBounds().doesCollide(x, y)) {
                        objects.add(allObject);
                    }
                }
            }

            /*for (int x = px; x <= sx; x++) {
                for (int y = py; y <= sy; y++) {
                    for (int[] hitbox : allObject.getBounds().getPixels()) {
                        if (allObject.getX() + hitbox[0] == x && allObject.getY() + hitbox[1] == y) {
                            objects.add(allObject);
                        }
                    }
                }
            }*/
        }

        return objects;
    }

    public List<IObject> getObjectInRadius(IObject object, double maxDistance){
        return getObjectInRadius(object, maxDistance, false);
    }

    public List<IObject> getObjectInRadius(IObject object, double maxDistance, boolean calculateMiddlepoint){
        List<IObject> objects = new ArrayList<>();

        int width = 0;
        int height = 0;

        if (object.getBounds() != null) {
            width = object.getBounds().getWitdh();
            height = object.getBounds().getHeight();
        }

        int px = (int)object.getX();
        int py = (int)object.getY();
        int sx = (int)object.getX() + width;
        int sy = (int)object.getY() + height;

        if (object.getBounds() != null) {
            px = px + object.getBounds().getStartX();
            py = py + object.getBounds().getStartY();
        }

        for (IObject allObject : getObjects()) {
            if (calculateMiddlepoint) {
                if (handler.getMain().distance((px + sx) / 2, allObject.getX(), (py + sy) / 2, allObject.getY()) > maxDistance)
                    continue;
            }
            else {
                if (handler.getMain().distance(px, allObject.getX(), py, allObject.getY()) > maxDistance)
                    continue;
            }


            if (object.getBounds().doesCollide(allObject.getBounds())) {
                objects.add(allObject);
            }
        }
        return objects;
    }
}