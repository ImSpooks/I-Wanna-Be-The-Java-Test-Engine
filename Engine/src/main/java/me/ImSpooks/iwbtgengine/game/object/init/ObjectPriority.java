package me.ImSpooks.iwbtgengine.game.object.init;

import java.lang.annotation.*;

/**
 * Created by Nick on 11 sep. 2019.
 * No part of this publication may be reproduced, distributed, or transmitted in any form or by any means.
 * Copyright © ImSpooks
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectPriority {
    ObjectsPriority renderPriority() default ObjectsPriority.NORMAL;
    ObjectsPriority colisionPriority() default ObjectsPriority.NORMAL;
}
