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

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.androidformenhancer.ValidationResult;
import com.androidformenhancer.helper.ActivityFormHelper;

/**
 * @author Soichiro Kashima
 */
public class MultipleCheckboxActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_checkbox);

        setupCheckboxes();
    }

    public void onSubmit(View v) {
        ActivityFormHelper helper = new ActivityFormHelper(MultipleCheckboxForm.class, this);
        ValidationResult result = helper.validate();
        if (result.hasError()) {
            Toast.makeText(this, result.getAllSerializedErrors(), Toast.LENGTH_SHORT).show();
        } else {
            // Create entity and do what you want
            // e.g. insert into database, send to server by HTTP
            MultipleCheckboxEntity entity = helper.create(MultipleCheckboxEntity.class);
            Toast.makeText(this, "OK! " + entity.cadidates, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCheckboxes() {
        ViewGroup parent = (ViewGroup) findViewById(R.id.cbg_dynamic_multiple);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 10; i++) {
            ViewGroup group = (ViewGroup) inflater.inflate(R.layout.multiple_checkbox_group, null);
            ((CheckBox) group.findViewById(R.id.cb1)).setText((i + 1) + "-1");
            ((CheckBox) group.findViewById(R.id.cb1)).setTag("value" + (i + 1) + "-1");
            ((CheckBox) group.findViewById(R.id.cb2)).setText((i + 1) + "-2");
            ((CheckBox) group.findViewById(R.id.cb2)).setTag("value" + (i + 1) + "-2");
            ((CheckBox) group.findViewById(R.id.cb3)).setText((i + 1) + "-3");
            ((CheckBox) group.findViewById(R.id.cb3)).setTag("value" + (i + 1) + "-3");
            parent.addView(group);
        }
    }
}
