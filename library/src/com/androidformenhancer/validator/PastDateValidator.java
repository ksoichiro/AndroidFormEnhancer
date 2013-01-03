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
import com.androidformenhancer.annotation.PastDate;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Validates that the value is the past date.
 * 
 * @author Soichiro Kashima
 */
public class PastDateValidator extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        PastDate pastDateValue = field.getAnnotation(PastDate.class);
        if (pastDateValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                DateFormat dateFormat = null;

                // Use DatePattern's format if exists
                DatePattern datePatternValue = field.getAnnotation(DatePattern.class);
                if (datePatternValue == null || TextUtils.isEmpty(datePatternValue.value())) {
                    dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                } else {
                    dateFormat = new SimpleDateFormat(datePatternValue.value(),
                            Locale.getDefault());
                }

                dateFormat.setLenient(false);
                Date date = null;
                try {
                    date = dateFormat.parse(value);
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTime(date);
                    discardTimeLessThanDay(calendar);
                    Calendar today = Calendar.getInstance(Locale.getDefault());
                    discardTimeLessThanDay(today);
                    if (pastDateValue.allowToday()) {
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                    }
                    if (calendar.before(today)) {
                        return null;
                    }
                } catch (ParseException e) {
                }
                return getMessage(R.styleable.ValidatorMessages_afeErrorPastDate,
                        R.string.afe__msg_validation_past_date,
                        getName(field, pastDateValue.nameResId()));
            }
        }

        return null;
    }

    private void discardTimeLessThanDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

}
