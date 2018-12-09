package me.ImSpooks.IWBTJ;

import me.ImSpooks.IWBTJ.event.EventManager;
import me.ImSpooks.IWBTJ.event.EventTarget;
import me.ImSpooks.IWBTJ.event.Listener;
import me.ImSpooks.IWBTJ.event.events.keyboard.KeyPressEvent;
import me.ImSpooks.IWBTJ.filemanager.FileManager;
import me.ImSpooks.IWBTJ.game.CachedRooms;
import me.ImSpooks.IWBTJ.keyinputs.Keyboard;
import me.ImSpooks.IWBTJ.object.ID;
import me.ImSpooks.IWBTJ.object.gameobjects.kid.Kid;
import me.ImSpooks.IWBTJ.sound.SoundPlayer;
import me.ImSpooks.IWBTJ.utils.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main extends Canvas{

    private static Main instance;

    private FileManager fileManager;
    private EventManager eventManager;

    private Handler handler;

    private Thread thread;
    public boolean running;

    public static Main getInstance() {
        return instance;
    }

    public JFrame frame;

    //public static int gameWidth = 807, gameHeight = 637;
    public static int gameWidth = 807, gameHeight = 637;

    private Keyboard keyboard;
    private CachedRooms cachedRooms;

    private boolean debugMode;

    public String title = "I Wanna Be The Java";
    public Main() {
        this.debugMode = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

        instance = this;

        this.eventManager = new EventManager();

        this.addKeyListener(keyboard = new Keyboard());

        // loading all files
        this.fileManager = new FileManager(this);
        this.fileManager.loadFiles();


        // setting up frame frame

        this.frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(gameWidth, gameHeight));
        frame.setMaximumSize(new Dimension(gameWidth, gameHeight));
        frame.setMinimumSize(new Dimension(gameWidth, gameHeight));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);

        renderManager = new RenderManager(this);

        // initializing handler
        (this.handler = new Handler(this)).loadingScreen();

        // save resources in to files
        saveResources();

        // starts the game
        cachedRooms = new CachedRooms(handler);

        this.handler.addObject(cachedRooms.getRoom("mainMenu"), new Kid(-50, -50, ID.KID, this.handler));

        this.start();
    }

    public RenderManager renderManager;
    public synchronized void start() {
        running = true;
        thread = new Thread(renderManager);
        thread.start();

        addQuitKey();
    }

    public synchronized void stop(){
        try{
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public synchronized void reset(){
        eventManager.shutdown();

        frame.setTitle(title);
        this.eventManager = new EventManager();
        this.fileManager.loadFiles();
        renderManager = new RenderManager(this);
        this.handler = new Handler(this);

        this.handler.addObject(cachedRooms.getRoom("mainMenu"), new Kid(-50, -50, ID.KID, this.handler));

        addQuitKey();
    }

    public void addQuitKey() {
        eventManager.register(new Listener() {
            @EventTarget
            public void onKeyPress(KeyPressEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    if (SoundPlayer.currentPlaying != null)
                        SoundPlayer.currentPlaying.stop();
                    SoundPlayer.currentPlaying = null;
                    reset();
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(-1);
                }
            }
        });
    }


    public FileManager getFileManager() {
        return fileManager;
    }

    public Handler getHandler() {
        return handler;
    }

    public void saveResources() {
        Map<String, List<String>> files = new LinkedHashMap<>();

        files.put("", Arrays.asList("config.ini"));

        for (Map.Entry<String, List<String>> entry : files.entrySet()) {
            for (String subFiles : entry.getValue()) {
                if (entry.getKey().isEmpty()) {
                    File file = new File(this.getFileManager().getMainDirectory(), subFiles);
                    if (!file.exists()) {
                        ResourceHandler.saveResource(subFiles, false);
                    }
                }
                else {
                    File file = new File(this.getFileManager().getSubDirectory(entry.getKey()), subFiles);
                    if (!file.exists()) {
                        ResourceHandler.saveResource(entry.getKey() + "/" + subFiles, false);
                    }
                }
            }
        }
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public boolean isDebuging() {
        return debugMode;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public CachedRooms getCachedRooms() {
        return cachedRooms;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double distance(double x1, double x2, double y1, double y2) {
        return Math.hypot(x1-x2, y1-y2);
    }
}
