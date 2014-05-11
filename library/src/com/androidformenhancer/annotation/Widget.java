/*
 * Copyright 2012 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidformenhancer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the field is a widget of the form. If you want to validate the
 * value of the field, you must always apply this.
 *
 * @author Soichiro Kashima
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Widget {
    /**
     * Resource ID of the widget related to this field.
     */
    int id();

    /**
     * Resource ID of the field name for the error message.<br>
     * This is set to {@code 0}(invalid) as default, and the field name will be
     * used in the error messages.
     */
    int nameResId() default 0;

    /**
     * Validates this field after the the field related to this ID.
     */
    int validateAfter() default 0;

    /**
     * Values of the widget grouped by this field.
     */
    WidgetValue[] values() default {};

    public static enum Type {
        TEXT,
        CHECKBOX,
        RADIO,
        SPINNER;
    }

}
