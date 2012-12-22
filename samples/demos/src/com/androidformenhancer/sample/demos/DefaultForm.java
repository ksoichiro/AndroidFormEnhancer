
package com.androidformenhancer.sample.demos;

import com.androidformenhancer.form.annotation.Digits;
import com.androidformenhancer.form.annotation.IntValue;
import com.androidformenhancer.form.annotation.MaxValue;
import com.androidformenhancer.form.annotation.Order;
import com.androidformenhancer.form.annotation.Radio;
import com.androidformenhancer.form.annotation.RadioValue;
import com.androidformenhancer.form.annotation.Required;
import com.androidformenhancer.form.annotation.Spinner;
import com.androidformenhancer.form.annotation.Text;
import com.androidformenhancer.form.annotation.Validated;

public class DefaultForm {

    @Validated(nameResId = R.string.form_default_name)
    @Required
    @Text(id = R.id.textfield_name)
    @Order(1)
    public String name;

    @Validated(nameResId = R.string.form_default_age)
    @IntValue
    @MaxValue(value = 100)
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
