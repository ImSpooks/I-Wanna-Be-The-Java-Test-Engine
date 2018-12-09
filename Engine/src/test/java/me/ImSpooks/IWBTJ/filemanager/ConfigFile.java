package me.ImSpooks.IWBTJ.filemanager;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends IFile {
    public ConfigFile(File file, String fileName) {
        super(file, fileName);
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public void saveFile() {

    }

    @Override
    public void loadFile() throws IOException {
        Ini ini = new Ini(this.getFile());



    }

    @Override
    public FileManager.FileType getFileType() {
        return FileManager.FileType.INI;
    }
}
