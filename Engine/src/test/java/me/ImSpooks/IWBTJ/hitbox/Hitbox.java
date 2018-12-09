package me.ImSpooks.IWBTJ.hitbox;

import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.object.IObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Hitbox {

    protected Hitbox() {
        initialize();
    }

    private List<int[]> pixels = new ArrayList<>();
;

    protected void addPixel(int x, int y) {
        pixels.add(new int[] {x, y});
    }
    protected void addPixel(double x, double y) {
        pixels.add(new int[] {(int)x, (int)y});
    }

    protected void removePixel(int x, int y) {
        pixels.remove(new int[] {x, y});
    }

    protected void clearPixels() {
        pixels.clear();
    }

    public abstract boolean doesCollide(Hitbox hitbox);
    public abstract boolean doesCollide(int x, int y);
    public abstract void initialize();
    public abstract IObject getOwner();

    public List<int[]> getPixels() {
        return pixels;
    }

    private int witdh = -Main.gameWidth;
    private int height = -Main.gameHeight;

    public int getWitdh() {
        if (witdh == -Main.gameWidth) {
            int lx = -1;
            int hx = -1;

            for (int[] pixels : pixels) {// find x
                if (lx == -1 || pixels[0] < lx) {
                    lx = pixels[0];
                }
                if (hx == -1 || pixels[0] > hx) {
                    hx = pixels[0];
                }
            }

            witdh = hx - lx;
        }
        return witdh;
    }

    public int getHeight() {
        if (height == -Main.gameHeight) {
            int ly = -1;
            int hy = -1;

            for (int[] pixels : pixels) {// find y
                if (ly == -1 || pixels[1] < ly) {
                    ly = pixels[1];
                }
                if (hy == -1 || pixels[1] > hy) {
                    hy = pixels[1];
                }
            }

            height = hy - ly;
        }
        return height;
    }

    private int startX = -Main.gameWidth;
    private int startY = -Main.gameHeight;

    public int getStartX() {
        if (startX == -Main.gameWidth) {
            startX = pixels.get(0)[0];
        }
        return startX;
    }

    public int getStartY() {
        if (startY == -Main.gameHeight) {
            startY = pixels.get(0)[1];
        }
        return startY;
    }

    private List<int[]> topPixels = new ArrayList<>();
    private List<int[]> bottomPixels = new ArrayList<>();
    private List<int[]> leftPixels = new ArrayList<>();
    private List<int[]> rightPixels = new ArrayList<>();
    public List<int[]> getTopPixels() {
        if (topPixels.isEmpty()) {
            int sy = -1;
            for (int[] pixels : pixels) {// find x
                if (sy == -1 || pixels[1] < sy) {
                    sy = pixels[1];
                }
            }

            for (int[] pixels : pixels) {// add pixels
                if (pixels[1] == sy) {
                    topPixels.add(pixels);
                }
            }
        }
        return topPixels;
    }

    public List<int[]> getBottomPixels() {
        if (bottomPixels.isEmpty()) {
            int sy = -1;
            for (int[] pixels : pixels) {// find x
                if (sy == -1 || pixels[1] > sy) {
                    sy = pixels[1];
                }
            }

            for (int[] pixels : pixels) {// add pixels
                if (pixels[1] == sy) {
                    bottomPixels.add(pixels);
                }
            }
        }
        return bottomPixels;
    }

    public List<int[]> getLeftPixels() {
        if (leftPixels.isEmpty()) {
            int sx = -1;
            for (int[] pixels : pixels) {// find x
                if (sx == -1 || pixels[0] < sx) {
                    sx = pixels[0];
                }
            }

            for (int[] pixels : pixels) {// add pixels
                if (pixels[0] == sx) {
                    leftPixels.add(pixels);
                }
            }
        }
        return leftPixels;
    }

    public List<int[]> getRightPixels() {
        if (rightPixels.isEmpty()) {
            int sx = -1;
            for (int[] pixels : pixels) {// find x
                if (sx == -1 || pixels[0] > sx) {
                    sx = pixels[0];
                }
            }

            for (int[] pixels : pixels) {// add pixels
                if (pixels[0] == sx) {
                    rightPixels.add(pixels);
                }
            }
        }
        return rightPixels;
    }

    public void clear() {
        pixels.clear();
    }
}