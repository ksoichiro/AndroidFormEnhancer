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

package com.androidformenhancer.sample.demos;

import com.androidformenhancer.validator.Validator;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * @author Soichiro Kashima
 */
public class SampleValidator extends Validator {

    @Override
    public String validate(final Field field) {
        Object value;
        try {
            value = field.get(getTarget());
        } catch (Exception e) {
            return null;
        }

        SampleAnnotation sampleValue = field.getAnnotation(SampleAnnotation.class);
        if (sampleValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                final String strValue = (String) value;
                if (TextUtils.isEmpty(strValue)
                        || !"A".equals(strValue.toUpperCase(Locale.getDefault()))) {
                    // Now this is a validation error, create message
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = sampleValue.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name
                    };
                    return getContext().getResources().getString(
                            R.string.msg_validation_sample,
                            messageParams);
                }
            }
        }

        return null;
    }
}
