package me.ImSpooks.IWBTJ.object;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.hitbox.CustomHitbox;
import me.ImSpooks.IWBTJ.hitbox.Hitbox;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public abstract class IObject {

    protected ID id;
    protected double x,y;
    protected double velX, velY;
    protected Handler handler;

    public IObject(double x, double y, ID id, Handler handler){
        this.x = x;
        this.y = y;
        this.id = id;
        this.handler = handler;
    }

    public void removeObject() {
        Main.getInstance().getHandler().removeObject(this);
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    private Hitbox hitbox = null;
    public abstract Hitbox setBounds();
    public Hitbox getBounds() { // cache hitbox for better perfomance
        if (hitbox == null)
            hitbox = setBounds();
        return hitbox;
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public void setId(ID id){
        this.id = id;
    }
    public ID getId(){
        return this.id;
    }
    public void setVelX(double velX){
        this.velX = velX;
    }
    public void setVelY(double velY){
        this.velY = velY;
    }
    public double getVelX(){
        return this.velX;
    }
    public double getVelY(){
        return this.velY;
    }

    public void onRemove() {}

    protected void drawImage(Graphics g, BufferedImage sourceImage, double x, double y, int imageNumber, int tileWidth, int tileHeight) {
        int px = (int)((x));
        int py = (int)((Math.floor(y)));
        int sx = (imageNumber%20) * tileWidth;
        int sy = tileHeight * (imageNumber/20);
        g.drawImage(sourceImage, px, py, px + tileWidth,
                py + tileHeight, sx, sy, sx + tileWidth, sy + tileHeight,
                null);
    }

    protected BufferedImage flipImage(BufferedImage image, boolean horizontally) {
        AffineTransform tx;
        AffineTransformOp op;

        if (horizontally) { // flip horizontally
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
        }
        else { // flip vertically
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
        }
        return image;
    }

    public void initialize() {}

    public void renderHitbox(Graphics g) {
        if (getBounds() != null) {
            g.setColor(new Color(255, 35, 220, 176));
            if (getBounds() instanceof CustomHitbox) {

                int[] prevPixels = null;
                for (int[] pixels : getBounds().getPixels()) {
                    if (prevPixels != null && pixels[0] == prevPixels[0] && pixels[1] == prevPixels[1])
                        continue;
                    g.fillRect(pixels[0] + (int) x, pixels[1] + (int) y, 1 - getBounds().getStartX(), 1 - getBounds().getStartY());
                    prevPixels = pixels;
                }
            }
            else {
                g.fillRect(getBounds().getPixels().get(0)[0] + (int) x, getBounds().getPixels().get(0)[1] + (int) y, getBounds().getPixels().get(getBounds().getPixels().size() - 1)[0] - getBounds().getStartX(), getBounds().getPixels().get(getBounds().getPixels().size() - 1)[1] - getBounds().getStartY());
            }
        }
    }

    protected void drawStringShadow(Graphics g, String string, int x, int y) {
        Color prevColor = g.getColor();
        g.drawString(string, x, y);
        g.setColor(new Color(prevColor.getRed(), prevColor.getGreen(), prevColor.getBlue(), prevColor.getAlpha() / 3));
        g.drawString(string, x + 2, y + 1);
        g.setColor(prevColor);
    }

    protected BufferedImage cloneimage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public BufferedImage scaleImage(BufferedImage image, double scale) {
        BufferedImage before = image;
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return scaleOp.filter(before, after);
    }
}