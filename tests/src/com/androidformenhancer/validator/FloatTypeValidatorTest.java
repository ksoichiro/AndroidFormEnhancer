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

import com.androidformenhancer.annotation.FloatType;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Test case for FloatTypeValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class FloatTypeValidatorTest extends InstrumentationTestCase {

    /**
     * Dummy class which has @FloatType field.
     */
    public class Foo {
        @FloatType
        public String a;
    }

    public void testValidate() throws Exception {
        Foo foo = new Foo();

        FloatTypeValidator validator = new FloatTypeValidator();
        validator.setContext(getInstrumentation().getContext());
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

        foo.a = " ";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "　";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

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
        assertNull(errorMessage);

        foo.a = Float.toString(Float.MAX_VALUE);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        // Invalid validation because of overflow
        foo.a = Double.toString(Float.MAX_VALUE + 1);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = Float.toString(Float.MIN_VALUE);
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        // Invalid validation because of overflow
        foo.a = Double.toString(Float.MIN_VALUE - 1);
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
