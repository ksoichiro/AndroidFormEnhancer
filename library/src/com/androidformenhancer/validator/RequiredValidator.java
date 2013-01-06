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
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.Required;
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
                    boolean isNotEmpty = when.isNotEmpty();
                    String equalsTo = when.equalsTo();
                    try {
                        String whenValue = (String) getValueById(when.id());
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
                if (TextUtils.isEmpty(strValue)
                        || (getWidgetType(field) == WidgetType.SPINNER
                                && required.otherThanHead() && strValue.equals("0"))) {
                    if (getWidgetType(field) == WidgetType.RADIO
                            || getWidgetType(field) == WidgetType.SPINNER) {
                        return getMessage(R.styleable.ValidatorMessages_afeErrorRequiredSelection,
                                R.string.afe__msg_validation_required_selection,
                                getName(field, required.nameResId()));
                    } else {
                        return getMessage(R.styleable.ValidatorMessages_afeErrorRequired,
                                R.string.afe__msg_validation_required,
                                getName(field, required.nameResId()));
                    }
                }
            } else if (type.equals(List.class)) {
                final List<String> list = getValueAsStringList(field);
                if (list == null || list.size() < required.atLeast()) {
                    return getMessage(
                            R.styleable.ValidatorMessages_afeErrorRequiredMultipleSelection,
                            R.string.afe__msg_validation_required_multiple_selection,
                            getName(field, required.nameResId()), required.atLeast());
                }
            }
        }

        return null;
    }
}
