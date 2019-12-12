package me.ImSpooks.iwbtgengine.game.object.particles.init;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.camera.Camera;
import me.ImSpooks.iwbtgengine.event.events.PerformActionEvent;
import me.ImSpooks.iwbtgengine.event.events.init.PerformAction;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class ParticleManager {

    @Getter private List<Particle> particles;
    @Getter private GameHandler handler;

    public ParticleManager(GameHandler handler) {
        this.particles = new ArrayList<>();
        this.handler = handler;
    }

    public void addParticle(Particle particle) {
        this.particles.add(particle);
    }

    public void clear() {
        // Clear particles in a queue to avoid ConcurrentModificationException
        this.handler.getEventHandler().queueEvent(new PerformActionEvent(new PerformAction() {
            @Override
            public void run() {
                particles.clear();
            }
        }));
    }

    public void update(float delta) {
        Iterator<Particle> iterator = particles.iterator();

        while (iterator.hasNext()) {
            Particle particle = iterator.next();

            particle.update(delta);
            if (!particle.isAlive())
                iterator.remove();
        }
    }

    public void render(Camera camera, Graphics graphics) {
        for (Particle particle : particles) {
            particle.createGraphics(graphics, camera);
        }
    }
}
