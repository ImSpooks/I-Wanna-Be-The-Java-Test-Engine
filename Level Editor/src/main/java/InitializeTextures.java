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
        The editor is maid with HTML and Javascript to save me some time, it is not optimal and does not automatically generate new sources.
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
            for (File file : this.getFilesInFolder(subFolder)) {
                String path = file.getPath();
                path = path.substring(resourceFolder.getPath().length() + 1).replace(File.separator, "/");

                Resource resource = new Resource(path, ResourceType.getFromType(path.split("/")[1]));

                if (resource.getResourceType() != null && resource.getResourceType().isEnabled())
                    resources.add(resource);
            }
        }

        System.out.println("resources = " + resources.size());

        this.generateJsonScript(resourcePath, resources);
    }

    private void generateJsonScript(String resourcePath, List<Resource> resources) {
        String editorPath = dir + File.separator + "Level Editor" + File.separator + "Editor";

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

            content.add("   " + fileName + ": {");

            content.add("       " + "path: \"" + resource.getPath() + "\",");
            content.add("       " + "type: \"" + resource.getResourceType().getType().toUpperCase() + "\",");
            content.add("       " + "subtype: \"" + resource.getResourceType().getSubtype().toUpperCase() + "\"");

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
            e.printStackTrace();
            System.out.println("Error while creating file");
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

    private List<File> getFilesInFolder(File folder) {
        List<File> subFiles = new ArrayList<>();

        for (File subFile : Objects.requireNonNull(folder.listFiles())) {
            if (!subFile.isDirectory()) {
                subFiles.add(subFile);
            }
        }

        return subFiles;
    }

    @RequiredArgsConstructor
    private static class Resource {

        @Getter private final String path;
        @Getter private final InitializeTextures.ResourceType resourceType;
    }

    @RequiredArgsConstructor
    public enum ResourceType {
        /*
            You can add a resource type manually, if a file is found but has a non existent type it will not be added.
         */


        BACKGROUNDS("backgrounds", "", false),

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
