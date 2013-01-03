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

package com.androidformenhancer.validator;

import com.androidformenhancer.R;
import com.androidformenhancer.annotation.FloatType;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * This validator provides the float field validation.
 * 
 * @author Soichiro Kashima
 */
public class FloatTypeValidator extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        FloatType floatValue = field.getAnnotation(FloatType.class);
        if (floatValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                try {
                    Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    return getMessage(R.styleable.ValidatorMessages_afeErrorFloatType,
                            R.string.afe__msg_validation_float_type,
                            getName(field, floatValue.nameResId()));
                }
            }
        }

        return null;
    }
}
