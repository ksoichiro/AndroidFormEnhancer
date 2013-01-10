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
import com.androidformenhancer.annotation.FloatType;
import com.androidformenhancer.annotation.Widget;

import java.lang.reflect.Field;

/**
 * Test case for FloatTypeValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class FloatTypeValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @FloatType field.
     */
    public class Foo {
        @FloatType
        @Widget(id = 0)
        public String a;
    }

    public void testValidate() throws Exception {
        FloatTypeValidator validator = new FloatTypeValidator();
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

        fieldData.setValue("0");
        validate(validator, fieldData, true);

        fieldData.setValue("1");
        validate(validator, fieldData, true);

        fieldData.setValue("100");
        validate(validator, fieldData, true);

        fieldData.setValue("-1");
        validate(validator, fieldData, true);

        fieldData.setValue("1,000");
        validate(validator, fieldData, false);

        fieldData.setValue("1.0");
        validate(validator, fieldData, true);

        fieldData.setValue(Double.toString(Float.MAX_VALUE));
        validate(validator, fieldData, true);

        // Invalid validation because of overflow
        fieldData.setValue(Double.toString(Float.MAX_VALUE + 1));
        validate(validator, fieldData, true);

        fieldData.setValue(Double.toString(Float.MIN_VALUE));
        validate(validator, fieldData, true);

        // Invalid validation because of overflow
        fieldData.setValue(Double.toString(Float.MIN_VALUE - 1));
        validate(validator, fieldData, true);

        fieldData.setValue("あ");
        validate(validator, fieldData, false);
    }

}
