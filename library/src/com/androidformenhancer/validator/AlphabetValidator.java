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
import com.androidformenhancer.annotation.Alphabet;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Validates that the value of the field consists of ASCII alphabet characters
 * or not.
 * 
 * @author Soichiro Kashima
 */
public class AlphabetValidator extends Validator {

    private static final String REGEX = "^[a-zA-Z]+$";
    private static final String REGEX_WITH_SPACE = "^[a-zA-Z ]+$";

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        Alphabet alphabet = field.getAnnotation(Alphabet.class);
        if (alphabet != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                String regex = alphabet.allowSpace() ? REGEX_WITH_SPACE : REGEX;
                if (!value.matches(regex)) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = alphabet.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name
                    };
                    return getMessage(R.styleable.ValidatorMessages_afeErrorAlphabet,
                            R.string.afe__msg_validation_alphabet,
                            messageParams);
                }
            }
        }

        return null;
    }

}
