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
import com.androidformenhancer.annotation.MaxValue;

/**
 * Validates that the value exceeds the max value or not.
 *
 * @author Soichiro Kashima
 */
public class MaxValueValidator extends Validator<MaxValue> {

    @Override
    public Class<MaxValue> getAnnotationClass() {
        return MaxValue.class;
    }

    @Override
    public String validate(final MaxValue annotation, final FieldData fieldData) {
        final String value = fieldData.getValueAsString();
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        int nValue = -1;
        boolean hasError = false;
        try {
            nValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            hasError = true;
        }
        if (hasError || annotation.value() < nValue) {
            return getMessage(R.styleable.ValidatorMessages_afeErrorMaxValue,
                    R.string.afe__msg_validation_max_value,
                    getName(fieldData, annotation.nameResId()), annotation.value());
        }
        return null;
    }

}
