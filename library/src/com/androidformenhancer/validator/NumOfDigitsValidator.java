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
import com.androidformenhancer.annotation.NumOfDigits;

/**
 * Validates that the number of the digits of the field.
 *
 * @author Soichiro Kashima
 */
public class NumOfDigitsValidator extends Validator<NumOfDigits> {

    @Override
    public Class<NumOfDigits> getAnnotationClass() {
        return NumOfDigits.class;
    }

    @Override
    public String validate(final NumOfDigits annotation, final FieldData fieldData) {
        final String value = fieldData.getValueAsString();
        if (TextUtils.isEmpty(value) || !value.matches("^[0-9]+$")) {
            return null;
        }
        if (annotation.value() != value.length()) {
            return getMessage(R.styleable.ValidatorMessages_afeErrorNumOfDigits,
                    R.string.afe__msg_validation_num_of_digits,
                    getName(fieldData, annotation.nameResId()), annotation.value());
        }
        return null;
    }

}
