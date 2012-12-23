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

import com.androidformenhancer.form.annotation.Order;
import com.androidformenhancer.form.annotation.Required;
import com.androidformenhancer.form.annotation.Text;
import com.androidformenhancer.form.annotation.Validated;

/**
 * @author Soichiro Kashima
 */
public class SampleCustomForm {

    @Validated(nameResId = R.string.form_sample_custom_sample)
    @Required
    @SampleAnnotation
    @Text(id = R.id.textfield_sample)
    @Order(1)
    public String sample;

}
