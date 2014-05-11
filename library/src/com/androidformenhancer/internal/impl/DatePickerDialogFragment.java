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

package com.androidformenhancer.internal.impl;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.androidformenhancer.internal.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DatePickerDialogFragment extends android.app.DialogFragment implements DialogFragment<FragmentTransaction> {
    private static final String ARG_TARGET_VIEW_RES_ID = "targetViewResId";
    private static final String ARG_DEFAULT_MESSAGE_RES_ID = "defaultMessageResId";

    public static class Builder {
        private int mTargetViewResId;
        private int mDefaultMessageResId;

        public Builder setTargetViewResId(final int resId) {
            mTargetViewResId = resId;
            return this;
        }

        public Builder setDefaultMessageResId(final int resId) {
            mDefaultMessageResId = resId;
            return this;
        }

        public DatePickerDialogFragment create() {
            Bundle args = new Bundle();
            if (mTargetViewResId != 0) {
                args.putInt(ARG_TARGET_VIEW_RES_ID, mTargetViewResId);
            }
            if (mDefaultMessageResId != 0) {
                args.putInt(ARG_DEFAULT_MESSAGE_RES_ID, mDefaultMessageResId);
            }
            DatePickerDialogFragment fragment = new DatePickerDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }
    }

    public DatePickerDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        int id = args.getInt(ARG_TARGET_VIEW_RES_ID);
        View v = getActivity().findViewById(id);
        if (v == null || !(v instanceof TextView)) {
            throw new IllegalArgumentException("Target view must be valid TextView: " + v);
        }
        final TextView tv = (TextView) v;
        if (TextUtils.isEmpty(tv.getText())) {
            int defaultMessageId = args.getInt(ARG_DEFAULT_MESSAGE_RES_ID);
            tv.setText(getActivity().getString(defaultMessageId));
        }

        DatePickerDialog.OnDateSetListener callBack =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = Calendar.getInstance(Locale.getDefault());
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        tv.setText(DateFormat
                                .getDateInstance(DateFormat.SHORT, Locale.getDefault())
                                .format(c.getTime()));
                    }
                };
        Calendar c = Calendar.getInstance();
        final Dialog dialog = new DatePickerDialog(getActivity(), callBack, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)) {
        };
        return dialog;
    }
}
