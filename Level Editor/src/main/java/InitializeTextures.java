import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Nick on 02 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class InitializeTextures {

    /*
        Run this class to reinitialize all the resources in the resource folder for the editor to use.
        The editor is made with HTML and Javascript to save me some time, it is not optimal and does not automatically generate new sources.
        Therefore this java code will run to (re)initialize edited, new and removed textures.
     */

    private String dir = System.getProperty("user.dir");
    
    public static void main(String[] args) {
        new InitializeTextures();
    }

    private InitializeTextures() {
        String resourcePath = "Engine" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "resources";

        File resourceFolder = new File(dir + File.separator + resourcePath);

        List<Resource> resources = new ArrayList<>();

        for (File subFolder : this.getSubFolders(resourceFolder)) {
            for (ResourceFile file : this.getFilesInFolder(subFolder)) {
                String path = file.getFile().getPath();
                path = path.substring(resourceFolder.getPath().length() + 1).replace(File.separator, "/");

                try {
                    ResourceType type = ResourceType.getFromType(path.split("/")[0]);
                    if (!Objects.requireNonNull(type).isEnabled())
                        continue;
                } catch (IllegalArgumentException | NullPointerException ignored) {
                }

                ResourceType type = ResourceType.getFromType(path.split("/")[1]);

                if (!Objects.requireNonNull(type).isEnabled())
                    continue;

                Resource resource = new Resource(path, type, path.split("/")[2]);

                if (resource.getResourceType() != null && resource.getResourceType().isEnabled())
                    resources.add(resource);
            }
        }

        System.out.println("resources = " + resources.size());

        this.generateJsonScript(resourcePath, resources);
    }

    private void generateJsonScript(String resourcePath, List<Resource> resources) {
        String editorPath = dir + File.separator + "Level Editor" + File.separator + "Editor" + File.separator + "resources";

        File file = new File(editorPath, "resources.js");

        StringBuilder typeArray = new StringBuilder("[");
        for (int i = 0; i < ResourceType.CACHE.length; i++) {
            if (ResourceType.CACHE[i].isEnabled())
                typeArray.append("\"").append(ResourceType.CACHE[i]).append("\"").append(i + 1 < ResourceType.CACHE.length ? ", " : "");
        }
        typeArray.append("]");

        List<String> content = new ArrayList<>();

        content.add("var Types = " + typeArray + ";");
        content.add("var Resources = {");

        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);

            String fileName = resource.getPath().split("/")[resource.getPath().split("/").length - 1].split("\\.")[0];

            // what is this OMEGALUL
            // TODO convert to more an actual json instead of hardcoded strings
            content.add("   " + fileName + ": {");

            content.add("       " + "path: \"" + resource.getPath() + "\",");
            content.add("       " + "parent: \"" + resource.getResourceType().getType().toUpperCase() + "\",");
            content.add("       " + "type: \"" + resource.getResourceType().getSubtype().toUpperCase() + "\",");
            content.add("       " + "subtype: \"" + resource.getSubType().toLowerCase() + "\"");

            content.add("   " + "}" + (i + 1 < resources.size() ? "," : ""));
        }
        
        content.add("};");

        if (file.exists() && file.delete())
            System.out.println("Overwriting existing file");

        try {
            if (!file.getParentFile().exists() && file.getParentFile().mkdir()) {
                System.out.println("Created folder");
            }

            if (file.createNewFile()) {

                PrintWriter writer = new PrintWriter(file.getPath(), "UTF-8");
                for (String s : content) {
                    writer.println(s);
                }
                writer.close();
                
                System.out.println("File created");
            }

        } catch (IOException e) {
            System.out.println("Error while creating file");
            e.printStackTrace();
        }
    }

    private List<File> getSubFolders(File folder) {
        List<File> subFolders = new ArrayList<>();

        for (File subFolder : Objects.requireNonNull(folder.listFiles())) {
            if (subFolder.isDirectory()) {
                subFolders.add(subFolder);
                subFolders.addAll(this.getSubFolders(subFolder));
            }
        }

        return subFolders;
    }

    private List<ResourceFile> getFilesInFolder(File folder) {
        List<ResourceFile> subFiles = new ArrayList<>();

        for (File subFile : Objects.requireNonNull(folder.listFiles())) {
            if (!subFile.isDirectory()) {
                subFiles.add(new ResourceFile(subFile, subFile.getName()));
            }
        }

        subFiles.sort((a, b) -> {
            if (a.getFileName().replaceAll("[^0-9]+", "").isEmpty() || b.getFileName().replaceAll("[^0-9]+","").isEmpty())
                return a.getFileName().compareTo(b.getFileName());

            return Integer.parseInt(a.getFileName().replaceAll("[^0-9]+","")) - Integer.parseInt(b.getFileName().replaceAll("[^0-9]+",""));
        });

        return subFiles;
    }

    @RequiredArgsConstructor
    private static class Resource {

        @Getter private final String path;
        @Getter private final InitializeTextures.ResourceType resourceType;
        @Getter private final String subType;
    }

    @RequiredArgsConstructor
    private static class ResourceFile {
        @Getter private final File file;
        @Getter private final String fileName;
    }

    @RequiredArgsConstructor
    public enum ResourceType {
        /*
            You can add a resource type manually, if a file is found but has a non existent type it will not be added.
         */


        BACKGROUNDS("backgrounds", "backgrounds", false),
        FONT("font", "font", false),

        // Sounds
        MUSIC("sound", "music", false),
        SFX("sound", "sfx", false),

        // Sprites
        BLOCKS("sprites", "blocks", true),
        ITEMS("sprites", "items", true),
        KID("sprites", "kid", false),
        KILLERS("sprites", "killers", true),
        MISC("sprites", "misc", true),
        PLATFORMS("sprites", "platforms", true),
        SAVES("sprites", "saves", true),
        SLOPES("sprites", "slopes", true),
        UI("sprites", "ui", false),
        TRIGGERS("sprites", "triggers", true),
        WARPS("sprites", "warps", true),

        ;

        @Getter private final String type;
        @Getter private final String subtype;
        @Getter private final boolean enabled;

        private static final ResourceType[] CACHE = ResourceType.values();

        public static ResourceType getFromType(String folder) {
            for (ResourceType resourceType : CACHE) {
                if (resourceType.getSubtype().equalsIgnoreCase(folder)) {
                    return resourceType;
                }
            }
            return null;
        }
    }
}
