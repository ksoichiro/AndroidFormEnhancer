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
import com.androidformenhancer.annotation.DatePattern;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Validates that the value matches the regular expression.
 * 
 * @author Soichiro Kashima
 */
public class DatePatternValidator extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        DatePattern datePatternValue = field.getAnnotation(DatePattern.class);
        if (datePatternValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                DateFormat dateFormat = null;

                if (TextUtils.isEmpty(datePatternValue.value())) {
                    dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                } else {
                    dateFormat = new SimpleDateFormat(datePatternValue.value(),
                            Locale.getDefault());
                }
                dateFormat.setLenient(false);
                try {
                    dateFormat.parse(value);
                    return null;
                } catch (ParseException e) {
                    return getMessage(R.styleable.ValidatorMessages_afeErrorDatePattern,
                            R.string.afe__msg_validation_date,
                            getName(field, datePatternValue.nameResId()));
                }
            }
        }

        return null;
    }

}
