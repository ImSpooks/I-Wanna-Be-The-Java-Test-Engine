package me.ImSpooks.iwbtgengine.event.events;

import lombok.Getter;
import lombok.Setter;
import me.ImSpooks.iwbtgengine.event.Event;
import me.ImSpooks.iwbtgengine.handler.GameHandler;

/**
 * Created by Nick on 09 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public abstract class CutsceneEvent implements Event {
    
    @Getter @Setter private boolean skip = false;


    public abstract boolean cutsceneFinished(GameHandler handler);

    @Override
    public boolean isFinished(GameHandler handler) {
        return skip || cutsceneFinished(handler);
    }
}
