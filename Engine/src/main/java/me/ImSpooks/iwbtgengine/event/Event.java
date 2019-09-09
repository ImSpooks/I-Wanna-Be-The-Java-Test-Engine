package me.ImSpooks.iwbtgengine.event;

import me.ImSpooks.iwbtgengine.handler.GameHandler;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public interface Event {
    void begin(GameHandler handler);
    void update(GameHandler handler, float delta);
    void onFinish(GameHandler handler);
    boolean isFinished(GameHandler handler);
}
