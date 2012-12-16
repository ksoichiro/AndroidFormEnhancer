
package com.androidformenhancer.sample.demos;

import com.androidformenhancer.form.annotation.IntValue;
import com.androidformenhancer.form.annotation.Order;
import com.androidformenhancer.form.annotation.Radio;
import com.androidformenhancer.form.annotation.RadioValue;
import com.androidformenhancer.form.annotation.Required;
import com.androidformenhancer.form.annotation.Text;

public class DefaultForm {

    @Required(nameResId = R.string.form_default_name)
    @Text(id = R.id.textfield_name)
    @Order(1)
    public String name;

    @Required(nameResId = R.string.form_default_age)
    @IntValue(nameResId = R.string.form_default_name)
    @Text(id = R.id.textfield_age)
    @Order(2)
    public String age;

    @Required(nameResId = R.string.form_default_gender)
    @Radio(groupId = R.id.rg_gender,
            values = {
                    @RadioValue(id = R.id.radio_gender_male, value = "0"),
                    @RadioValue(id = R.id.radio_gender_female, value = "1")
            })
    @Order(3)
    public String gender;

}
