package me.ImSpooks.iwbtgengine.event;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class EventHandler {

    private final Queue<Event> eventQueue = new ArrayDeque<>();
    private Event currentEvent;

    public void update(float delta) {
        while (currentEvent == null || currentEvent.isFinished()) { // no active event
            if (eventQueue.peek() == null) { // no event queued up
                currentEvent = null;
                break;
            } else {                    // event queued up
                currentEvent = eventQueue.poll();
                currentEvent.begin();
            }
        }

        if (currentEvent != null) {
            currentEvent.update(delta);
        }
    }

    public void addEvent(Event event) {
        this.eventQueue.add(event);
    }
}
