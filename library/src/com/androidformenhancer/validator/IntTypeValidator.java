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
import com.androidformenhancer.annotation.IntType;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * This validator provides the integer field validation.
 * 
 * @author Soichiro Kashima
 */
public class IntTypeValidator extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        IntType intValue = field.getAnnotation(IntType.class);
        if (intValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return getMessage(R.styleable.ValidatorMessages_afeErrorIntType,
                            R.string.afe__msg_validation_int_type,
                            getName(field, intValue.nameResId()));
                }
            }
        }

        return null;
    }
}
