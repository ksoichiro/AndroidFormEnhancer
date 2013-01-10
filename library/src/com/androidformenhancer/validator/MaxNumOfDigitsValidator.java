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

import com.androidformenhancer.FieldData;
import com.androidformenhancer.R;
import com.androidformenhancer.annotation.MaxNumOfDigits;

import android.text.TextUtils;

/**
 * Validates that the length of the value of the field exceeds the max number of
 * digits or not.
 * 
 * @author Soichiro Kashima
 */
public class MaxNumOfDigitsValidator extends Validator<MaxNumOfDigits> {

    @Override
    public Class<MaxNumOfDigits> getAnnotationClass() {
        return MaxNumOfDigits.class;
    }

    @Override
    public String validate(final MaxNumOfDigits annotation, final FieldData fieldData) {
        final String value = fieldData.getValueAsString();
        if (TextUtils.isEmpty(value) || !value.matches("^[0-9]+$")) {
            return null;
        }
        if (annotation.value() < value.length()) {
            return getMessage(R.styleable.ValidatorMessages_afeErrorMaxNumOfDigits,
                    R.string.afe__msg_validation_max_num_of_digits,
                    getName(fieldData, annotation.nameResId()),
                    annotation.value());
        }
        return null;
    }

}
