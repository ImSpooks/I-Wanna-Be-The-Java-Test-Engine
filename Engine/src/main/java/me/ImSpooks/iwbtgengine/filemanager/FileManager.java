package me.ImSpooks.iwbtgengine.filemanager;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.Main;

import java.io.File;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class FileManager {

    @Getter private Main instance;

    private String gameFileName;

    @Getter private File directory;

    public FileManager(Main instance) {
        this.instance = instance;

        try {
            String filePath = this.instance.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            gameFileName = filePath.split("/")[filePath.split("/").length - 1];

            if (filePath.endsWith(".jar"))
                filePath = filePath.substring(0, filePath.length() - 4);


            filePath = filePath.substring(0, filePath.length() - filePath.split("/")[filePath.split("/").length - 1].length() - 1);

            this.directory = new File(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
