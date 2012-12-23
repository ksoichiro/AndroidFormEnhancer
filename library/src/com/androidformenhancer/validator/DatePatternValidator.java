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
import com.androidformenhancer.form.annotation.DatePattern;

import android.text.TextUtils;
import android.util.Log;

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

    private static final String TAG = "DatePatternValidator";

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

        DatePattern datePatternValue = field.getAnnotation(DatePattern.class);
        if (datePatternValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                final String strValue = (String) value;
                if (TextUtils.isEmpty(strValue)) {
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
                    dateFormat.parse(strValue);
                    return null;
                } catch (ParseException e) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = datePatternValue.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name
                    };
                    return getMessage(R.styleable.ValidatorMessages_afeErrorDatePattern,
                            R.string.afe__msg_validation_date,
                            messageParams);
                }
            }
        }

        return null;
    }

}
