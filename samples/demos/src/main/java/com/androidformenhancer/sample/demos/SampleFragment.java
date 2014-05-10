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

import com.androidformenhancer.FormHelper;
import com.androidformenhancer.ValidationResult;
import com.androidformenhancer.internal.impl.SupportV4FragmentFormHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author Soichiro Kashima
 */
public class SampleFragment extends Fragment {

    private static final String DIALOG_TAG = "dialog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);
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
        SupportV4FragmentFormHelper helper = new SupportV4FragmentFormHelper(DefaultForm.class, this);
        helper.setOnFocusOutValidation();
    }

    public void onSubmit(View v) {
        SupportV4FragmentFormHelper helper = new SupportV4FragmentFormHelper(DefaultForm.class, this);
        ValidationResult result = helper.validate();
        if (result.hasError()) {
            showAlertDialog(result.getAllSerializedErrors());
        } else {
            // Create entity and do what you want
            // e.g. insert into database, send to server by HTTP
            DefaultEntity entity = helper.create(DefaultEntity.class);
            Toast.makeText(getActivity(), "OK!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(final String message) {
        SampleAlertDialogFragment f = new SampleAlertDialogFragment();
        f.setMessage(message);
        showDialogFragment(f);
    }

    public void showDialogFragment(final DialogFragment dialogFragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        if (dialogFragment != null) {
            dialogFragment.show(ft, DIALOG_TAG);
        }
    }

    public static class SampleAlertDialogFragment extends DialogFragment {
        private static final String ARG_MESSAGE = "message";

        private void setMessage(final String message) {
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString(ARG_MESSAGE, message);
            setArguments(args);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            Bundle args = getArguments();
            String message = args.getString(ARG_MESSAGE);
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, null);
            Dialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
