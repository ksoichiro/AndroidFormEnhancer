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
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.androidformenhancer.internal.DialogFragment;
import com.androidformenhancer.internal.impl.DatePickerDialogSupportFragment;
import com.androidformenhancer.internal.impl.SimpleDialogSupportFragment;

public class SupportFragmentFormHelper extends FormHelper {

    private Fragment mFragment;

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     *
     * @param clazz    class of the form
     * @param fragment fragment which create this object
     */
    public SupportFragmentFormHelper(final Class<?> clazz, final Fragment fragment) {
        super(clazz, fragment.getActivity().getBaseContext());
        mFragment = fragment;
        setRootView(fragment.getView().getRootView());
        init();
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void showAlertDialog(final String title, final String message, final boolean cancelable) {
        if (mFragment == null) {
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
        if (mFragment == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        FragmentTransaction ft = mFragment.getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = mFragment.getActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        if (dialogFragment != null) {
            dialogFragment.show(ft, DIALOG_TAG);
        }
    }

    @Override
    public void setAsDateField(final int id, final int defaultMessageId) {
        if (mFragment == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        mFragment.getActivity().findViewById(id).setClickable(true);
        mFragment.getActivity().findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(id, defaultMessageId);
            }
        });
        ((TextView) mFragment.getActivity().findViewById(id)).setText(mFragment.getActivity().getString(defaultMessageId));
    }

    @Override
    public void showDatePickerDialog(final int id, final int defaultMessageId) {
        if (mFragment == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        View v = mFragment.getActivity().findViewById(id);
        if (v == null || !(v instanceof TextView)) {
            throw new IllegalArgumentException("Target view must be valid TextView: " + v);
        }
        showDialogFragment(new DatePickerDialogSupportFragment.Builder()
                .setTargetViewResId(id)
                .setDefaultMessageResId(defaultMessageId)
                .create());
    }

}
