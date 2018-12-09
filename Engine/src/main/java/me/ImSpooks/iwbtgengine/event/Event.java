package me.ImSpooks.iwbtgengine.event;

/**
 * Created by Nick on 09 Dec 2018.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
public interface Event {
    void begin();
    void update(float delta);
    void onFinish();
    boolean isFinished();
}
