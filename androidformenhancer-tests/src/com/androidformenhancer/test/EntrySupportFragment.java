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

package com.androidformenhancer.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidformenhancer.ValidationResult;
import com.androidformenhancer.helper.ActivityFormHelper;
import com.androidformenhancer.helper.FormHelper;
import com.androidformenhancer.helper.SupportFragmentFormHelper;

/**
 * @author Soichiro Kashima
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EntrySupportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_entry, container, false);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(v);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // You must not call this in onCreateView() because we cannot access
        // to root view by getView().
        FormHelper helper = new SupportFragmentFormHelper(EntryForm.class, this);
        helper.setAsDateField(R.id.textfield_birthday, R.string.msg_default_date);
        helper.setOnFocusOutValidation();
    }

    public void onSubmit(View v) {
        FormHelper helper = new SupportFragmentFormHelper(EntryForm.class, this);
        ValidationResult result = helper.validate();
        if (result.hasError()) {
            helper.showAlertDialog(result.getAllSerializedErrors());
        } else {
            Toast.makeText(getActivity(), "OK!", Toast.LENGTH_SHORT).show();
        }
    }

}
