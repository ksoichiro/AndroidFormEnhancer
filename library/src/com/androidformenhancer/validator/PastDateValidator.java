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
import com.androidformenhancer.annotation.PastDate;

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
public class PastDateValidator extends Validator<PastDate> {

    @Override
    public Class<PastDate> getAnnotationClass() {
        return PastDate.class;
    }

    @Override
    public String validate(final PastDate annotation, final FieldData fieldData) {
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
        Date date = null;
        try {
            date = dateFormat.parse(value);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(date);
            discardTimeLessThanDay(calendar);
            Calendar today = Calendar.getInstance(Locale.getDefault());
            discardTimeLessThanDay(today);
            if (annotation.allowToday()) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            if (calendar.before(today)) {
                return null;
            }
        } catch (ParseException e) {
        }
        return getMessage(R.styleable.ValidatorMessages_afeErrorPastDate,
                R.string.afe__msg_validation_past_date,
                getName(fieldData, annotation.nameResId()));
    }

    private void discardTimeLessThanDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

}
