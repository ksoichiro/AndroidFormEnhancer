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

import com.androidformenhancer.form.annotation.IntValue;
import com.androidformenhancer.test.DummyActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Test case for NumberValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class NumberValidatorTest extends ActivityInstrumentationTestCase2<DummyActivity> {

    public NumberValidatorTest() {
        super(DummyActivity.class);
    }

    public NumberValidatorTest(Class<DummyActivity> activityClass) {
        super(activityClass);
    }

    /**
     * Dummy class which has @IntValue field.
     */
    public class Foo {
        @IntValue
        public String a;
    }

    public void testValidate() throws Exception {
        Foo foo = new Foo();

        NumberValidator validator = new NumberValidator();
        validator.setContext(getActivity());
        validator.setTarget(foo);
        Field field = Foo.class.getDeclaredField("a");

        // Null
        foo.a = null;
        String errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        // Empty
        foo.a = "";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "0";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "100";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "-1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "1,000";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "1.0";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = Integer.toString(Integer.MAX_VALUE);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        // Invalid validation because of overflow
        foo.a = Long.toString(Integer.MAX_VALUE + 1);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = Integer.toString(Integer.MIN_VALUE);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        // Invalid validation because of overflow
        foo.a = Long.toString(Integer.MIN_VALUE - 1);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "a";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "あ";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);
    }

}
