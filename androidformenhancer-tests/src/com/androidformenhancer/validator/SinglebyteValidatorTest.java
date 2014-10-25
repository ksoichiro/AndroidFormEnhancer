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
import com.androidformenhancer.ValidationException;
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.Singlebyte;
import com.androidformenhancer.annotation.Widget;

import java.lang.reflect.Field;

/**
 * Test case for MultibyteValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class SinglebyteValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @Singlebyte field.
     */
    public class Foo {
        @Singlebyte
        @Widget(id = 0)
        public String a;
    }

    public void testValidate() throws Exception {
        SinglebyteValidator validator = new SinglebyteValidator();
        validator.setContext(getInstrumentation().getContext());

        Field field = Foo.class.getDeclaredField("a");
        FieldData fieldData = new FieldData(field, WidgetType.TEXT);

        fieldData.setValue(null);
        validate(validator, fieldData, true);

        fieldData.setValue("");
        validate(validator, fieldData, true);

        fieldData.setValue("1");
        validate(validator, fieldData, true);

        fieldData.setValue("a");
        validate(validator, fieldData, true);

        fieldData.setValue(" ");
        validate(validator, fieldData, true);

        fieldData.setValue("　");
        validate(validator, fieldData, false);

        fieldData.setValue("あ");
        validate(validator, fieldData, false);

        fieldData.setValue("あ1");
        validate(validator, fieldData, false);

        fieldData.setValue("あ𠮷");
        validate(validator, fieldData, false);

        fieldData.setValue("𠮷");
        validate(validator, fieldData, false);

        validator.setEncoding("SJIS");

        fieldData.setValue(new String("1".getBytes("SJIS"), "SJIS"));
        validate(validator, fieldData, true);

        fieldData.setValue(new String("あ".getBytes("SJIS"), "SJIS"));
        validate(validator, fieldData, false);

        fieldData.setValue(new String("あ1".getBytes("SJIS"), "SJIS"));
        validate(validator, fieldData, false);

        fieldData.setValue(new String("予定表".getBytes("SJIS"), "SJIS"));
        validate(validator, fieldData, false);
    }

    public void testEncoding() throws Throwable {
        SinglebyteValidator validator = new SinglebyteValidator();
        validator.setEncoding("UTF-8");
        assertEquals("UTF-8", validator.getEncoding());
    }

    public void testSetContext() {
        SinglebyteValidator validator = new SinglebyteValidator();
        validator.setContext(getInstrumentation().getContext());

        validator.setEncoding("UTF-8");
        validator.setContext(getInstrumentation().getContext());
    }

    public void testUnsupportedEncoding() throws Throwable {
        try {
            SinglebyteValidator validator = new SinglebyteValidator();
            validator.setContext(getInstrumentation().getContext());

            Field field = Foo.class.getDeclaredField("a");
            FieldData fieldData = new FieldData(field, WidgetType.TEXT);

            validator.setEncoding("FOO");
            fieldData.setValue("a");
            validate(validator, fieldData, true);
        } catch (ValidationException e) {
            assertEquals("Unsupported encoding used: FOO", e.getMessage());
        }
    }

}
