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

package com.androidformenhancer.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.androidformenhancer.internal.DialogFragment;
import com.androidformenhancer.internal.impl.DatePickerDialogSupportFragment;
import com.androidformenhancer.internal.impl.SimpleDialogSupportFragment;

public class FragmentActivityFormHelper extends FormHelper {

    private FragmentActivity mFragmentActivity;

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     *
     * @param clazz    class of the form
     * @param activity activity which create this object
     */
    public FragmentActivityFormHelper(final Class<?> clazz, final FragmentActivity activity) {
        super(clazz, activity);
        mFragmentActivity = activity;
        setRootView(activity.getWindow().getDecorView().findViewById(android.R.id.content));
        init();
    }

    public void setActivity(final FragmentActivity activity) {
        mFragmentActivity = activity;
    }

    @Override
    public void showAlertDialog(final String title, final String message, final boolean cancelable) {
        if (mFragmentActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        showDialogFragment(new SimpleDialogSupportFragment.Builder()
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .create());
    }

    @Override
    public void showDialogFragment(final DialogFragment dialogFragment) {
        if (mFragmentActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = mFragmentActivity.getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        if (dialogFragment != null) {
            dialogFragment.show(ft, DIALOG_TAG);
        }
    }

    @Override
    public void setAsDateField(final int id, final int defaultMessageId) {
        if (mFragmentActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        mFragmentActivity.findViewById(id).setClickable(true);
        mFragmentActivity.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(id, defaultMessageId);
            }
        });
        ((TextView) mFragmentActivity.findViewById(id)).setText(mFragmentActivity.getString(defaultMessageId));
    }

    @Override
    public void showDatePickerDialog(final int id, final int defaultMessageId) {
        if (mFragmentActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        View v = mFragmentActivity.findViewById(id);
        if (v == null || !(v instanceof TextView)) {
            throw new IllegalArgumentException("Target view must be valid TextView: " + v);
        }
        showDialogFragment(new DatePickerDialogSupportFragment.Builder()
                .setTargetViewResId(id)
                .setDefaultMessageResId(defaultMessageId)
                .create());
    }
}
