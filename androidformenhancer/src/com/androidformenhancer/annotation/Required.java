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
 * Represents the value of the field must not be null or empty String.
 * <p/>
 * If the target widget is a Spinner or RadioGroup, it means that one of the
 * item must be selected. If the target widget are CheckBoxes, it means that
 * some of the items must be checked. The number of check boxes which must be
 * checked, is defined by {@linkplain #atLeast()}.
 * <p/>
 * If you want to set conditions, use {@link #when()}.
 *
 * @author Soichiro Kashima
 * @see When
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {
    /**
     * Resource ID of the field name for the error message.<br>
     * This is set to {@code 0}(invalid) as default, and the field name will be
     * used in the error messages.
     */
    int nameResId() default 0;

    /**
     * CheckBoxes must be checked at least this value. This is only valid if the
     * widget are the CheckBoxes.
     */
    int atLeast() default 1;

    /**
     * If set to true, the validator assumes selecting the head item of the
     * spinner is an error.
     */
    boolean otherThanHead() default false;

    /**
     * Conditions when the constraint is applied.
     */
    When[] when() default {};
}
