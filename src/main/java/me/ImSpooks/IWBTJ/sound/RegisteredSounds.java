package me.ImSpooks.IWBTJ.sound;

import me.ImSpooks.IWBTJ.sound.fx.IResourceSoundFX;
import me.ImSpooks.IWBTJ.sound.fx.ISoundFX;
import me.ImSpooks.IWBTJ.sound.music.IMusic;
import me.ImSpooks.IWBTJ.sound.music.IResourceMusic;
import me.ImSpooks.IWBTJ.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class RegisteredSounds {

    private HashMap<String, ISoundFX> soundFX;
    private HashMap<String, IMusic> music;

    public HashMap<String, ISound> allSounds;

    public RegisteredSounds() {
        this.soundFX = new LinkedHashMap<>();
        this.music = new LinkedHashMap<>();
        this.allSounds = new LinkedHashMap<>();

        this.init();
    }

    private void init() {
        // soundfx

        //kid sounds
        this.soundFX.put("kidJump", new IResourceSoundFX("sndJump.wav", "/sounds/sfx/sndJump.wav"));
        this.soundFX.put("kidDJump", new IResourceSoundFX("sndDJump.wav", "/sounds/sfx/sndDJump.wav"));
        this.soundFX.put("kidWallJump", new IResourceSoundFX("sndWallJump.wav", "/sounds/sfx/sndWallJump.wav"));
        this.soundFX.put("kidShoot", new IResourceSoundFX("sndShoot.wav", "/sounds/sfx/sndShoot.wav"));
        this.soundFX.put("kidDeath", new IResourceSoundFX("sndDeath.wav", "/sounds/sfx/sndDeath.wav"));

        this.music.put("guyRock", new IResourceMusic("musGuyRock.wav", "/sounds/music/musGuyRock.wav"));
        this.music.put("megaman", new IResourceMusic("musMegaman.wav", "/sounds/music/musMegaman.wav"));
        this.music.put("miku", new IResourceMusic("musMiku.wav", "/sounds/music/musMiku.wav"));
        this.music.put("onDeath", new IResourceMusic("musOnDeath.wav", "/sounds/music/musOnDeath.wav"));


        this.allSounds.putAll(soundFX);
        this.allSounds.putAll(music);
    }

    public ISoundFX getSoundFX(String sound) {
        ISoundFX fx = this.soundFX.get(sound);

        if (fx instanceof IResourceFile) {
            try {
                IResourceSoundFX soundObject = (IResourceSoundFX) ReflectionUtils.getConstructor(IResourceSoundFX.class, String.class, String.class).newInstance(fx.getFileName(), ((IResourceFile) fx).getResourcePath());
                return soundObject;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            try {
                ISoundFX soundObject = (ISoundFX) ReflectionUtils.getConstructor(ISoundFX.class, String.class).newInstance(fx.getFileName());
                return soundObject;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public IMusic getMusic(String sound) {
        IMusic music = this.music.get(sound);

        if (music instanceof IResourceFile) {
            try {
                IResourceMusic soundObject = (IResourceMusic) ReflectionUtils.getConstructor(IResourceMusic.class, String.class, String.class).newInstance(music.getFileName(), ((IResourceFile) music).getResourcePath());
                return soundObject;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            try {
                IMusic soundObject = (IMusic) ReflectionUtils.getConstructor(IMusic.class, String.class).newInstance(music.getFileName());
                return soundObject;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
