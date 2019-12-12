package me.ImSpooks.iwbtgengine.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.sprite.atlas.TextureAtlas;
import me.ImSpooks.iwbtgengine.game.object.sprite.gif.GifDecoder;
import me.ImSpooks.iwbtgengine.game.object.sprite.gif.GifIcon;
import me.ImSpooks.iwbtgengine.helpers.StringHelpers;
import me.ImSpooks.iwbtgengine.sound.Sound;
import org.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ResourceManager {

    private final Main main;

    private Map<String, CachedResource<?>> resources;


    public ResourceManager(Main main) {
        this.main = main;

        this.resources = new HashMap<>();
    }

    public void initialize() {
        try {
            String directory = "/resources/";

            resourcesLoop: for (String resourceFile : getResourceFiles(directory, this.getClass().getResource(directory))) {
                try {

                    for (ResourceType resourceType : ResourceType.CACHE) {
                        for (String extension : resourceType.getExtensions()) {
                            if (resourceFile.toLowerCase().endsWith("." + extension.toLowerCase())) {
                                this.cacheResource(resourceFile, resourceType, extension.toLowerCase());

                                continue resourcesLoop;
                            }
                        }
                    }

                } catch (Exception e) {
                    main.getLogger().log(Level.WARNING, String.format("Unable to load resource '%s', thrown exception: '%s'", resourceFile, e.getClass().getSimpleName()));
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T get(String value, Class<T> clazz) {
        if (!resources.containsKey(value)) {
            Logger.warn("Resource \"{}\" was not initialized.", value);
            return null;
        }

        return clazz.cast(resources.get(value).getObject());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String value) {
        if (!resources.containsKey(value)) {
            Logger.warn("Resource \"{}\" was not initialized.", value);
            return null;
        }

        CachedResource<?> resource = resources.get(value);

        return (T) resource.getResourceType().getClazz().cast(resource.getObject());
    }

    public CachedResource<?> getResource(String value) {
        return resources.get(value);
    }

    private void cacheResource(String resource, ResourceType type, String extension) throws IOException {
        String fileName = resource.split("/")[resource.split("/").length - 1].split("\\.")[0];

        switch (type) {
            case IMAGE: {
                if (this.getClass().getResourceAsStream(resource.replace(".png", ".atlas")) != null) {
                    return;
                }

                resources.put(resource, new CachedResource<>(ImageIO.read(this.getClass().getResourceAsStream(resource)), resource, type));
                break;
            }
            case GIF: {
                GifDecoder gifDecoder = new GifDecoder();
                gifDecoder.read(new BufferedInputStream(this.getClass().getResourceAsStream(resource)));

                GifIcon icon = new GifIcon();

                for (GifDecoder.GifFrame frame : gifDecoder.getFrames()) {
                    icon.getFrames().add(frame.image);
                    icon.getDelays().add(frame.delay);
                }

                resources.put(resource, new CachedResource<>(icon, resource, type));
                gifDecoder.clearCache();
                break;
            }
            case TEXTURE_ATLAS: {
                resources.put(resource, new CachedResource<>(new TextureAtlas(resource, true), resource, type));
                break;
            }
            case SOUND: {
                try {
                    Sound sound = (Sound) Class.forName(Sound.class.getPackage().getName() + "." + StringHelpers.capitalize(extension) + "Sound").getConstructor(String.class, String.class).newInstance(fileName, resource);
                    this.resources.put(resource, new CachedResource<>(sound, resource, type));
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    Logger.error(e, "Something went wrong while initializing sound {}", fileName);
                }

                break;
            }
            case FONT: {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

                try {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(resource)).deriveFont(24.0f);
                    ge.registerFont(font);
                    this.resources.put(resource, new CachedResource<>(font, resource, type));
                } catch (FontFormatException e) {
                    Logger.error(e, "Something went wrong while initializing font {}", fileName);
                }
            }
        }
    }

    @RequiredArgsConstructor
    public static class CachedResource<T> {
        @Getter private final T object;
        @Getter private final String resourcePath;
        @Getter private final ResourceType resourceType;
    }

    @RequiredArgsConstructor
    public enum ResourceType {
        IMAGE(new String[] {"png", "jpg", "jpeg", "bmp"}, BufferedImage.class),
        GIF(new String[] {"gif"}, GifIcon.class),
        TXT(new String[] {"txt"}, null),
        SOUND(new String[] {"wav", "brstm"}, Sound.class),
        FONT(new String[] {"ttf"}, Font.class),
        TEXTURE_ATLAS(new String[] {"atlas"}, TextureAtlas.class)
        ;

        public static final ResourceType[] CACHE = values();

        @Getter private final String[] extensions;
        @Getter private final Class clazz;
    }

    private List<String> getResourceFiles(String path, URL protocol) throws IOException {
        List<String> filenames = new ArrayList<>();

        if (protocol.getProtocol().equalsIgnoreCase("jar")) {
            path = path.substring(1);
            String internalPath = protocol.getPath();
            String jarPath = internalPath.substring(5, internalPath.indexOf("!"));

            try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = "/" + entry.getName();

                    if (name.startsWith("/" + path)) {
                        if (!name.contains(".")) {
                            filenames.add("");
                            continue;
                        }
                        filenames.add(name);
                    }

                }
            }
        }
        else {
            for (String file : readLines(this.getClass().getResourceAsStream(path))) {
                if (!file.contains(".")) {
                    filenames.addAll(this.getResourceFiles(path + file + "/", protocol));
                    continue;
                }
                filenames.add(path + file);
            }
        }

        return filenames;
    }

    private List<String> readLines(InputStream input) throws IOException {
        return readLines(new InputStreamReader(input, StandardCharsets.UTF_8));
    }

    private List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = input instanceof BufferedReader ? (BufferedReader) input : new BufferedReader(input);
        List<String> list = new ArrayList<>();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            list.add(line);
        }

        return list;
    }
}
