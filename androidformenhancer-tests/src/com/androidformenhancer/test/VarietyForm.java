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

package com.androidformenhancer.test;

import com.androidformenhancer.annotation.AlphaNum;
import com.androidformenhancer.annotation.Digits;
import com.androidformenhancer.annotation.Email;
import com.androidformenhancer.annotation.IntRange;
import com.androidformenhancer.annotation.IntType;
import com.androidformenhancer.annotation.Katakana;
import com.androidformenhancer.annotation.MaxLength;
import com.androidformenhancer.annotation.MaxNumOfDigits;
import com.androidformenhancer.annotation.MaxValue;
import com.androidformenhancer.annotation.MinValue;
import com.androidformenhancer.annotation.Multibyte;
import com.androidformenhancer.annotation.NumOfDigits;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.WidgetValue;

/**
 * @author Soichiro Kashima
 */
public class VarietyForm {

    @Widget(id = R.id.textfield_name)
    public String stringType;

    @Widget(id = R.id.textfield_int)
    public String intType;

    @Widget(id = R.id.textfield_long)
    public String longType;

    @Widget(id = R.id.textfield_float)
    public String floatType;

    @Widget(id = R.id.textfield_double)
    public String doubleType;

    @Widget(id = R.id.textfield_boolean)
    public String booleanType;

    @Widget(id = R.id.textfield_short)
    public String shortType;

    @Widget(id = R.id.textfield_char)
    public String charType;
}
