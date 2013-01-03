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
import com.androidformenhancer.annotation.Regex;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Validates that the value matches the regular expression.
 * 
 * @author Soichiro Kashima
 */
public class RegexValidator extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        Regex regexValue = field.getAnnotation(Regex.class);
        if (regexValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                if (!value.matches(regexValue.value())) {
                    return getMessage(R.styleable.ValidatorMessages_afeErrorRegex,
                            R.string.afe__msg_validation_regex,
                            getName(field, regexValue.nameResId()));
                }
            }
        }

        return null;
    }

}
