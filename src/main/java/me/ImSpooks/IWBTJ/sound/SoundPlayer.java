package me.ImSpooks.IWBTJ.sound;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class SoundPlayer {

    private static RegisteredSounds registeredSounds;

    public static SoundPlayer currentPlaying;

    public static RegisteredSounds getRegisteredSounds() {
        if (registeredSounds == null)
            registeredSounds = new RegisteredSounds();
        return registeredSounds;
    }

    private ISound sound;

    private Clip clip;
    private AudioInputStream inputStream;

    private int currentFrame = 0;
    private boolean paused = false;


    public SoundPlayer open(ISound sound) {
        this.sound = sound;

        try {
            this.clip = AudioSystem.getClip();
            if (sound instanceof IResourceFile) {
                this.inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(((IResourceFile) sound).getStream()));
            }
            else {
                this.inputStream = AudioSystem.getAudioInputStream(sound.getFile());
            }
            clip.open(inputStream);
            clip.setFramePosition(this.currentFrame);
        } catch (LineUnavailableException |
                UnsupportedAudioFileException | IOException e) {
            System.out.println("Something went wrong while loading file " + sound.getFileName());
            e.printStackTrace();
        }
        return this;
    }

    public SoundPlayer setVolume(double volume) {
        FloatControl gain = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(volume) / Math.log(gain.getMaximum()) * 20);
        //double dB = volume * game.getMaximum() / game.getMinimum();
        gain.setValue(dB);
        return this;
    }

    public void start() {
        this.clip.start();
    }

    public void stop() {
        this.clip.stop();
    }

    public void pause() {
        if (paused)
            return;
        paused = true;
        this.currentFrame = this.clip.getFramePosition();
        this.clip.stop();
    }
    public void resume() {
        if (!paused)
            return;
        paused = false;
        this.clip.setFramePosition(this.currentFrame);
        clip.start();
    }

    public Clip getClip() {
        return clip;
    }

    public AudioInputStream getInputStream() {
        return inputStream;
    }

    public int getCurrentFrame() {
        return this.clip.getFramePosition();
    }

    public SoundPlayer setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        return this;
    }

    public static SoundPlayer playFX(ISound sound) {
        SoundPlayer player = new SoundPlayer();
        player.open(sound);
        player.start();
        return player;
    }
}
