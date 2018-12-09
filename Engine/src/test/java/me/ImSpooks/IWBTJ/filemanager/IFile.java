package me.ImSpooks.IWBTJ.filemanager;

import java.io.File;
import java.io.IOException;

public abstract class IFile {
    public File file;
    public String fileName;
    public boolean createIfNull = true;

    public IFile(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public File getDirectory() {
        String[] splitted = this.getFile().toString().split(File.separator);

        return new File(this.getFile().toString().substring(0, this.getFile().toString().length() - getFileName().length()));
    }
    public String getFileName() {
        return fileName;
    }

    public abstract void saveFile() throws IOException;
    public abstract void loadFile() throws IOException;
    public abstract FileManager.FileType getFileType();
}
