
package com.androidformenhancer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Widget {

    int id();

    Type type();

    int validateAfter() default 0;

    WidgetValue[] values() default {};

    int atLeast() default 1;

    boolean headIsDummy() default false;

    public static enum Type {
        TEXT,
        CHECKBOX,
        RADIO,
        SPINNER;
    }

}
