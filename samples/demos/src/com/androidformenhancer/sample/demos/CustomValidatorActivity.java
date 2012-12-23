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

import com.androidformenhancer.utils.FormHelper;
import com.androidformenhancer.utils.StringUtils;
import com.androidformenhancer.validator.ValidationManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author Soichiro Kashima
 */
public class CustomValidatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_validator);
    }

    public void onSubmit(View v) {
        FormHelper<SampleCustomForm> helper = new FormHelper<SampleCustomForm>();

        SampleCustomForm form = helper.extractFormFromView(this, SampleCustomForm.class);

        // Validation
        ValidationManager validationManager = new ValidationManager(this);
        ArrayList<String> errorMessages = validationManager.validate(form);
        if (errorMessages.size() > 0) {
            // Error
            Toast.makeText(
                    this,
                    StringUtils.serialize(errorMessages),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "OK!", Toast.LENGTH_SHORT).show();
        }
    }

}
