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

package com.androidformenhancer.sample.demos;

import com.androidformenhancer.annotation.DatePattern;
import com.androidformenhancer.annotation.Digits;
import com.androidformenhancer.annotation.Hiragana;
import com.androidformenhancer.annotation.IntType;
import com.androidformenhancer.annotation.Katakana;
import com.androidformenhancer.annotation.MaxLength;
import com.androidformenhancer.annotation.MaxValue;
import com.androidformenhancer.annotation.MinValue;
import com.androidformenhancer.annotation.Multibyte;
import com.androidformenhancer.annotation.Order;
import com.androidformenhancer.annotation.PastDate;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.Widget.Type;
import com.androidformenhancer.annotation.WidgetValue;

import java.util.List;

/**
 * @author Soichiro Kashima
 */
public class DefaultForm {

    @Required
    @Multibyte
    @MaxLength(20)
    @Widget(id = R.id.textfield_name, type = Type.TEXT, nameResId = R.string.form_default_name)
    @Order(1)
    public String name;

    @Hiragana
    @MaxLength(20)
    @Widget(id = R.id.textfield_hiragana, type = Type.TEXT, nameResId = R.string.form_default_hiragana)
    @Order(2)
    public String hiragana;

    @Katakana
    @MaxLength(20)
    @Widget(id = R.id.textfield_katakana, type = Type.TEXT, nameResId = R.string.form_default_katakana)
    @Order(3)
    public String katakana;

    @IntType
    @MinValue(20)
    @MaxValue(100)
    @Widget(id = R.id.textfield_age, type = Type.TEXT, nameResId = R.string.form_default_age)
    @Order(4)
    public String age;

    @Required
    @Widget(id = R.id.rg_gender,
            type = Type.RADIO,
            nameResId = R.string.form_default_gender,
            values = {
                    @WidgetValue(id = R.id.radio_gender_male, value = "0"),
                    @WidgetValue(id = R.id.radio_gender_female, value = "1")
            })
    @Order(5)
    public String gender;

    @Digits
    @Widget(id = R.id.textfield_phone, type = Type.TEXT, nameResId = R.string.form_default_phone)
    @Order(6)
    public String phone;

    @Required
    @DatePattern
    @PastDate
    @Widget(id = R.id.textfield_birthday, type = Type.TEXT, nameResId = R.string.form_default_birthday)
    @Order(7)
    public String birthday;

    @Required
    @Widget(id = R.id.spn_credit_card_company, type = Type.SPINNER,
            nameResId = R.string.form_default_credit_card_company, headIsDummy = true)
    @Order(8)
    public String creditCardCompany;

    @Required
    @Widget(id = R.id.cbg_got_to_know_by,
            type = Type.CHECKBOX,
            nameResId = R.string.form_default_got_to_know_by,
            atLeast = 2,
            values = {
                    @WidgetValue(id = R.id.cb_got_to_know_by_tv, value = "TV"),
                    @WidgetValue(id = R.id.cb_got_to_know_by_internet, value = "IN"),
                    @WidgetValue(id = R.id.cb_got_to_know_by_twitter, value = "TW"),
                    @WidgetValue(id = R.id.cb_got_to_know_by_facebook, value = "FB"),
            })
    @Order(9)
    public List<String> gotToKnowBy;
}
