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

import com.androidformenhancer.annotation.AlphaNum;
import com.androidformenhancer.annotation.Digits;
import com.androidformenhancer.annotation.Email;
import com.androidformenhancer.annotation.IntRange;
import com.androidformenhancer.annotation.IntType;
import com.androidformenhancer.annotation.Katakana;
import com.androidformenhancer.annotation.MaxLength;
import com.androidformenhancer.annotation.MaxNumOfDigits;
import com.androidformenhancer.annotation.MinValue;
import com.androidformenhancer.annotation.Multibyte;
import com.androidformenhancer.annotation.NumOfDigits;
import com.androidformenhancer.annotation.Required;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.WidgetValue;

/**
 * @author Soichiro Kashima
 */
public class EntryForm {

    @Required
    @Multibyte
    @MaxLength(20)
    @Widget(id = R.id.textfield_last_name, nameResId = R.string.form_entry_last_name)
    public String lastName;

    @Required
    @Multibyte
    @MaxLength(20)
    @Widget(id = R.id.textfield_first_name, nameResId = R.string.form_entry_first_name, validateAfter = R.id.textfield_last_name)
    public String firstName;

    @Required
    @Katakana
    @MaxLength(20)
    @Widget(id = R.id.textfield_last_name_kana, nameResId = R.string.form_entry_last_name_kana, validateAfter = R.id.textfield_first_name)
    public String lastNameKana;

    @Required
    @Katakana
    @MaxLength(20)
    @Widget(id = R.id.textfield_first_name_kana, nameResId = R.string.form_entry_first_name_kana, validateAfter = R.id.textfield_last_name_kana)
    public String firstNameKana;

    @Required
    @Widget(id = R.id.rg_gender,
            nameResId = R.string.form_entry_gender,
            validateAfter = R.id.textfield_first_name_kana,
            values = {
                    @WidgetValue(id = R.id.rb_gender_male, value = "1"),
                    @WidgetValue(id = R.id.rb_gender_female, value = "2")
            })
    public String gender;

    @Required
    @Digits
    @MaxNumOfDigits(11)
    @Widget(id = R.id.textfield_phone_mobile, nameResId = R.string.form_entry_phone_mobile, validateAfter = R.id.textfield_phone_home)
    public String phoneMobile;

    @Digits
    @MaxNumOfDigits(11)
    @Widget(id = R.id.textfield_phone_home, nameResId = R.string.form_entry_phone_home, validateAfter = R.id.rg_gender)
    public String phoneHome;

    @Email
    @Widget(id = R.id.textfield_email_mobile, nameResId = R.string.form_entry_email_mobile, validateAfter = R.id.textfield_phone_mobile)
    public String emailMobile;

    @Email
    @Widget(id = R.id.textfield_email_pc, nameResId = R.string.form_entry_email_pc, validateAfter = R.id.textfield_email_mobile)
    public String emailPc;

    @Required
    @Digits
    @NumOfDigits(7)
    @Widget(id = R.id.textfield_zip_code, nameResId = R.string.form_entry_zip_code, validateAfter = R.id.textfield_email_pc)
    public String zipCode;

    @Required
    @Multibyte
    @Widget(id = R.id.textfield_address1, nameResId = R.string.form_entry_address1, validateAfter = R.id.textfield_zip_code)
    public String address1;

    @Required
    @Multibyte
    @Widget(id = R.id.textfield_address2, nameResId = R.string.form_entry_address2, validateAfter = R.id.textfield_address1)
    public String address2;

    @Required
    @Multibyte
    @Widget(id = R.id.textfield_address3, nameResId = R.string.form_entry_address3, validateAfter = R.id.textfield_address2)
    public String address3;

    @Required
    @Multibyte
    @Widget(id = R.id.textfield_address4, nameResId = R.string.form_entry_address4, validateAfter = R.id.textfield_address3)
    public String address4;

    @Required
    @AlphaNum
    @Widget(id = R.id.textfield_password, nameResId = R.string.form_entry_password, validateAfter = R.id.textfield_address4)
    public String password;

    @Required
    @AlphaNum
    @Widget(id = R.id.textfield_password_retype, nameResId = R.string.form_entry_password_retype, validateAfter = R.id.textfield_password)
    public String passwordRetype;

    @Required
    @Widget(id = R.id.spn_residence_type,
            nameResId = R.string.form_entry_residence_type, headIsDummy = true,
            validateAfter = R.id.textfield_password_retype)
    public String residenceType;

    @IntType
    @MinValue(1)
    @Widget(id = R.id.textfield_residence_year, nameResId = R.string.form_entry_residence_year, validateAfter = R.id.spn_residence_type)
    public String residenceYear;

    @Required
    @IntType
    @IntRange(min = 0, max = 11)
    @Widget(id = R.id.textfield_residence_month, nameResId = R.string.form_entry_residence_month, validateAfter = R.id.textfield_residence_year)
    public String residenceMonth;

}
