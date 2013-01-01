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
import com.androidformenhancer.annotation.CheckBoxGroup;
import com.androidformenhancer.annotation.Radio;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.Spinner;
import com.androidformenhancer.annotation.When;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This validator provides the required field validation.
 * 
 * @author Soichiro Kashima
 */
public class RequiredValidator extends Validator {

    @Override
    public String validate(final Field field) {
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
                final String strValue = getValueAsString(field);
                Spinner spinner = (Spinner) field.getAnnotation(Spinner.class);
                if (TextUtils.isEmpty(strValue)
                        || (spinner != null && spinner.headIsDummy() && strValue.equals("0"))) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = required.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    // Select message according to the type of the field
                    Object[] messageParams = new Object[] {
                            name
                    };
                    if (field.getAnnotation(Radio.class) != null
                            || spinner != null) {
                        return getMessage(R.styleable.ValidatorMessages_afeErrorRequiredSelection,
                                R.string.afe__msg_validation_required_selection,
                                messageParams);
                    } else {
                        return getMessage(R.styleable.ValidatorMessages_afeErrorRequired,
                                R.string.afe__msg_validation_required,
                                messageParams);
                    }
                }
            } else if (type.equals(List.class)) {
                final List<String> list = getValueAsStringList(field);
                CheckBoxGroup checkBoxGroup = (CheckBoxGroup) field
                        .getAnnotation(CheckBoxGroup.class);
                if (checkBoxGroup != null
                        && (list == null || list.size() < checkBoxGroup.atLeast())) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = required.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name, checkBoxGroup.atLeast()
                    };
                    return getMessage(
                            R.styleable.ValidatorMessages_afeErrorRequiredMultipleSelection,
                            R.string.afe__msg_validation_required_multiple_selection,
                            messageParams);
                }
            }
        }

        return null;
    }
}
