package me.ImSpooks.iwbtgengine.handler;

import me.ImSpooks.iwbtgengine.Main;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ResourceHandler {

    private final Main main;

    private Map<String, HashMap<String, ResourceType>> cache;
    private Map<String, Object> resources;

    public ResourceHandler(Main main) {
        this.main = main;

        this.cache = new HashMap<>();
        this.resources = new HashMap<>();
    }

    public void initialize() {
        // player

        this.add("player_default_idle", "/resources/sprites/kid/sprPlayerIdle.png", ResourceType.IMAGE);
        this.add("player_default_running", "/resources/sprites/kid/sprPlayerRunning.png", ResourceType.IMAGE);
        this.add("player_default_fall", "/resources/sprites/kid/sprPlayerFall.png", ResourceType.IMAGE);
        this.add("player_default_jump", "/resources/sprites/kid/sprPlayerJump.png", ResourceType.IMAGE);
        this.add("player_default_sliding", "/resources/sprites/kid/sprPlayerSliding.png", ResourceType.IMAGE);
        this.add("player_default_bullet", "/resources/sprites/kid/sprBullet.png", ResourceType.IMAGE);
        this.add("player_default_blood", "/resources/sprites/kid/sprBlood.png", ResourceType.IMAGE);
        this.add("player_default_bow", "/resources/sprites/kid/sprBow.png", ResourceType.IMAGE);

        // blocks
        this.add("block_texture_default", "/resources/sprites/blocks/sprBlock.png", ResourceType.IMAGE);
        this.add("block-mini_texture_default", "/resources/sprites/blocks/sprMiniblock.png", ResourceType.IMAGE);
    }

    public void add(String value, String path, ResourceType type) {
        cache.put(value, new HashMap<>());
        cache.get(value).put(path, type);
    }

    public <T> T get(String value, Class<T> clazz) {
        if (!resources.containsKey(value)) {
            if (cache == null) {
                main.getLogger().log(Level.WARNING, "Resource {%s} was not initialized.", value);
            }
            else {
                main.getLogger().log(Level.WARNING, "Unable to load resources.", value);
            }
            return null;
        }

        return clazz.cast(resources.get(value));
    }

    public void finishLoading() {
        for (Map.Entry<String, HashMap<String, ResourceType>> entry : cache.entrySet()) {
            for (Map.Entry<String, ResourceType> typeEntry : entry.getValue().entrySet()) {
                try {

                    String path = typeEntry.getKey();
                    ResourceType type = typeEntry.getValue();

                    switch (type) {
                        case IMAGE: {
                            resources.put(entry.getKey(), ImageIO.read(getClass().getResourceAsStream(path)));
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        cache = null;
    }

    public enum ResourceType {
        IMAGE,
        TXT,
        ;
    }
}
