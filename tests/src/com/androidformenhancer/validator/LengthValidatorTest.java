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
import com.androidformenhancer.annotation.Length;
import com.androidformenhancer.annotation.Widget;

import java.lang.reflect.Field;

/**
 * Test case for LengthValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class LengthValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @Length field.
     */
    public class Foo {
        @Length(5)
        @Widget(id = 0)
        public String a;
    }

    public void testValidate() throws Exception {
        LengthValidator validator = new LengthValidator();
        validator.setContext(getInstrumentation().getContext());

        Field field = Foo.class.getDeclaredField("a");
        FieldData fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue(null);
        validate(validator, fieldData, true);

        fieldData.setValue("");
        validate(validator, fieldData, true);

        fieldData.setValue("      ");
        validate(validator, fieldData, false);

        fieldData.setValue("　　　　　　");
        validate(validator, fieldData, false);

        fieldData.setValue("a");
        validate(validator, fieldData, false);

        fieldData.setValue("ab");
        validate(validator, fieldData, false);

        // Boundary with ASCII
        fieldData.setValue("abcde");
        validate(validator, fieldData, true);

        // Boundary +1 with ASCII
        fieldData.setValue("abcdef");
        validate(validator, fieldData, false);

        fieldData.setValue("あ");
        validate(validator, fieldData, false);

        fieldData.setValue("あい");
        validate(validator, fieldData, false);

        // Boundary with Multi-byte
        fieldData.setValue("あいうえお");
        validate(validator, fieldData, true);

        fieldData.setValue("あいうえおか");
        validate(validator, fieldData, false);

        // Boundary with Multi-byte including surrogate pair
        fieldData.setValue("あいう𠮷");
        validate(validator, fieldData, true);

        fieldData.setValue("あいうえ𠮷");
        validate(validator, fieldData, false);
    }

}
