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
import com.androidformenhancer.annotation.Hiragana;
import com.androidformenhancer.annotation.Widget;

import java.lang.reflect.Field;

/**
 * Test case for HiraganaValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class HiraganaValidatorTest extends ValidatorTest {

    /**
     * Dummy class which has @Hiragana field.
     */
    public class Foo {
        @Hiragana
        @Widget(id = 0)
        public String a;
    }

    public void testValidate() throws Exception {
        HiraganaValidator validator = new HiraganaValidator();
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

        fieldData.setValue("1");
        validate(validator, fieldData, false);

        fieldData
                .setValue("いうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんゃゅょっぁぃぅぇぉがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽゔー、。あ");
        validate(validator, fieldData, true);

        fieldData.setValue("ア");
        validate(validator, fieldData, false);

        fieldData.setValue("亜");
        validate(validator, fieldData, false);

        fieldData.setValue("あア");
        validate(validator, fieldData, false);
    }

}
