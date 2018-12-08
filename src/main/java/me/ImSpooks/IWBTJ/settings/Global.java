package me.ImSpooks.IWBTJ.settings;

import me.ImSpooks.IWBTJ.Main;
import me.ImSpooks.IWBTJ.game.IRoom;
import me.ImSpooks.IWBTJ.game.mainmenu.SaveSelection;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.save.SaveFile;

public class Global {

    public static double gravity = 1.0;

    public static String fontName = "Unispace";

    public static IRoom currentRoom = null;

    public static void saveGame() {
        Kid kid = Main.getInstance().getHandler().getKid();

        SaveFile save = SaveSelection.selected;
        save.setRoomNumber(Global.currentRoom.getRoomNumber());
        if (kid != null) {
            save.setPlayerX(kid.getX());
            save.setPlayerY(kid.getY());
        }
        else {
            save.setPlayerX(32);
            save.setPlayerY(32);
        }

        save.setFlipGravity(Global.gravity < 0.0);


        SaveSelection.selected.getFile().saveFile();
    }
}
