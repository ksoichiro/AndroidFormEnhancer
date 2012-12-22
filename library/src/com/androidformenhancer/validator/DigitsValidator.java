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
import com.androidformenhancer.form.annotation.Digits;
import com.androidformenhancer.form.annotation.Validated;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * This validator provides the digital character field validation.
 * 
 * @author Soichiro Kashima
 */
public class DigitsValidator extends Validator {

    private static final String TAG = "DigitsValidator";

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

        Digits digits = field.getAnnotation(Digits.class);
        if (digits != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                final String strValue = (String) value;
                if (TextUtils.isEmpty(strValue)) {
                    return null;
                }
                if (!strValue.matches("^[0-9]+$")) {
                    String name = field.getName();
                    Validated validated = (Validated) field.getAnnotation(Validated.class);
                    int nameResId = validated.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = digits.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    return getContext().getResources().getString(
                            R.string.afe__msg_validation_digits,
                            new Object[] {
                                name
                            });
                }
            }
        }

        return null;
    }

}
