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
import com.androidformenhancer.annotation.AlphaNum;
import com.androidformenhancer.annotation.Widget;

import java.lang.reflect.Field;

/**
 * Test case for AlphaNumValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class AlphaNumValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @AlphaNum field.
     */
    public class Foo {
        @AlphaNum
        @Widget(id = 0)
        public String a;

        @AlphaNum(allowSpace = true)
        @Widget(id = 0)
        public String b;
    }

    public void testValidate() throws Exception {
        AlphaNumValidator validator = new AlphaNumValidator();
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
        validate(validator, fieldData, true);

        fieldData.setValue("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        validate(validator, fieldData, true);

        fieldData.setValue("1");
        validate(validator, fieldData, true);

        fieldData.setValue("あ");
        validate(validator, fieldData, false);

        fieldData.setValue("ア");
        validate(validator, fieldData, false);

        fieldData.setValue("亜");
        validate(validator, fieldData, false);

        field = Foo.class.getDeclaredField("b");
        fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue(" ");
        validate(validator, fieldData, true);
    }

}
