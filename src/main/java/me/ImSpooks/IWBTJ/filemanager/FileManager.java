package me.ImSpooks.IWBTJ.filemanager;

import me.ImSpooks.IWBTJ.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public String gameFileName;
    private Main main;
    private File mainDirectory;
    private List<IFile> files;

    public FileManager(Main game) {
        this.files = new ArrayList<>();
        this.main = game;

        try {
            String filePath = this.main.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            gameFileName = filePath.split("/")[filePath.split("/").length - 1];

            if (filePath.endsWith(".jar"))
               filePath = filePath.substring(0, filePath.length() - 4);


            filePath = filePath.substring(0, filePath.length() - filePath.split("/")[filePath.split("/").length - 1].length() - 1);

            this.mainDirectory = new File(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < 3; i++) {
            files.add(new SaveData(getFile(getSubDirectory("Data"), "saveData" + i), "saveData" + i));
        }
    }

    public void loadFiles() {
        files.stream().forEach(file -> {
            try {
                if (!file.getFile().exists() && file.createIfNull) {
                    file.getFile().mkdir();
                    file.saveFile();
                }
                file.loadFile();
            } catch (IOException e) {
                System.out.println("Something went wrong while creating file " + file.getFileName());
                e.printStackTrace();
            }
        });
    }

    public File getSubDirectory(String subName) {
        return new File(this.mainDirectory.toString() + File.separator + subName.replace("/", File.separator));
    }

    public File getFile(File directory, String fileName, FileType fileType) {
        return getFile(directory, fileName + fileType.getFileType());
    }

    public File getMainDirectory() {
        return mainDirectory;
    }

    public File getFile(File directory, String fileName) {
        return new File(directory, fileName);
    }

    public enum FileType {
        TEXT_FILE(".txt"),
        CONFIGURATION_FILE(".ini"),

        TXT(TEXT_FILE),
        INI(CONFIGURATION_FILE),

        SAVE(".sav"),

        UNKNOWN(""),
        ;

        private final String fileType;

        FileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileType() {
            return fileType;
        }


        FileType(FileType fileType) {
            this.fileType = fileType.fileType;
        }
    }
}