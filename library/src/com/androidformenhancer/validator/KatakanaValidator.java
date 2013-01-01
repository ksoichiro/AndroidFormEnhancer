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
import com.androidformenhancer.annotation.Katakana;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Validates that the value of the field consists of Japanese katakana
 * characters or not.
 * 
 * @author Soichiro Kashima
 */
public class KatakanaValidator extends Validator {

    private static final String REGEX = "^[アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンャュョッァィゥェォヵヶガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポヴー、。]+$";

    @Override
    public String validate(final Field field) {
        String value = getValueAsString(field);

        Katakana katakana = field.getAnnotation(Katakana.class);
        if (katakana != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                if (!value.matches(REGEX)) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = katakana.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name
                    };
                    return getMessage(R.styleable.ValidatorMessages_afeErrorKatakana,
                            R.string.afe__msg_validation_katakana,
                            messageParams);
                }
            }
        }

        return null;
    }

}
