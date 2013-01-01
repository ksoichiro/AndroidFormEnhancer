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
import com.androidformenhancer.annotation.NumOfDigits;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Validates that the number of the digits of the field.
 * 
 * @author Soichiro Kashima
 */
public class NumOfDigitsValidator extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        NumOfDigits numOfDigitsValue = field.getAnnotation(NumOfDigits.class);
        if (numOfDigitsValue != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value) || !value.matches("^[0-9]+$")) {
                    return null;
                }
                if (numOfDigitsValue.value() != value.length()) {
                    String name = field.getName();
                    int nameResId = getNameResourceId(field);
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    nameResId = numOfDigitsValue.nameResId();
                    if (nameResId > 0) {
                        name = getContext().getResources().getString(nameResId);
                    }
                    Object[] messageParams = new Object[] {
                            name, numOfDigitsValue.value()
                    };
                    return getMessage(R.styleable.ValidatorMessages_afeErrorNumOfDigits,
                            R.string.afe__msg_validation_num_of_digits,
                            messageParams);
                }
            }
        }

        return null;
    }

}
