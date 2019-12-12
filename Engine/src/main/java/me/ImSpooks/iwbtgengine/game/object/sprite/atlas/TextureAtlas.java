package me.ImSpooks.iwbtgengine.game.object.sprite.atlas;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.game.object.sprite.AtlasSprite;
import org.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 13 nov. 2019.
 * Copyright © ImSpooks
 */
public class TextureAtlas {

    @Getter private final String path;
    @Getter private BufferedImage fullImage;
    @Getter private final boolean resource;

    @Getter private final Map<String, BufferedImage> subImages = new HashMap<>();
    @Getter private final Map<String, AtlasSprite> subSprites = new HashMap<>();

    public TextureAtlas(String path, boolean resource) {
        this.path = path;
        this.resource = resource;

        this.read();
    }

    public void read() {
        BufferedReader reader = null;
        try {
            if (this.resource) {
                this.fullImage = ImageIO.read(this.getClass().getResourceAsStream(path.replace(".atlas", ".png")));
                reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(this.path)));
            }
            else {
                this.fullImage = ImageIO.read(new File(path.replace(".atlas", ".png")));
                reader = new BufferedReader(new FileReader(new File(path)));
            }
        } catch (IOException e) {
            Logger.error(e);
            this.fullImage = null;
        }

        if (this.fullImage != null) {
            try {
                String line = reader.readLine();

                String lastLine = "";
                boolean readingSubImage = false;
                boolean nameAdded = false;

                Map<String, String[]> data = new HashMap<>();

                while (line != null) {
                    if (line.startsWith("  ")) {
                        readingSubImage = true;
                        if (!nameAdded) {
                            nameAdded = true;
                            data.put("name", new String[]{lastLine});
                        }

                        line = line.trim();
                        String[] split = line.split(": ");
                        data.put(split[0], split[1].split(", "));
                    }
                    else {
                        if (readingSubImage) {
                            try {
                                int[] xy = new int[]{Integer.parseInt(data.get("xy")[0]), Integer.parseInt(data.get("xy")[1])};
                                int[] size = new int[]{Integer.parseInt(data.get("size")[0]), Integer.parseInt(data.get("size")[1])};
                                int index = Integer.parseInt(data.get("index")[0]);

                                String name = data.get("name")[0];
                                BufferedImage image = fullImage.getSubimage(xy[0], xy[1], size[0], size[1]);

                                subSprites.putIfAbsent(name, new AtlasSprite());
                                subSprites.get(name).getRenderedImages().add(image);

                                if (index != -1)
                                    name += "_" + index;

                                subImages.put(name, image);
                            } catch (NumberFormatException e) {
                                Logger.warn(e, "Cannot create altas texture for texture {}", data.get("name")[0]);
                            }
                        }
                        readingSubImage = false;
                        nameAdded = false;
                        data.clear();
                    }

                    lastLine = line;
                    line = reader.readLine();
                }
                // adding last entry
                try {
                    int[] xy = new int[]{Integer.parseInt(data.get("xy")[0]), Integer.parseInt(data.get("xy")[1])};
                    int[] size = new int[]{Integer.parseInt(data.get("size")[0]), Integer.parseInt(data.get("size")[1])};
                    int index = Integer.parseInt(data.get("index")[0]);

                    String name = data.get("name")[0];
                    BufferedImage image = fullImage.getSubimage(xy[0], xy[1], size[0], size[1]);

                    subSprites.putIfAbsent(name, new AtlasSprite());
                    subSprites.get(name).getRenderedImages().add(image);

                    if (index != -1)
                        name += "_" + index;

                    subImages.put(name, image);
                } catch (NumberFormatException e) {
                    Logger.warn(e, "Cannot create altas texture for texture {}", data.get("name")[0]);
                }
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    public AtlasSprite getSprite(String sprite) {
        return this.subSprites.get(sprite);
    }

    public BufferedImage getSubSprite(String sprite) {
        BufferedImage image = this.subImages.get(sprite);
        if (image == null) image = this.getSprite(sprite).getKeyFrame(0);
        return image;
    }

    public BufferedImage getSubSprite(String sprite, int index) {
        BufferedImage image = this.subImages.get(sprite + "_" + index);
        if (image == null) image = this.getSprite(sprite).getKeyFrame(index);
        return image;
    }
}