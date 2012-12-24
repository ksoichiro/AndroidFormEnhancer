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

import com.androidformenhancer.annotation.DatePattern;
import com.androidformenhancer.annotation.PastDate;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Test case for PastDateValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class PastDateValidatorTest extends InstrumentationTestCase {

    /**
     * Dummy class which has @PastDate field.
     */
    public class Foo {
        @PastDate
        public String a;

        @PastDate(allowToday = true)
        public String b;

        @DatePattern("yyyy.MM.dd")
        @PastDate
        public String c;
    }

    public void testValidate() throws Exception {
        Foo foo = new Foo();

        PastDateValidator validator = new PastDateValidator();
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

        foo.a = "12/32/2000";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "1/1/2000";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "01/01/2000";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2/29/2000";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2/29/2001";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        Calendar c = Calendar.getInstance(Locale.getDefault());
        DateFormat dateFormatUS = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        foo.a = dateFormatUS.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, -1);
        foo.a = dateFormatUS.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, 2);
        foo.a = dateFormatUS.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        field = Foo.class.getDeclaredField("b");

        c = Calendar.getInstance(Locale.getDefault());
        foo.b = dateFormatUS.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, -1);
        foo.b = dateFormatUS.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, 2);
        foo.b = dateFormatUS.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        Locale.setDefault(Locale.JAPAN);

        field = Foo.class.getDeclaredField("a");

        foo.a = "2000/12/32";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.a = "2000/1/1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2000/01/01";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2000/2/29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.a = "2001/2/29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        c = Calendar.getInstance(Locale.getDefault());
        DateFormat dateFormatJAPAN = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.getDefault());
        foo.a = dateFormatJAPAN.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, -1);
        foo.a = dateFormatJAPAN.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, 2);
        foo.a = dateFormatJAPAN.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.a + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        field = Foo.class.getDeclaredField("b");

        c = Calendar.getInstance(Locale.getDefault());
        foo.b = dateFormatJAPAN.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, -1);
        foo.b = dateFormatJAPAN.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, 2);
        foo.b = dateFormatJAPAN.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.b + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        Locale.setDefault(defaultLocale);

        // User defined format

        field = Foo.class.getDeclaredField("c");
        foo.c = "2000.12.31";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.c = "2000.12.32";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        foo.c = "2000.1.1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.c = "2000.01.01";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.c = "2000.2.29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNull(errorMessage);

        foo.c = "2001.2.29";
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        c = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        foo.c = customDateFormat.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNotNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, -1);
        foo.c = customDateFormat.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNull(errorMessage);

        c.add(Calendar.DAY_OF_MONTH, 2);
        foo.c = customDateFormat.format(c.getTime());
        errorMessage = validator.validate(field);
        Log.i("TEST", "input: " + foo.c + ", message: " + errorMessage);
        assertNotNull(errorMessage);
    }

}
