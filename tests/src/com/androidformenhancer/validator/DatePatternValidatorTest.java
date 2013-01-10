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
import com.androidformenhancer.annotation.DatePattern;
import com.androidformenhancer.annotation.Widget;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Test case for DatePatternValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class DatePatternValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @DatePattern field.
     */
    public class Foo {
        @DatePattern
        @Widget(id = 0)
        public String a;

        @DatePattern("yyyy.MM.dd")
        @Widget(id = 0)
        public String b;
    }

    public void testValidate() throws Exception {
        DatePatternValidator validator = new DatePatternValidator();
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

        fieldData.setValue("12/31/2012");
        validate(validator, fieldData, true);

        fieldData.setValue("12/32/2012");
        validate(validator, fieldData, false);

        fieldData.setValue("1/1/2012");
        validate(validator, fieldData, true);

        fieldData.setValue("01/01/2012");
        validate(validator, fieldData, true);

        fieldData.setValue("2/29/2012");
        validate(validator, fieldData, true);

        fieldData.setValue("2/29/2011");
        validate(validator, fieldData, false);

        Locale.setDefault(Locale.JAPAN);

        fieldData.setValue("2012/12/31");
        validate(validator, fieldData, true);

        fieldData.setValue("2012/12/32");
        validate(validator, fieldData, false);

        fieldData.setValue("2012/1/1");
        validate(validator, fieldData, true);

        fieldData.setValue("2012/01/01");
        validate(validator, fieldData, true);

        fieldData.setValue("2012/2/29");
        validate(validator, fieldData, true);

        fieldData.setValue("2011/2/29");
        validate(validator, fieldData, false);

        Locale.setDefault(defaultLocale);

        // User defined format
        field = Foo.class.getDeclaredField("b");
        fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue("2012.12.31");
        validate(validator, fieldData, true);

        fieldData.setValue("2012.12.32");
        validate(validator, fieldData, false);

        fieldData.setValue("2012.1.1");
        validate(validator, fieldData, true);

        fieldData.setValue("2012.01.01");
        validate(validator, fieldData, true);

        fieldData.setValue("2012.2.29");
        validate(validator, fieldData, true);

        fieldData.setValue("2011.2.29");
        validate(validator, fieldData, false);
    }

}
