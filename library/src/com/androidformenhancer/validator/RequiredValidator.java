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
import com.androidformenhancer.form.annotation.Radio;
import com.androidformenhancer.form.annotation.Required;
import com.androidformenhancer.form.annotation.Spinner;
import com.androidformenhancer.form.annotation.Validated;
import com.androidformenhancer.form.annotation.When;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * This validator provides the required field validation.
 * 
 * @author Soichiro Kashima
 */
public class RequiredValidator extends Validator {

    private static final String TAG = "RequiredValidator";

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

        Required required = field.getAnnotation(Required.class);
        if (required != null) {
            // Checks the conditions to valiate
            When[] whenList = required.when();
            boolean validateEnabled;
            if (whenList == null || whenList.length == 0) {
                validateEnabled = true;
            } else {
                validateEnabled = false;
                for (When when : whenList) {
                    String name = when.name();
                    boolean isNotEmpty = when.isNotEmpty();
                    String equalsTo = when.equalsTo();
                    try {
                        Field whenField = getTarget().getClass().getField(name);
                        String whenValue = (String) whenField.get(getTarget());
                        if (isNotEmpty) {
                            if (!TextUtils.isEmpty(whenValue)) {
                                validateEnabled = true;
                            }
                        } else if (equalsTo.equals(whenValue)) {
                            validateEnabled = true;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            // Do not validate
            if (!validateEnabled) {
                return null;
            }
            // Validate
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                final String strValue = (String) value;
                Spinner spinner = (Spinner) field.getAnnotation(Spinner.class);
                if (TextUtils.isEmpty(strValue)
                        || (spinner != null && spinner.headIsDummy() && strValue.equals("0"))) {
                    String name = field.getName();
                    Validated validated = (Validated) field.getAnnotation(Validated.class);
                    int nameResId = validated.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = required.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    // Select message according to the type of the field
                    Resources res = getContext().getResources();
                    if (field.getAnnotation(Radio.class) != null
                            || spinner != null) {
                        return res.getString(
                                R.string.afe__msg_validation_require_selection,
                                new Object[] {
                                    name
                                });
                    } else {
                        return res.getString(
                                R.string.afe__msg_validation_required,
                                new Object[] {
                                    name
                                });
                    }
                }
            }
        }

        return null;
    }
}
