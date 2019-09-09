package me.ImSpooks.iwbtgengine.event;

import lombok.Getter;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class EventHandler {

    @Getter private final GameHandler gameHandler;

    public EventHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    private final Queue<Event> eventQueue = new ArrayDeque<>();
    @Getter private Event currentEvent;

    public void update(float delta) {
        while (currentEvent == null || currentEvent.isFinished(this.gameHandler)) { // no active event
            if (currentEvent != null) {
                currentEvent.onFinish(this.gameHandler);
            }
            if (eventQueue.peek() == null) { // no event queued up
                currentEvent = null;
                break;
            } else {                    // event queued up
                currentEvent = eventQueue.poll();
                currentEvent.begin(this.gameHandler);
            }
        }

        if (currentEvent != null) {
            currentEvent.update(this.gameHandler, delta);
        }
    }

    public void queueEvent(Event event) {
        this.eventQueue.add(event);
    }
}
