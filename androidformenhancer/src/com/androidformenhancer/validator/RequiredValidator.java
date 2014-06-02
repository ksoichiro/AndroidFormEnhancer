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
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.When;

import java.util.List;

/**
 * This validator provides the required field validation.
 *
 * @author Soichiro Kashima
 */
public class RequiredValidator extends Validator<Required> {

    @Override
    public Class<Required> getAnnotationClass() {
        return Required.class;
    }

    @Override
    public String validate(final Required annotation, final FieldData fieldData) {
        // Checks the conditions to validate
        When[] whenList = annotation.when();
        boolean validateEnabled;
        if (whenList == null || whenList.length == 0) {
            validateEnabled = true;
        } else {
            validateEnabled = false;
            for (When when : whenList) {
                boolean isNotEmpty = when.isNotEmpty();
                String equalsTo = when.equalsTo();
                try {
                    String whenValue = getValueById(when.id());
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
        if (fieldData.isArray()) {
            final List<String> list = fieldData.getValueAsStringList();
            if (list == null || list.size() < annotation.atLeast()) {
                return getMessage(
                        R.styleable.ValidatorMessages_afeErrorRequiredMultipleSelection,
                        R.string.afe__msg_validation_required_multiple_selection,
                        getName(fieldData, annotation.nameResId()), annotation.atLeast());
            }
        } else {
            final String strValue = fieldData.getValueAsString();
            if (TextUtils.isEmpty(strValue)
                    || (fieldData.getWidgetType() == WidgetType.SPINNER
                    && annotation.otherThanHead() && strValue.equals("0"))) {
                if (fieldData.getWidgetType() == WidgetType.RADIO
                        || fieldData.getWidgetType() == WidgetType.SPINNER) {
                    return getMessage(R.styleable.ValidatorMessages_afeErrorRequiredSelection,
                            R.string.afe__msg_validation_required_selection,
                            getName(fieldData, annotation.nameResId()));
                } else {
                    return getMessage(R.styleable.ValidatorMessages_afeErrorRequired,
                            R.string.afe__msg_validation_required,
                            getName(fieldData, annotation.nameResId()));
                }
            }
        }
        return null;
    }
}
