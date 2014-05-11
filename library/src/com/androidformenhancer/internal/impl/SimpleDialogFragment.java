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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.androidformenhancer.internal.DialogFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SimpleDialogFragment extends android.app.DialogFragment implements DialogFragment<FragmentTransaction> {
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_CANCELABLE = "cancelable";

    public static class Builder {
        private String mTitle;
        private String mMessage;
        private boolean mCancelable = true;

        public Builder setTitle(final String title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(final String message) {
            mMessage = message;
            return this;
        }

        public Builder setCancelable(final boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public SimpleDialogFragment create() {
            Bundle args = new Bundle();
            if (mTitle != null) {
                args.putString(ARG_TITLE, mTitle);
            }
            if (mMessage != null) {
                args.putString(ARG_MESSAGE, mMessage);
            }
            args.putBoolean(ARG_CANCELABLE, mCancelable);
            SimpleDialogFragment fragment = new SimpleDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }
    }

    public SimpleDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        boolean cancelable = args.getBoolean(ARG_CANCELABLE, true);
        setCancelable(cancelable);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = args.getString(ARG_TITLE);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        String message = args.getString(ARG_MESSAGE);
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }
}
