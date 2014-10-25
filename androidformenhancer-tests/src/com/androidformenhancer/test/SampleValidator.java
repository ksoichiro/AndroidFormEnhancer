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

package com.androidformenhancer.test;

import android.text.TextUtils;

import com.androidformenhancer.FieldData;
import com.androidformenhancer.validator.Validator;

import java.util.Locale;

/**
 * @author Soichiro Kashima
 */
public class SampleValidator extends Validator<SampleAnnotation> {

    @Override
    public Class<SampleAnnotation> getAnnotationClass() {
        return SampleAnnotation.class;
    }

    @Override
    public String validate(final SampleAnnotation annotation, final FieldData formMetaData) {
        final String value = formMetaData.getValueAsString();
        if (TextUtils.isEmpty(value)
                || !"A".equals(value.toUpperCase(Locale.getDefault()))) {
            // Now this is a validation error, create message
            return getContext().getResources().getString(
                    R.string.msg_validation_sample,
                    getName(formMetaData, annotation.nameResId()));
        }
        return null;
    }

}
