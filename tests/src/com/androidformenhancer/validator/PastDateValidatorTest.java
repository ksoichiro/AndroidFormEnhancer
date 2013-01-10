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
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.PastDate;
import com.androidformenhancer.annotation.Widget;

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
public class PastDateValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @PastDate field.
     */
    public class Foo {
        @PastDate
        @Widget(id = 0)
        public String a;

        @PastDate(allowToday = true)
        @Widget(id = 0)
        public String b;

        @PastDate("yyyy.MM.dd")
        @Widget(id = 0)
        public String c;
    }

    public void testValidate() throws Exception {
        PastDateValidator validator = new PastDateValidator();
        validator.setContext(getInstrumentation().getContext());

        Field field = Foo.class.getDeclaredField("a");
        FieldData fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue(null);
        validate(validator, fieldData, true);

        fieldData.setValue("");
        validate(validator, fieldData, true);

        fieldData.setValue(" ");
        validate(validator, fieldData, false);

        fieldData.setValue("　");
        validate(validator, fieldData, false);

        fieldData.setValue("a");
        validate(validator, fieldData, false);

        // Locale specific tests
        Locale defaultLocale = Locale.getDefault();

        Locale.setDefault(Locale.US);

        fieldData.setValue("12/32/2000");
        validate(validator, fieldData, false);

        fieldData.setValue("1/1/2000");
        validate(validator, fieldData, true);

        fieldData.setValue("01/01/2000");
        validate(validator, fieldData, true);

        fieldData.setValue("2/29/2000");
        validate(validator, fieldData, true);

        fieldData.setValue("2/29/2001");
        validate(validator, fieldData, false);

        Calendar c = Calendar.getInstance(Locale.getDefault());
        DateFormat dateFormatUS = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        // Today
        fieldData.setValue(dateFormatUS.format(c.getTime()));
        validate(validator, fieldData, false);

        // Yesterday
        c.add(Calendar.DAY_OF_MONTH, -1);
        fieldData.setValue(dateFormatUS.format(c.getTime()));
        validate(validator, fieldData, true);

        // Tomorrow
        c.add(Calendar.DAY_OF_MONTH, 2);
        fieldData.setValue(dateFormatUS.format(c.getTime()));
        validate(validator, fieldData, false);

        field = Foo.class.getDeclaredField("b");
        fieldData = new FieldData(field, WidgetType.TEXT);
        c = Calendar.getInstance(Locale.getDefault());
        fieldData.setValue(dateFormatUS.format(c.getTime()));
        validate(validator, fieldData, true);

        // Yesterday
        c.add(Calendar.DAY_OF_MONTH, -1);
        fieldData.setValue(dateFormatUS.format(c.getTime()));
        validate(validator, fieldData, true);

        // Tomorrow
        c.add(Calendar.DAY_OF_MONTH, 2);
        fieldData.setValue(dateFormatUS.format(c.getTime()));
        validate(validator, fieldData, false);

        Locale.setDefault(Locale.JAPAN);

        field = Foo.class.getDeclaredField("a");
        fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue("2000/12/32");
        validate(validator, fieldData, false);

        fieldData.setValue("2000/1/1");
        validate(validator, fieldData, true);

        fieldData.setValue("2000/01/01");
        validate(validator, fieldData, true);

        fieldData.setValue("2000/2/29");
        validate(validator, fieldData, true);

        fieldData.setValue("2001/2/29");
        validate(validator, fieldData, false);

        c = Calendar.getInstance(Locale.getDefault());
        DateFormat dateFormatJAPAN = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.getDefault());

        // Today
        fieldData.setValue(dateFormatJAPAN.format(c.getTime()));
        validate(validator, fieldData, false);

        // Yesterday
        c.add(Calendar.DAY_OF_MONTH, -1);
        fieldData.setValue(dateFormatJAPAN.format(c.getTime()));
        validate(validator, fieldData, true);

        // Tomorrow
        c.add(Calendar.DAY_OF_MONTH, 2);
        fieldData.setValue(dateFormatJAPAN.format(c.getTime()));
        validate(validator, fieldData, false);

        field = Foo.class.getDeclaredField("b");
        fieldData = new FieldData(field, WidgetType.TEXT);
        c = Calendar.getInstance(Locale.getDefault());

        // Today
        fieldData.setValue(dateFormatJAPAN.format(c.getTime()));
        validate(validator, fieldData, true);

        // Yesterday
        c.add(Calendar.DAY_OF_MONTH, -1);
        fieldData.setValue(dateFormatJAPAN.format(c.getTime()));
        validate(validator, fieldData, true);

        // Tomorrow
        c.add(Calendar.DAY_OF_MONTH, 2);
        fieldData.setValue(dateFormatJAPAN.format(c.getTime()));
        validate(validator, fieldData, false);

        Locale.setDefault(defaultLocale);

        // User defined format
        field = Foo.class.getDeclaredField("c");
        fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue("2000.12.31");
        validate(validator, fieldData, true);

        fieldData.setValue("2000.12.32");
        validate(validator, fieldData, false);

        fieldData.setValue("2000.1.1");
        validate(validator, fieldData, true);

        fieldData.setValue("2000.01.01");
        validate(validator, fieldData, true);

        fieldData.setValue("2000.2.29");
        validate(validator, fieldData, true);

        fieldData.setValue("2001.2.29");
        validate(validator, fieldData, false);

        c = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

        // Today
        fieldData.setValue(customDateFormat.format(c.getTime()));
        validate(validator, fieldData, false);

        // Yesterday
        c.add(Calendar.DAY_OF_MONTH, -1);
        fieldData.setValue(customDateFormat.format(c.getTime()));
        validate(validator, fieldData, true);

        // Tomorrow
        c.add(Calendar.DAY_OF_MONTH, 2);
        fieldData.setValue(customDateFormat.format(c.getTime()));
        validate(validator, fieldData, false);
    }

}
