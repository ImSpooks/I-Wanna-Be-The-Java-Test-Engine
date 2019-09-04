package me.ImSpooks.iwbtgengine.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFIcon;
import me.ImSpooks.iwbtgengine.sound.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
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
            resourcesLoop: for (String resourceFile : getResourceFiles("resources/")) {

                for (ResourceType resourceType : ResourceType.CACHE) {
                    for (String extension : resourceType.getExtensions()) {
                        if (resourceFile.endsWith("." + extension)) {
                            this.cacheResource("/" + resourceFile, resourceType);

                            continue resourcesLoop;
                        }
                    }
                }
            }
        } catch (Exception e) {
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

    private void cacheResource(String resource, ResourceType type) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
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
                System.out.println("resource = " + resource);
                AudioInputStream stream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(resource)));

                // load the sound into memory (a Clip)

                Clip clip = AudioSystem.getClip();
                clip.open(stream);

                Sound sound = new Sound(fileName, clip);

                this.resources.put(fileName, Arrays.asList(sound, type.getClazz()));

                System.out.println("Loaded sfx " + fileName);
                break;
            }
        }
    }

    @RequiredArgsConstructor
    public enum ResourceType {
        IMAGE(new String[] {"png", "jpg", "jpeg", "bmp"}, BufferedImage.class),
        GIF(new String[] {"gif"}, GIFIcon.class),
        TXT(new String[] {"txt"}, null),
        SOUND(new String[] {"wav"}, Sound.class);
        ;

        public static ResourceType[] CACHE = values();

        @Getter private final String[] extensions;
        @Getter private final Class clazz;
    }

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                if (!resource.contains(".")) {
                    filenames.addAll(getResourceFiles(path + resource + "/"));
                    continue;
                }
                filenames.add(path + resource);
            }
        }

        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
