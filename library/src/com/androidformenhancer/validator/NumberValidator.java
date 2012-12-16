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
import com.androidformenhancer.form.annotation.IntValue;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * This validator provides the required field validation.
 * 
 * @author Soichiro Kashima
 */
public class NumberValidator extends Validator {

    private static final String TAG = "NumberValidator";

    @Override
    public String validate(final Field field) {
        Object value;
        try {
            value = field.get(getTarget());
        } catch (Exception e) {
            // TODO Throw some exception to inform caller this illegal state
            Log.v(TAG, e.getMessage());
            return null;
        }

        IntValue intValue = field.getAnnotation(IntValue.class);
        if (intValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                final String strValue = (String) value;
                try {
                    Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    String name = field.getName();
                    int nameResId = intValue.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    return getContext().getResources().getString(
                            R.string.afe__msg_validation_integer,
                            new Object[] {
                                    name
                            });
                }
            }
        }

        return null;
    }
}
