package me.ImSpooks.IWBTJ.sound.fx;


import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.sound.ISound;

import java.io.File;

public class ISoundFX implements ISound {

    public String fileName;

    public ISoundFX(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public File getFile() {
        return new File(Main.getInstance().getFileManager().getSubDirectory("sound"), this.getInternalFileName());
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String getInternalFileName() {
        return this.getType().getPathName() + this.fileName;
    }

    @Override
    public SoundType getType() {
        return SoundType.FX;
    }
}
