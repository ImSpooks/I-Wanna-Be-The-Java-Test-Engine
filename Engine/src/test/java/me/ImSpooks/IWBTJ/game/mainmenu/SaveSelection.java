package me.ImSpooks.IWBTJ.game.mainmenu;

import me.ImSpooks.IWBTJ.Handler;
import me.ImSpooks.IWBTJ.event.EventTarget;
import me.ImSpooks.IWBTJ.event.Listener;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyPressEvent;
import me.ImSpooks.IWBTJ.game.IRoom;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.FakeKid;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.save.SaveFile;
import me.ImSpooks.IWBTJ.settings.Global;
import me.ImSpooks.IWBTJ.settings.Keybinds;
import me.ImSpooks.IWBTJ.sound.SoundPlayer;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SaveSelection extends IRoom {

    public static SaveFile selected;
    public static List<SaveFile> saveObjectList = new ArrayList<>();

    private FakeKid fakeKid = null;

    public SaveSelection(Handler handler) {
        super(handler, "/room/main/SaveMenu.jmap");
    }

    @Override
    public void initialize() {
        handler.getMain().getEventManager().register(new KeyInputs());

        fakeKid = new FakeKid(0, 0, ID.KID, handler);
    }

    @Override
    public void tick() {
        if (fakeKid != null)
            fakeKid.tick();
        tickObjects();
    }


    @Override
    public void renderRoom(Graphics g) {
        for (int i = 0; i < saveObjectList.size(); i++) {
            int x = 160 + 240 * i;
            int y = 192;

            g.setColor(Color.BLACK);
            g.setFont(new Font(Global.fontName, Font.BOLD, 25));

            int stringwitdh = g.getFontMetrics().stringWidth("Save " + (i+1));

            drawStringShadow(g, "Save " + (i+1), x - (stringwitdh / 2), y);

            x = 100 + 240 * i;
            y = 240;

            SaveFile file = saveObjectList.get(i);

            g.setFont(new Font(null, Font.BOLD, 16));
            g.setColor(Color.BLACK);


            try {
                Field field = file.getClass().getDeclaredField("death");
                field.setAccessible(true);
                drawStringShadow(g, "Deaths: " + field.get(file), x, y + 16);

                field = file.getClass().getDeclaredField("time");
                field.setAccessible(true);

                long time = field.getLong(file);
                long lhours = time / 3600;
                long lminutes = (time % 3600) / 60;
                long lseconds = time % 60;

                String hours = String.valueOf(lhours).length() == 1 ? "0" + lhours : String.valueOf(lhours);
                String minutes = String.valueOf(lminutes).length() == 1 ? "0" + lminutes : String.valueOf(lminutes);
                String seconds = String.valueOf(lseconds).length() == 1 ? "0" + lseconds : String.valueOf(lseconds);

                drawStringShadow(g, "Time: " + hours + ":" + minutes + ":" + seconds, x, y + 32);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
        fakeKid.setX(144 + 240 * saveSelected);
        fakeKid.setY(448 - 4);

        if (optionSelected != -1) {
            int x = 160 + 240 * saveSelected;
            int y = 216;

            g.setFont(new Font(null, Font.BOLD, 16));
            g.setColor(Color.BLACK);

            int stringwitdh = g.getFontMetrics().stringWidth("<" + options[optionSelected] + ">");

            drawStringShadow(g, "<" + options[optionSelected] + ">", x - (stringwitdh / 2), y);
        }

        /*g.setColor(new Color(0, 255, 255, 128).darker().darker());
        for (int i = 0; i < 20; i++) {
            g.drawLine(0, 32 * i, Main.gameWidth, 32 * i);
        }

        for (int i = 0; i < 30; i++) {
            g.drawLine(32 * i, 0, 32 * i, Main.gameHeight);
        }*/

        fakeKid.render(g);
    }

    @Override
    public int getRoomNumber() {
        return 0;
    }

    @Override
    public void onRemove() {
    }




    public int saveSelected = 0;
    private int optionSelected = -1;
    private String[] options;
    private class KeyInputs implements Listener {
        public KeyInputs() {
        }

        public SaveFile selected_ingui;


        @EventTarget
        public void onKeyPress(KeyPressEvent e) {
            if (e.getKeyCode() == Keybinds.KEY_JUMP) {
                selected_ingui = saveObjectList.get(saveSelected);
                if (selected_ingui.getTime() == 0)
                    options = new String[] {"Medium", "Hard", "Very Hard", "Impossible"};
                else
                    options = new String[] {"Load game", "Medium", "Hard", "Very Hard", "Impossible"};
                optionSelected = 0;

                handler.getMain().getEventManager().unregister(this);
                handler.getMain().getEventManager().register(new KeyInputs2());
            }
            else {
                if (e.getKeyCode() == Keybinds.KEY_RIGHT) {
                    if (saveSelected < saveObjectList.size() - 1) {
                        saveSelected++;
                    } else {
                        saveSelected = 0;
                    }
                    SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidJump")).start();
                } else if (e.getKeyCode() == Keybinds.KEY_LEFT) {
                    if (saveSelected > 0) {
                        saveSelected--;
                    } else {
                        saveSelected = saveObjectList.size() - 1;
                    }
                    SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidJump")).start();
                }
            }
        }

        public class KeyInputs2 implements Listener {
            @EventTarget
            public void onKeyPress(KeyPressEvent e) {
                if (e.getKeyCode() == Keybinds.KEY_JUMP) {
                    e.setCancelled(true);
                    selected = selected_ingui;
                    IRoom room;

                    if (options[optionSelected].equalsIgnoreCase("Load game")) {
                        room = handler.getMain().getCachedRooms().getRoom(selected_ingui.getRoomNumber());

                        double x = selected_ingui.getPlayerX();
                        double y = selected_ingui.getPlayerY();
                        boolean flipGravity = selected_ingui.isFlipGravity();

                        if (flipGravity)
                            Global.gravity *= -1;

                        Kid kid = handler.getKid();
                        kid.resetKid();
                        kid.setX(x);
                        kid.setY(y);

                        handler.addObject(room);
                    }
                    else {
                        room = handler.getMain().getCachedRooms().getRoom(1);
                        Global.currentRoom = room;
                        Kid kid = handler.getKid();
                        kid.resetKid();
                        kid.setX(room.getStartX());
                        kid.setY(room.getStartY());

                        handler.addObject(room);

                        selected.setDifficulty(SaveFile.Difficulty.valueOf(options[optionSelected].toUpperCase().replace(" ", "_")));
                        Global.saveGame();
                    }
                    Global.currentRoom = room;

                    handler.getMain().getEventManager().unregister(this);
                }
                else {
                    if (e.getKeyCode() == Keybinds.KEY_RIGHT) {
                        if (optionSelected < options.length - 1) {
                            optionSelected++;
                        } else {
                            optionSelected = 0;
                        }
                        SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidJump")).start();
                    } else if (e.getKeyCode() == Keybinds.KEY_LEFT) {
                        if (optionSelected > 0) {
                            optionSelected--;
                        } else {
                            optionSelected = options.length - 1;
                        }
                        SoundPlayer.playFX(SoundPlayer.getRegisteredSounds().getSoundFX("kidJump")).start();
                    }
                }
            }
        }
    }
}