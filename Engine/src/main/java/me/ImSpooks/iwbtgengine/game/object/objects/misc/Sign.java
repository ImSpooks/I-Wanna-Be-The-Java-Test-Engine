package me.ImSpooks.iwbtgengine.game.object.objects.misc;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.collision.Hitbox;
import me.ImSpooks.iwbtgengine.game.object.GameObject;
import me.ImSpooks.iwbtgengine.game.object.player.KidObject;
import me.ImSpooks.iwbtgengine.game.object.sprite.Sprite;
import me.ImSpooks.iwbtgengine.game.room.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick on 16 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Sign extends GameObject {

    @Getter private String text = null;

    @Getter @Setter private int fadeIn = 8;
    @Getter @Setter private int fadeOut = 12;

    @Getter @Setter private Color color = Color.BLACK;

    public Sign(Room parent, double x, double y, Sprite sprite) {
        super(parent, x, y, sprite);

        this.setHitbox(new Hitbox(Hitbox.HitboxType.SQUARE) {
            @Override
            public java.util.List<int[]> getPixels() {
                List<int[]> pixels = new ArrayList<>();

                for (int x = 0; x < sprite.getImage().getWidth(); x++) {
                    for (int y = 0; y < sprite.getImage().getHeight(); y++) {

                        // only adding outline to reduce lag

                        pixels.add(new int[]{x, y});
                    }
                }

                return pixels;
            }
        });
    }

    public void setText(String text) {
        this.text = text;
        this.lines.clear();
    }

    private Map<String, Integer> lines = new LinkedHashMap<>();

    private void calculate(Graphics graphics) {
        String[] words = text.split(" ");

        int textWidth = graphics.getFontMetrics().stringWidth(text);

        if (textWidth > 250) {
            int index = 0;

            while (index < words.length) {
                StringBuilder builder = new StringBuilder();

                try {
                    if (graphics.getFontMetrics().stringWidth(words[index]) > 250) {
                        builder.append(words[index++]);
                    }
                    else {
                        while (graphics.getFontMetrics().stringWidth(builder.toString()) + graphics.getFontMetrics().stringWidth(words[index]) < 250) {
                            builder.append(words[index++]).append(" ");
                        }
                    }

                    lines.put(builder.toString().trim(), graphics.getFontMetrics().stringWidth(builder.toString().trim()));
                } catch (ArrayIndexOutOfBoundsException e) {
                    lines.put(builder.toString().trim(), graphics.getFontMetrics().stringWidth(builder.toString().trim()));
                }
            }
        }
        else {
            lines.put(text, graphics.getFontMetrics().stringWidth(text));
        }
    }

    @Override
    public void render(Camera camera, Graphics graphics) {
        super.render(camera, graphics);

        if (canRender(camera)) {
            if (text == null || text.isEmpty()) {
                text = String.format("\'%s\': No string data set.", this.getCustomId());
            }

            int alpha = 0;


            if (visible) {
                ticksIn++;
                ticksOut = fadeOut;

                alpha = (int) (((double) ticksIn / (double) fadeIn) * 255.0);
            }
            else {
                ticksIn = 0;
                ticksOut--;

                alpha = (int) (((double) ticksOut / (double) fadeOut) * 255.0);
            }

            if (ticksIn == 0 && ticksOut <= 0)
                return;

            Color color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), getImageUtils().getColoringUtils().keep256(alpha));

            if (lines.isEmpty()) {
                this.calculate(graphics);
            }

            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(lines.entrySet());
            for (int i = 0; i < entryList.size(); i++) {
                Map.Entry<String, Integer> entry = entryList.get(i);

                this.getImageUtils().drawStringShadow(graphics, entry.getKey(), (int) x - (entry.getValue() / 2), (int) y - 16 * (lines.size() - i), color);
            }
        }
    }

    @Override
    public boolean update(float delta) {
        if (super.update(delta)) {
            KidObject kid = this.getParent().getHandler().getKid();
            if (kid == null)
                return false;

            visible = this.getHitbox().intersects(kid.getHitbox(), this.x, this.y, kid.getX(), kid.getY());

            return true;
        }
        return false;
    }

    private int ticksIn = 0;
    @Getter @Setter private boolean visible = false;
    private int ticksOut = 0;
}
