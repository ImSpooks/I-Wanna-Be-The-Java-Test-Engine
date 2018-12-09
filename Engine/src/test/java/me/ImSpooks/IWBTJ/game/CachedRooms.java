package me.ImSpooks.IWBTJ.game;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.game.mainmenu.MainMenu;
import me.ImSpooks.IWBTJ.game.mainmenu.SaveSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CachedRooms {

    private HashMap<String, IRoom> levels;
    private HashMap<Integer, IRoom> levelsPerNumber;

    public CachedRooms(Handler handler) {
        levels = new LinkedHashMap<>();
        levelsPerNumber = new HashMap<>();

        // initial rooms
        levels.put("saveSelection", new SaveSelection(handler));

        // rooms

        //stage 1
        levels.put("stage1/room1", new me.ImSpooks.IWBTJ.game.room.stage1.Room1(handler, "/room/level/stage1/room1.jmap"));


        levels.put("mainMenu", new MainMenu(handler));
        for (int i = 0; i < levels.values().size(); i++) {
            IRoom room = new ArrayList<>(levels.values()).get(i);
            levelsPerNumber.put(room.getRoomNumber(), room);
        }


        handler.removeObject(handler.object.getFirst());
    }

    public HashMap<String, IRoom> getLevels() {
        return levels;
    }

    public HashMap<Integer, IRoom> getLevelsPerNumber() {
        return levelsPerNumber;
    }

    public IRoom getRoom(String roomName) {
        return levels.get(roomName);
    }

    public IRoom getRoom(int number) {
        return levelsPerNumber.get(number);
    }
}
