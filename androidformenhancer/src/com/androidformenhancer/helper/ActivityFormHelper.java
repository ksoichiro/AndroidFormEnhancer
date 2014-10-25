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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.androidformenhancer.internal.DialogFragment;
import com.androidformenhancer.internal.impl.DatePickerDialogFragment;
import com.androidformenhancer.internal.impl.SimpleDialogFragment;

public class ActivityFormHelper extends FormHelper {

    private Activity mNormalActivity;

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     *
     * @param clazz    class of the form
     * @param activity activity which create this object
     */
    public ActivityFormHelper(final Class<?> clazz, final Activity activity) {
        super(clazz, activity);
        mNormalActivity = activity;
        setRootView(activity.getWindow().getDecorView().findViewById(android.R.id.content));
        init();
    }

    public void setActivity(Activity activity) {
        mNormalActivity = activity;
    }

    @Override
    public void showAlertDialog(final String title, final String message, final boolean cancelable) {
        if (mNormalActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            showDialogFragment(new SimpleDialogFragment.Builder()
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(cancelable)
                    .create());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void showDialogFragment(final DialogFragment dialogFragment) {
        if (mNormalActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        android.app.FragmentTransaction ft = mNormalActivity.getFragmentManager().beginTransaction();
        android.app.Fragment prev = mNormalActivity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        if (dialogFragment != null) {
            dialogFragment.show(ft, DIALOG_TAG);
        }
    }

    @Override
    public void setAsDateField(final int id, final int defaultMessageId) {
        if (mNormalActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            mNormalActivity.findViewById(id).setClickable(true);
            mNormalActivity.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(id, defaultMessageId);
                }
            });
            ((TextView) mNormalActivity.findViewById(id)).setText(mNormalActivity.getString(defaultMessageId));
        }
    }

    @Override
    public void showDatePickerDialog(final int id, final int defaultMessageId) {
        if (mNormalActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        View v = mNormalActivity.findViewById(id);
        if (v == null || !(v instanceof TextView)) {
            throw new IllegalArgumentException("Target view must be valid TextView: " + v);
        }
        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            showDialogFragment(new DatePickerDialogFragment.Builder()
                    .setTargetViewResId(id)
                    .setDefaultMessageResId(defaultMessageId)
                    .create());
        }
    }

}
