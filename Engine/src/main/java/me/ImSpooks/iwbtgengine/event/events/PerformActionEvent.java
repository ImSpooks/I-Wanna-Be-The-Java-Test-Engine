package me.ImSpooks.iwbtgengine.event.events;

import me.ImSpooks.iwbtgengine.event.Event;
import me.ImSpooks.iwbtgengine.event.events.init.PerformAction;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public class PerformActionEvent implements Event {

    private final PerformAction action;

    public PerformActionEvent(PerformAction action) {
        this.action = action;
    }

    @Override
    public void begin(GameHandler handler) {
        action.run();
    }

    @Override
    public void update(GameHandler handler, float delta) {}

    @Override
    public void onFinish(GameHandler handler) {}

    @Override
    public boolean isFinished(GameHandler handler) { return true; }
}
