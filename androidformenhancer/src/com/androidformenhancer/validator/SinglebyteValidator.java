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

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;

import com.androidformenhancer.FieldData;
import com.androidformenhancer.R;
import com.androidformenhancer.ValidationException;
import com.androidformenhancer.annotation.Singlebyte;

import java.io.UnsupportedEncodingException;

/**
 * Validates that the value of the field consists of single-byte characters or
 * not.
 *
 * @author Soichiro Kashima
 */
public class SinglebyteValidator extends Validator<Singlebyte> {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private String mEncoding;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        setEncoding();
    }

    @Override
    public Class<Singlebyte> getAnnotationClass() {
        return Singlebyte.class;
    }

    @Override
    public String validate(final Singlebyte annotation, final FieldData fieldData) {
        final String value = fieldData.getValueAsString();
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        boolean hasError = false;
        try {
            for (int i = 0; i < value.length(); i = value.offsetByCodePoints(i, 1)) {
                char[] c = Character.toChars(value.codePointAt(i));
                if (c.length > 1) {
                    hasError = true;
                    break;
                }
                byte[] b = new String(c).getBytes(mEncoding);
                if (b.length != 1) {
                    hasError = true;
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new ValidationException("Unsupported encoding used: " + mEncoding, e);
        }
        if (hasError) {
            return getMessage(R.styleable.ValidatorMessages_afeErrorSinglebyte,
                    R.string.afe__msg_validation_singlebyte,
                    getName(fieldData, annotation.nameResId()));
        }

        return null;
    }

    public String getEncoding() {
        return mEncoding;
    }

    public void setEncoding(final String encoding) {
        mEncoding = encoding;
    }

    private void setEncoding() {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorDefinitions,
                R.attr.afeValidatorDefinitions, 0);

        mEncoding = a.getString(R.styleable.ValidatorDefinitions_afeCharacterEncoding);
        if (TextUtils.isEmpty(mEncoding)) {
            mEncoding = DEFAULT_ENCODING;
        }

        a.recycle();
    }

}
