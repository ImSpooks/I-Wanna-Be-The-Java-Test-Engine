package me.ImSpooks.IWBTJ.sound;

import java.io.File;

public interface ISound {

    File getFile();
    String getFileName();
    String getInternalFileName();
    SoundType getType();

    enum SoundType {
        FX(0, "FX", "FX", "fx_"),
        MUSIC(1, "Music", "Music", "m_"),
        ;

        final int id;
        final String name, prefix, internalPrefix, pathName;

        SoundType(int id, String name, String prefix, String pathName) {
            this.id = id;
            this.name = name;
            this.prefix = prefix;
            this.internalPrefix = this.prefix.replace(" ", "_").toLowerCase();
            this.pathName = pathName;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getInternalPrefix() {
            return internalPrefix;
        }

        public String getPathName() {
            return pathName;
        }
    }
}
