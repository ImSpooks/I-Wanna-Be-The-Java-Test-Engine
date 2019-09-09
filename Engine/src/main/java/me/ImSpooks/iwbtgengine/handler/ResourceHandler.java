package me.ImSpooks.iwbtgengine.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFIcon;
import me.ImSpooks.iwbtgengine.sound.Sound;
import me.ImSpooks.iwbtgengine.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ResourceHandler {

    private final Main main;

    private Map<String, List<Object>> resources;


    public ResourceHandler(Main main) {
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
                            if (resourceFile.toLowerCase().endsWith("." + extension)) {
                                this.cacheResource(resourceFile, resourceType, extension);

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
            main.getLogger().log(Level.WARNING, "Resource {%s} was not initialized.", value);
            return null;
        }

        return clazz.cast(resources.get(value).get(0));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String value) {
        if (!resources.containsKey(value)) {
            main.getLogger().log(Level.WARNING, "Resource {%s} was not initialized.", value);
            return null;
        }

        List<Object> resource = resources.get(value);

        return((Class<T>) resource.get(1)).cast(resource.get(0));
    }

    public List<Object> getResource(String value) {
        return resources.get(value);
    }

    private void cacheResource(String resource, ResourceType type, String extension) throws IOException {
        String fileName = resource.split("/")[resource.split("/").length - 1].split("\\.")[0];

        switch (type) {
            case IMAGE: {
                resources.put(fileName, Arrays.asList(ImageIO.read(getClass().getResourceAsStream(resource)), type.getClazz()));
                break;
            }
            case GIF: {
                if (resource.startsWith("/"))
                    resource = resource.substring(1);

                GIFIcon icon = new GIFIcon();
                icon.setSource(resource);
                resources.put(fileName, Arrays.asList(icon, type.getClazz()));
                break;
            }
            case SOUND: {
                try {
                    Sound sound = (Sound) Class.forName(Sound.class.getPackage().getName() + "." + StringUtils.capitalize(extension) + "Sound").getConstructor(String.class, String.class).newInstance(fileName, resource);
                    this.resources.put(fileName, Arrays.asList(sound, type.getClazz()));
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @RequiredArgsConstructor
    public enum ResourceType {
        IMAGE(new String[] {"png", "jpg", "jpeg", "bmp"}, BufferedImage.class),
        GIF(new String[] {"gif"}, GIFIcon.class),
        TXT(new String[] {"txt"}, null),
        SOUND(new String[] {"wav", "brtsm"}, Sound.class),
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
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList<>();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            list.add(line);
        }

        return list;
    }

    private BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
    }
}
