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

import com.androidformenhancer.annotation.CheckBoxGroup;
import com.androidformenhancer.annotation.CheckBoxValue;
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
import com.androidformenhancer.annotation.Radio;
import com.androidformenhancer.annotation.RadioValue;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.Spinner;
import com.androidformenhancer.annotation.Text;
import com.androidformenhancer.annotation.Validated;

import java.util.List;

/**
 * @author Soichiro Kashima
 */
public class DefaultForm {

    @Validated(nameResId = R.string.form_default_name)
    @Required
    @Multibyte
    @MaxLength(20)
    @Text(id = R.id.textfield_name)
    @Order(1)
    public String name;

    @Validated(nameResId = R.string.form_default_hiragana)
    @Hiragana
    @MaxLength(20)
    @Text(id = R.id.textfield_hiragana)
    @Order(2)
    public String hiragana;

    @Validated(nameResId = R.string.form_default_katakana)
    @Katakana
    @MaxLength(20)
    @Text(id = R.id.textfield_katakana)
    @Order(3)
    public String katakana;

    @Validated(nameResId = R.string.form_default_age)
    @IntType
    @MinValue(20)
    @MaxValue(100)
    @Text(id = R.id.textfield_age)
    @Order(4)
    public String age;

    @Validated(nameResId = R.string.form_default_gender)
    @Required
    @Radio(groupId = R.id.rg_gender,
            values = {
                    @RadioValue(id = R.id.radio_gender_male, value = "0"),
                    @RadioValue(id = R.id.radio_gender_female, value = "1")
            })
    @Order(5)
    public String gender;

    @Validated(nameResId = R.string.form_default_phone)
    @Digits
    @Text(id = R.id.textfield_phone)
    @Order(6)
    public String phone;

    @Validated(nameResId = R.string.form_default_birthday)
    @Required
    @DatePattern
    @PastDate
    @Text(id = R.id.textfield_birthday)
    @Order(7)
    public String birthday;

    @Validated(nameResId = R.string.form_default_credit_card_company)
    @Required
    @Spinner(id = R.id.spn_credit_card_company, headIsDummy = true)
    @Order(8)
    public String creditCardCompany;

    @Validated(nameResId = R.string.form_default_got_to_know_by)
    @Required
    @CheckBoxGroup(groupId = R.id.cbg_got_to_know_by,
            atLeast = 2,
            values = {
                    @CheckBoxValue(id = R.id.cb_got_to_know_by_tv, value = "TV"),
                    @CheckBoxValue(id = R.id.cb_got_to_know_by_internet, value = "IN"),
                    @CheckBoxValue(id = R.id.cb_got_to_know_by_twitter, value = "TW"),
                    @CheckBoxValue(id = R.id.cb_got_to_know_by_facebook, value = "FB"),
            })
    @Order(9)
    public List<String> gotToKnowBy;
}
