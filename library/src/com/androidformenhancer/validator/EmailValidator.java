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
import com.androidformenhancer.annotation.Email;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Validates that the value matches the regular expression.
 * 
 * @author Soichiro Kashima
 */
public class EmailValidator extends AbstractRegexValidator<Email> {

    public static final String REGEX_EMAIL = "^[\\w-]+(\\.[\\w-]+)*@([\\w][\\w-]*\\.)+[\\w][\\w-]*$";
    private String mRegex = REGEX_EMAIL;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorDefinitions,
                R.attr.afeValidatorDefinitions, 0);
        int resId = a.getResourceId(R.styleable.ValidatorDefinitions_afeCustomEmailPattern, 0);
        a.recycle();
        if (resId == 0) {
            mRegex = REGEX_EMAIL;
        } else {
            mRegex = getContext().getString(resId);
        }
    }

    @Override
    public Class<Email> getAnnotationClass() {
        return Email.class;
    }

    @Override
    protected String getRegex(final Email annotation) {
        return mRegex;
    }

    @Override
    protected int getOverrideNameResourceId(Email annotation) {
        return annotation.nameResId();
    }

    @Override
    protected int getErrorMessageResourceId() {
        return R.string.afe__msg_validation_email;
    }

    @Override
    protected int getNameStyleIndex() {
        return R.styleable.ValidatorMessages_afeErrorEmail;
    }

}
