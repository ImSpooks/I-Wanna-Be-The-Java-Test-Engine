package me.ImSpooks.iwbtgengine.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ImSpooks.iwbtgengine.Main;
import me.ImSpooks.iwbtgengine.game.object.sprite.GIFIcon;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ResourceHandler {

    private final Main main;

    private Map<String, Object> resources;


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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T get(String value, Class<T> clazz) {
        if (!resources.containsKey(value)) {
            main.getLogger().log(Level.WARNING, "Resource {%s} was not initialized.", value);
            return null;
        }

        return clazz.cast(resources.get(value));
    }

    private void cacheResource(String resource, ResourceType type) throws IOException{
        String fileName = resource.split("/")[resource.split("/").length - 1].split("\\.")[0];

        switch (type) {
            case IMAGE: {
                resources.put(fileName, ImageIO.read(getClass().getResourceAsStream(resource)));
                break;
            }
            case GIF: {
                if (resource.startsWith("/"))
                    resource = resource.substring(1);

                GIFIcon icon = new GIFIcon();
                icon.setSource(resource);
                resources.put(fileName, icon);
                break;
            }
        }
    }

    @RequiredArgsConstructor
    public enum ResourceType {
        IMAGE(new String[] {"png", "jpg", "jpeg", "bmp"}),
        GIF(new String[] {"gif"}),
        TXT(new String[] {"txt"}),
        ;

        public static ResourceType[] CACHE = values();

        @Getter private final String[] extensions;
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
