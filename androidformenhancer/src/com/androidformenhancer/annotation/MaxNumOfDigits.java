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
 * Represents the value of the field must have digits less than or equals to the
 * number specified by {@link #value()}.
 * <p/>
 * This annotation resembles to the {@linkplain MaxLength}, but this does not
 * treat as an error if the value includes non-digit character even though its
 * length does not match {@linkplain #value()}. Use {@linkplain Digits} together
 * if you want to validate whether the characters in the value are only digits.
 * <p/>
 * If you want to disallow length less than {@link #value()}, use
 * {@linkplain NumOfDigits} instead.
 *
 * @author Soichiro Kashima
 * @see Digits
 * @see NumOfDigits
 * @see MaxLength
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxNumOfDigits {
    /**
     * Resource ID of the field name for the error message.<br>
     * This is set to {@code 0}(invalid) as default, and the field name will be
     * used in the error messages.
     */
    int nameResId() default 0;

    /**
     * Max number of the digits of the field.
     */
    int value();
}
