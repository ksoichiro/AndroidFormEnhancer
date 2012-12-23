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

import com.androidformenhancer.form.annotation.Digits;
import com.androidformenhancer.form.annotation.IntType;
import com.androidformenhancer.form.annotation.MaxLength;
import com.androidformenhancer.form.annotation.MaxValue;
import com.androidformenhancer.form.annotation.MinValue;
import com.androidformenhancer.form.annotation.Multibyte;
import com.androidformenhancer.form.annotation.Order;
import com.androidformenhancer.form.annotation.Radio;
import com.androidformenhancer.form.annotation.RadioValue;
import com.androidformenhancer.form.annotation.Required;
import com.androidformenhancer.form.annotation.Spinner;
import com.androidformenhancer.form.annotation.Text;
import com.androidformenhancer.form.annotation.Validated;

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

    @Validated(nameResId = R.string.form_default_age)
    @IntType
    @MinValue(20)
    @MaxValue(100)
    @Text(id = R.id.textfield_age)
    @Order(2)
    public String age;

    @Validated(nameResId = R.string.form_default_gender)
    @Required
    @Radio(groupId = R.id.rg_gender,
            values = {
                    @RadioValue(id = R.id.radio_gender_male, value = "0"),
                    @RadioValue(id = R.id.radio_gender_female, value = "1")
            })
    @Order(3)
    public String gender;

    @Validated(nameResId = R.string.form_default_phone)
    @Digits
    @Text(id = R.id.textfield_phone)
    @Order(4)
    public String phone;

    @Validated(nameResId = R.string.form_default_credit_card_company)
    @Required
    @Spinner(id = R.id.spn_credit_card_company, headIsDummy = true)
    @Order(5)
    public String creditCardCompany;

}
