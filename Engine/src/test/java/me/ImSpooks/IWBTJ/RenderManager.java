package me.ImSpooks.IWBTJ;

import me.ImSpooks.IWBTJ.game.mainmenu.SaveSelection;
import me.ImSpooks.IWBTJ.save.SaveFile;
import me.ImSpooks.iwbtgengine.global.Global;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class RenderManager implements Runnable {

    private Main instance;

    public RenderManager(Main instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        double tps = Global.FRAME_RATE;
        long lastRender = System.nanoTime();
        double ns = 1000000000 / tps;
        double delta = 0;
        long timer = System.currentTimeMillis();

        int frames = 0;

        while (instance.running) {
            long now = System.nanoTime();
            delta += (now - lastRender) / ns;
            lastRender = now;
            while(delta >= 1) {
                delta = 0;

                tick();
                render();
                frames++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println(frames);
                frames = 0;

                if (SaveSelection.selected != null) {
                    SaveFile file = SaveSelection.selected;

                    file.setTime(file.getTime() + 1);

                    long time = file.getTime();
                    long lhours = time / 3600;
                    long lminutes = (time % 3600) / 60;
                    long lseconds = time % 60;

                    String hours = String.valueOf(lhours).length() == 1 ? "0" + lhours : String.valueOf(lhours);
                    String minutes = String.valueOf(lminutes).length() == 1 ? "0" + lminutes : String.valueOf(lminutes);
                    String seconds = String.valueOf(lseconds).length() == 1 ? "0" + lseconds : String.valueOf(lseconds);

                    Main.getInstance().frame.setTitle(Main.getInstance().title + " - Save " + (Integer.parseInt(file.getFile().getFileName().substring(file.getFile().getFileName().length() - 1)) + 1) + " - Deaths: " + file.getDeath() + "  Time: " + hours + ":" + minutes + ":" + seconds);
                }
            }
        }
    }

    private void tick() {
        this.instance.getHandler().tick();
        this.instance.getKeyboard().tickKeyboard();
    }

    private void render() {
        this.instance.requestFocus();
        BufferStrategy bs = this.instance.getBufferStrategy();
        if (bs == null){
            this.instance.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        this.instance.getHandler().render(g);

        g.dispose();
        bs.show();
    }
}
