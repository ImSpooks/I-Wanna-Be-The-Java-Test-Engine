package me.ImSpooks.IWBTJ.save;

import me.ImSpooks.IWBTJ.filemanager.SaveData;
import me.ImSpooks.IWBTJ.object.gameobjects.items.ObjBossItem;
import me.ImSpooks.IWBTJ.object.gameobjects.items.ObjSecretItem;

import java.util.List;

public class SaveFile { //TODO SaveSelection screen, SaveFile loading
    private int death;
    private long time;

    private Difficulty difficulty;
    private int roomNumber;
    private double playerX;
    private double playerY;
    private boolean flipGravity;

    private List<ObjSecretItem> secretItems;
    private List<ObjBossItem> bossItems;

    private final SaveData saveData;

    public SaveFile(int death, long time, Difficulty difficulty, int roomNumber, int playerX, int playerY, boolean flipGravity, List<ObjSecretItem> secretItems, List<ObjBossItem> bossItems, SaveData saveData) {
        this.death = death;
        this.time = time;
        this.difficulty = difficulty;
        this.roomNumber = roomNumber;
        this.playerX = playerX;
        this.playerY = playerY;
        this.flipGravity = flipGravity;
        this.secretItems = secretItems;
        this.bossItems = bossItems;
        this.saveData = saveData;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getPlayerX() {
        return playerX;
    }

    public void setPlayerX(double playerX) {
        this.playerX = playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public void setPlayerY(double playerY) {
        this.playerY = playerY;
    }

    public boolean isFlipGravity() {
        return flipGravity;
    }

    public void setFlipGravity(boolean flipGravity) {
        this.flipGravity = flipGravity;
    }

    public List<ObjSecretItem> getSecretItems() {
        return secretItems;
    }

    public void setSecretItems(List<ObjSecretItem> secretItems) {
        this.secretItems = secretItems;
    }

    public List<ObjBossItem> getBossItems() {
        return bossItems;
    }

    public void setBossItems(List<ObjBossItem> bossItems) {
        this.bossItems = bossItems;
    }

    public enum Difficulty {
        NULL(""),
        MEDIUM("Medium"),
        HARD(""),
        VERY_HARD("VHard"),
        IMPOSSIBLE(""),
        ;

        private final String internalName;

        Difficulty(String internalName) {
            this.internalName = internalName;
        }

        public String getInternalName() {
            return internalName;
        }
    }

    public SaveData getFile() {
        return saveData;
    }
}
