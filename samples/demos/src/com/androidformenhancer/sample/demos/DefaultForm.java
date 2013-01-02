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
import com.androidformenhancer.annotation.PastDate;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.WidgetValue;

import java.util.List;

/**
 * @author Soichiro Kashima
 */
public class DefaultForm {

    @Required
    @Multibyte
    @MaxLength(20)
    @Widget(id = R.id.textfield_name, nameResId = R.string.form_default_name)
    public String name;

    @Hiragana
    @MaxLength(20)
    @Widget(id = R.id.textfield_hiragana, nameResId = R.string.form_default_hiragana, validateAfter = R.id.textfield_name)
    public String hiragana;

    @Katakana
    @MaxLength(20)
    @Widget(id = R.id.textfield_katakana, nameResId = R.string.form_default_katakana, validateAfter = R.id.textfield_hiragana)
    public String katakana;

    @IntType
    @MinValue(20)
    @MaxValue(100)
    @Widget(id = R.id.textfield_age, nameResId = R.string.form_default_age, validateAfter = R.id.textfield_katakana)
    public String age;

    @Required
    @Widget(id = R.id.rg_gender,
            nameResId = R.string.form_default_gender,
            validateAfter = R.id.textfield_age,
            values = {
                    @WidgetValue(id = R.id.radio_gender_male, value = "0"),
                    @WidgetValue(id = R.id.radio_gender_female, value = "1")
            })
    public String gender;

    @Digits
    @Widget(id = R.id.textfield_phone, nameResId = R.string.form_default_phone, validateAfter = R.id.rg_gender)
    public String phone;

    @Required
    @DatePattern
    @PastDate
    @Widget(id = R.id.textfield_birthday, nameResId = R.string.form_default_birthday, validateAfter = R.id.textfield_phone)
    public String birthday;

    @Required
    @Widget(id = R.id.spn_credit_card_company,
            nameResId = R.string.form_default_credit_card_company, headIsDummy = true,
            validateAfter = R.id.textfield_birthday)
    public String creditCardCompany;

    @Required
    @Widget(id = R.id.cbg_got_to_know_by,
            nameResId = R.string.form_default_got_to_know_by,
            validateAfter = R.id.spn_credit_card_company,
            atLeast = 2,
            values = {
                    @WidgetValue(id = R.id.cb_got_to_know_by_tv, value = "TV"),
                    @WidgetValue(id = R.id.cb_got_to_know_by_internet, value = "IN"),
                    @WidgetValue(id = R.id.cb_got_to_know_by_twitter, value = "TW"),
                    @WidgetValue(id = R.id.cb_got_to_know_by_facebook, value = "FB"),
            })
    public List<String> gotToKnowBy;
}
