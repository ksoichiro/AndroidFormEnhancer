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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Validates that the value matches the regular expression.
 * 
 * @author Soichiro Kashima
 */
public abstract class AbstractRegexValidator<T extends Annotation> extends Validator {

    @Override
    public String validate(final Field field) {
        final String value = getValueAsString(field);

        T annotation = field.getAnnotation(getValidationAnnotationClass());
        if (annotation != null) {
            final Class<?> type = field.getType();
            if (type.equals(String.class)) {
                if (TextUtils.isEmpty(value)) {
                    return null;
                }
                if (!value.matches(getRegex(annotation))) {
                    return getMessage(getNameStyleIndex(),
                            getErrorMessageResourceId(),
                            getName(field, getOverrideNameResourceId(annotation)));
                }
            }
        }

        return null;
    }

    protected abstract Class<T> getValidationAnnotationClass();

    protected abstract String getRegex(final T annotation);

    protected abstract int getOverrideNameResourceId(final T annotation);

    protected abstract int getErrorMessageResourceId();

    protected int getNameStyleIndex() {
        return 0;
    }

}
