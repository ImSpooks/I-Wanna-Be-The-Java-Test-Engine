package me.ImSpooks.iwbtgengine;

import lombok.Getter;
import org.hackyourlife.gcn.dsp.BRSTMPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nick on 01 May 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class Window {

    @Getter private JFrame frame;

    public Window(Main main, String title, int width, int height) {
        this.initialize();

        this.frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.add(main);
    }

    private void initialize() {
        System.setProperty("sun.java2d.opengl", "true");

        new BRSTMPlayer(this.getClass().getResourceAsStream("/resources/sounds/music/STRM_N_KOOPA_N.brstm")).start();
    }
}
