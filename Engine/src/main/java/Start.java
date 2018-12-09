import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.ImSpooks.iwbtgengine.Main;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Start {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "I Wanna Be The Java Engine";

        config.width = 806;
        config.height = 637;

        config.foregroundFPS = 50;
        config.backgroundFPS = 50;
        config.resizable = false;

        new LwjglApplication(new Main(), config);
    }
}
