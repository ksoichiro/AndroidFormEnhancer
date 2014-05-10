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

import android.text.TextUtils;

import com.androidformenhancer.FieldData;
import com.androidformenhancer.R;
import com.androidformenhancer.annotation.DatePattern;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Validates that the value matches the regular expression.
 *
 * @author Soichiro Kashima
 */
public class DatePatternValidator extends Validator<DatePattern> {

    @Override
    public Class<DatePattern> getAnnotationClass() {
        return DatePattern.class;
    }

    @Override
    public String validate(final DatePattern annotation, final FieldData fieldData) {
        final String value = fieldData.getValueAsString();
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        DateFormat dateFormat = null;

        if (TextUtils.isEmpty(annotation.value())) {
            dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat(annotation.value(),
                    Locale.getDefault());
        }
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(value);
            return null;
        } catch (ParseException e) {
            return getMessage(R.styleable.ValidatorMessages_afeErrorDatePattern,
                    R.string.afe__msg_validation_date,
                    getName(fieldData, annotation.nameResId()));
        }
    }

}
