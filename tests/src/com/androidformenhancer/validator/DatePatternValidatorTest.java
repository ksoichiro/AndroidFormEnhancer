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

import com.androidformenhancer.form.annotation.DatePattern;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Test case for DatePatternValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class DatePatternValidatorTest extends InstrumentationTestCase {

    /**
     * Dummy class which has @DatePattern field.
     */
    public class Foo {
        @DatePattern
        public String a;

        @DatePattern("yyyy.MM.dd")
        public String b;
    }

    public void testValidate() throws Exception {
        Foo foo = new Foo();

        DatePatternValidator validator = new DatePatternValidator();
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

        foo.a = "a";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        // Locale specific tests
        Locale defaultLocale = Locale.getDefault();

        Locale.setDefault(Locale.US);

        foo.a = "12/31/2012";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "12/32/2012";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "1/1/2012";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "01/01/2012";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2/29/2012";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2/29/2011";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        Locale.setDefault(Locale.JAPAN);

        foo.a = "2012/12/31";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2012/12/32";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "2012/1/1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2012/01/01";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2012/2/29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2011/2/29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        Locale.setDefault(defaultLocale);

        // User defined format

        field = Foo.class.getDeclaredField("b");
        foo.b = "2012.12.31";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.b = "2012.12.32";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.b = "2012.1.1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.b = "2012.01.01";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.b = "2012.2.29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.b = "2011.2.29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNotNull(errorMessage);
    }

}
