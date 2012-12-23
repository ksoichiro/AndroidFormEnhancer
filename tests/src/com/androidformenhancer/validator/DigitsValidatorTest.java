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

import com.androidformenhancer.form.annotation.Digits;
import com.androidformenhancer.test.DummyActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Test case for DigitsValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class DigitsValidatorTest extends ActivityInstrumentationTestCase2<DummyActivity> {

    public DigitsValidatorTest() {
        super(DummyActivity.class);
    }

    public DigitsValidatorTest(Class<DummyActivity> activityClass) {
        super(activityClass);
    }

    /**
     * Dummy class which has @Digits field.
     */
    public class Foo {
        @Digits
        public String a;
    }

    public void testValidate() throws Exception {
        Foo foo = new Foo();

        DigitsValidator validator = new DigitsValidator();
        validator.setContext(getActivity());
        validator.setTarget(foo);
        Field field = Foo.class.getDeclaredField("a");

        // Digits
        foo.a = "123";
        String errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNull(errorMessage);

        // Not digits
        foo.a = "a";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Partially digits
        foo.a = "a1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Not digits and double byte
        foo.a = "あいうえお";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Digits but double byte
        foo.a = "１２３";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Empty
        foo.a = "";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNull(errorMessage);

        // Null
        foo.a = null;
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNull(errorMessage);
    }
}
