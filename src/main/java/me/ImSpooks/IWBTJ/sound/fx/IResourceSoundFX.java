package me.ImSpooks.IWBTJ.sound.fx;


import me.ImSpooks.IWBTJ.sound.IResourceFile;

import java.io.File;
import java.io.InputStream;

public class IResourceSoundFX extends ISoundFX implements IResourceFile {

    private InputStream stream;
    private String resourcePath;
    private String fileName;

    public IResourceSoundFX(String fileName, String resourcePath) {
        super(fileName);
        this.resourcePath = resourcePath;
        this.stream = getClass().getResourceAsStream(resourcePath);
        this.fileName = fileName;
    }

    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getStream() {
        return stream;
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
