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

import android.test.InstrumentationTestCase;
import android.util.Log;

/**
 * Convenience class for testing validators.
 * 
 * @author Soichiro Kashima
 */
public class ValidatorTest extends InstrumentationTestCase {

    public void validate(final Validator<?> validator, final FieldData fieldData,
            final boolean expectValid)
            throws Exception {
        String errorMessage = validator.validate(fieldData);
        Log.i("TEST", "input: " + fieldData.getValue() + ", message: " + errorMessage);
        if (expectValid) {
            assertNull(errorMessage);
        } else {
            assertNotNull(errorMessage);
        }
    }

}
