package me.ImSpooks.IWBTJ.filemanager;

import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.game.mainmenu.SaveSelection;
import me.ImSpooks.IWBTJ.object.gameobjects.items.ObjBossItem;
import me.ImSpooks.IWBTJ.object.gameobjects.items.ObjSecretItem;
import me.ImSpooks.IWBTJ.save.SaveFile;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SaveData  extends IFile {
    public SaveData(File file, String fileName) {
        super(file, fileName);
        this.createIfNull = false;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public void saveFile() {
        try {
            File outDir = Main.getInstance().getFileManager().getSubDirectory("Data");
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            if (!this.getFile().exists()) {
                this.getFile().createNewFile();
            }

            PrintWriter writer = new PrintWriter(new FileWriter(this.getFile()));

            // encode in base64 so it is not easy to edit
            SaveFile saveObject = SaveSelection.selected;
            
            JSONObject json = new JSONObject(saveObject);
            byte[] encodedData = Base64.getEncoder().encode(json.toString().getBytes());

            writer.println(new String(encodedData));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void loadFile() throws IOException {
        if (!this.getFile().exists()) {
            SaveSelection.saveObjectList.add(new SaveFile(0, 0, SaveFile.Difficulty.NULL, 0, 0, 0, false, new ArrayList<>(), new ArrayList<>(), this));
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

        String line;
        while ((line = reader.readLine()) != null) {
            byte[] encodedData = line.getBytes();

            byte[] decodedData = Base64.getDecoder().decode(encodedData);
            String strJson = new String(decodedData);

            JSONObject json = new JSONObject(strJson);

            int death = json.getInt("death");
            long time = json.getLong("time");

            SaveFile.Difficulty difficulty = SaveFile.Difficulty.valueOf(json.getString("difficulty"));
            int roomNumber = json.getInt("roomNumber");
            int playerX = json.getInt("playerX");
            int playerY = json.getInt("playerY");
            boolean flipGravity = json.getBoolean("flipGravity");

            List<ObjSecretItem> secretItems = (List) json.getJSONArray("secretItems").toList();
            List<ObjBossItem> bossItems = (List) json.getJSONArray("bossItems").toList();

            SaveSelection.saveObjectList.add(new SaveFile(death, time, difficulty, roomNumber, playerX, playerY, flipGravity, secretItems, bossItems, this));

            reader.close();
            return;
        }
        SaveSelection.saveObjectList.add(new SaveFile(0, 0, SaveFile.Difficulty.NULL, 0, 0, 0, false, new ArrayList<>(), new ArrayList<>(), this));
    }

    @Override
    public FileManager.FileType getFileType() {
        return FileManager.FileType.SAVE;
    }
}