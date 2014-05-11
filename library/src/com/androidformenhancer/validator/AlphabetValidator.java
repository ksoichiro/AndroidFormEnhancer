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
import com.androidformenhancer.annotation.Alphabet;

/**
 * Validates that the value of the field consists of ASCII alphabet characters
 * or not.
 *
 * @author Soichiro Kashima
 */
public class AlphabetValidator extends AbstractRegexValidator<Alphabet> {

    private static final String REGEX = "^[a-zA-Z]+$";
    private static final String REGEX_WITH_SPACE = "^[a-zA-Z ]+$";

    @Override
    public Class<Alphabet> getAnnotationClass() {
        return Alphabet.class;
    }

    @Override
    protected String getRegex(final Alphabet annotation) {
        return annotation.allowSpace() ? REGEX_WITH_SPACE : REGEX;
    }

    @Override
    protected int getOverrideNameResourceId(final Alphabet annotation) {
        return annotation.nameResId();
    }

    @Override
    protected int getErrorMessageResourceId() {
        return R.string.afe__msg_validation_alphabet;
    }

    @Override
    protected int getNameStyleIndex() {
        return R.styleable.ValidatorMessages_afeErrorAlphabet;
    }

}
