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

package com.androidformenhancer;

import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.internal.ValidationManager;
import com.androidformenhancer.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper class to use this library's functions.
 * 
 * @author Soichiro Kashima
 */
public class FormHelper {

    private static final String DIALOG_TAG = FormHelper.class.getCanonicalName() + "_dialog";
    private static final String EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY =
            "You cannot use this method without FragmentActivity "
                    + "because this method use DialogFragment. "
                    + "Check that you set a FragmentActivity instance to the constructor.";

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private boolean mValidationErrorIconEnabled;
    private Drawable mIconError;
    private Drawable mIconOk;
    private ValidationManager mValidationManager;

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     * 
     * @param clazz class of the form
     * @param activity activity which create this object
     */
    public FormHelper(final Class<?> clazz, final Activity activity) {
        mContext = activity;
        if (activity instanceof FragmentActivity) {
            mActivity = (FragmentActivity) activity;
        }
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mValidationManager = new ValidationManager(mContext, clazz);
        init();
    }

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     * 
     * @param clazz class of the form
     * @param fragment fragment which create this object
     */
    public FormHelper(final Class<?> clazz, final Fragment fragment) {
        mContext = fragment.getActivity().getBaseContext();
        mRootView = fragment.getView().getRootView();
        mValidationManager = new ValidationManager(mContext, clazz);
        init();
    }

    /**
     * Enables or disables the error icon feature. If enabled and the EditText
     * has errors, the icon will be shown on the right of it. By default, it is
     * enabled.
     * 
     * @param enabled true if enable error icon
     */
    public void setValidationErrorIconEnabled(final boolean enabled) {
        mValidationErrorIconEnabled = enabled;
    }

    /**
     * Sets the icon of the error for the EditText.
     * 
     * @param d Drawable object for icon
     */
    public void setIconError(final Drawable d) {
        mIconError = d;
        setDrawableIntrinsicBounds(mIconError);
    }

    /**
     * Sets the icon of the error for the EditText.
     * 
     * @param resId resource ID for icon drawable
     */
    public void setIconError(final int resId) {
        mIconError = mContext.getResources().getDrawable(resId);
        setDrawableIntrinsicBounds(mIconError);
    }

    /**
     * Sets the icon of the success for the EditText.
     * 
     * @param d Drawable object for icon
     */
    public void setIconOk(final Drawable d) {
        mIconOk = d;
        setDrawableIntrinsicBounds(mIconOk);
    }

    /**
     * Sets the icon of the success for the EditText.
     * 
     * @param resId resource ID for icon drawable
     */
    public void setIconOk(final int resId) {
        mIconOk = mContext.getResources().getDrawable(resId);
        setDrawableIntrinsicBounds(mIconOk);
    }

    /**
     * Validates the input values.
     * <p>
     * Validations are executed in the orders specified by the
     * {@linkplain Widget#validateAfter()}. If this annotation is not specified,
     * the order is undefined.
     * 
     * @return result of the validation
     */
    public ValidationResult validate() {
        mValidationManager.extractFormFromView(mRootView);
        ValidationResult validationResult = mValidationManager.validate();
        for (int id : validationResult.getValidatedIds()) {
            View v = mRootView.findViewById(id);
            if (!(v instanceof TextView)) {
                continue;
            }
            setErrorToTextView(validationResult, (TextView) v);
        }
        return validationResult;
    }

    /**
     * Sets the {@linkplain android.view.View.OnFocusChangeListener} to
     * validate on focus out.<br>
     * If you want to implement your own
     * {@linkplain android.view.View.OnFocusChangeListener}, use
     * {@linkplain #validateText(int)}.
     */
    public void setOnFocusOutValidation() {
        mValidationManager.extractFormFromView(mRootView);
        for (final int id : mValidationManager.getExtractedWidgetIds()) {
            WidgetType widgetType = mValidationManager.getFieldData(id).getWidgetType();
            if (widgetType != WidgetType.TEXT) {
                continue;
            }
            final EditText e = (EditText) mRootView.findViewById(id);
            e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        mValidationManager.extractFormFromView(mRootView);
                        ValidationResult result = mValidationManager.validate(id);
                        setErrorToTextView(result, e);
                    }
                }
            });
        }
    }

    /**
     * Validates TextView or EditText.<br>
     * For example, you can call this inside
     * {@linkplain android.view.View.OnFocusChangeListener}.
     * 
     * @param textViewId target TextView or EditText's resource ID
     */
    public void validateText(final int textViewId) {
        mValidationManager.extractFormFromView(mRootView);
        ValidationResult result = mValidationManager.validate(textViewId);
        TextView v = (TextView) mRootView.findViewById(textViewId);
        setErrorToTextView(result, v);
    }

    /**
     * Returns the extracted form.<br>
     * The form returned is deep copy, so values of this object's fields cannot
     * be changed from inside the FormHelper.
     * 
     * @return deep copy of the extracted form
     */
    public Object getForm() {
        return mValidationManager.getForm();
    }

    /**
     * Copies an object fields which have same names as the form class.
     * 
     * @param clazz the entity class to create
     * @return created entity object
     */
    public <E> E create(final Class<E> clazz) {
        return mValidationManager.create(clazz);
    }

    /**
     * Sets the error icon and popup window to the specified TextView or
     * EditText.<br>
     * Before calling this, you must call {@linkplain #validate()} or
     * {@linkplain #validateText(int)} to get validation result.
     * 
     * @param result validation result
     * @param textView TextView or EditText object which you want to set error
     */
    public void setErrorToTextView(final ValidationResult result, final TextView textView) {
        if (mValidationErrorIconEnabled) {
            int id = textView.getId();
            if (result.hasErrorFor(id)) {
                textView.setError(StringUtils.serialize(result.getErrorsFor(id)),
                        mIconError);
                textView.setCompoundDrawables(null, null, mIconError, null);
            } else {
                textView.setCompoundDrawables(null, null, mIconOk, null);
            }
        }
    }

    /**
     * Shows the DialogFragment with FragmentActivity.<br>
     * You must set {@linkplain android.support.v4.app.FragmentActivity}
     * instance when you create this instance. This method use only one fragment
     * tag, so if the old fragment has been shown already, it will be dismissed
     * and new one will be shown.
     * 
     * @param dialogFragment dialog to be shown
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showDialogFragment(final DialogFragment dialogFragment) {
        if (mActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = mActivity.getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        if (dialogFragment != null) {
            dialogFragment.show(ft, DIALOG_TAG);
        }
    }

    /**
     * Shows the simple dialog with title and message using DialogFragment. You
     * must set {@linkplain android.support.v4.app.FragmentActivity} instance
     * when you create this instance. This method use only one fragment tag, so
     * if the old fragment has been shown already, it will be dismissed and new
     * one will be shown.
     * 
     * @param title title string
     * @param message message string
     * @param cancelable true if the dialog is cancelable
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final String title, final String message, final boolean cancelable) {
        if (mActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        showDialogFragment(new SimpleDialogFragment.Builder()
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .create());
    }

    /**
     * Convenience for showAlertDialog(null, Context#getString(messageResId),
     * false)}.
     * 
     * @see #showAlertDialog(int, int, boolean)
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final int messageResId) {
        showAlertDialog(null, mContext.getString(messageResId), false);
    }

    /**
     * Convenience for showAlertDialog(null, Context#getString(messageResId),
     * cancelable)}.
     * 
     * @see #showAlertDialog(int, int, boolean)
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final int messageResId, final boolean cancelable) {
        showAlertDialog(null, mContext.getString(messageResId), cancelable);
    }

    /**
     * Convenience for showAlertDialog(Context#getString(titleResId),
     * Context#getString(messageResId), false)}.
     * 
     * @see #showAlertDialog(int, int, boolean)
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final int titleResId, final int messageResId) {
        showAlertDialog(mContext.getString(titleResId), mContext.getString(messageResId), false);
    }

    /**
     * Convenience for showAlertDialog(Context#getString(titleResId),
     * Context#getString(messageResId), cancelable)}.
     * 
     * @see #showAlertDialog(int, int, boolean)
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final int titleResId, final int messageResId,
            final boolean cancelable) {
        showAlertDialog(mContext.getString(titleResId), mContext.getString(messageResId),
                cancelable);
    }

    /**
     * Convenience for showAlertDialog(null, message, false)}.
     * 
     * @see #showAlertDialog(int, int, boolean)
     * @param message message string
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final String message) {
        showAlertDialog(null, message, false);
    }

    /**
     * Convenience for showAlertDialog(title, message, false)}.
     * 
     * @see #showAlertDialog(int, int, boolean)
     * @param title title string
     * @param message message string
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showAlertDialog(final String title, final String message) {
        showAlertDialog(title, message, false);
    }

    /**
     * Sets the TextView (or its subclass such as Button) as date field. If you
     * click the widget, the DatePickerDialog will be shown.
     * 
     * @see #showDatePickerDialog(int, int)
     * @param id target widget resource ID
     * @param defaultMessageId message resource ID when there is no selection
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void setAsDateField(final int id, final int defaultMessageId) {
        if (mActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        mActivity.findViewById(id).setClickable(true);
        mActivity.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(id, defaultMessageId);
            }
        });
        ((TextView) mActivity.findViewById(id)).setText(mActivity.getString(defaultMessageId));
    }

    /**
     * Shows a DatePickerDialog. If the date set by dialog, the value will be
     * set to the TextView specified by the id. The date format is
     * {@linkplain java.text.DateFormat#SHORT} with your device's default
     * Locale.<br>
     * Normally, you should use {@linkplain #setAsDateField(int, int)} instead
     * of calling this directly.
     * 
     * @param id target widget resource ID
     * @param defaultMessageId message resource ID when there is no selection
     * @throws IllegalStateException if FragmentActivity instance has not been
     *             passed to this instance
     */
    public void showDatePickerDialog(final int id, final int defaultMessageId) {
        if (mActivity == null) {
            throw new IllegalStateException(EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY);
        }
        View v = mActivity.findViewById(id);
        if (v == null || !(v instanceof TextView)) {
            throw new IllegalArgumentException("Target view must be valid TextView: " + v);
        }
        showDialogFragment(new DatePickerDialogFragment.Builder()
                .setTargetViewResId(id)
                .setDefaultMessageResId(defaultMessageId)
                .create());
    }

    private void init() {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorDefinitions,
                R.attr.afeValidatorDefinitions, 0);

        mValidationErrorIconEnabled = a.getBoolean(
                R.styleable.ValidatorDefinitions_afeValidationErrorIconEnabled, true);

        mIconError = getIconFromStyle(a,
                R.styleable.ValidatorDefinitions_afeValidationIconError,
                R.drawable.ic_textfield_error);

        mIconOk = getIconFromStyle(a,
                R.styleable.ValidatorDefinitions_afeValidationIconOk,
                R.drawable.ic_textfield_ok);

        a.recycle();
    }

    private Drawable getIconFromStyle(TypedArray a, int index, int defaultResId) {
        int id = a.getResourceId(index, 0);
        Drawable d;
        if (id > 0) {
            d = mContext.getResources().getDrawable(id);
        } else {
            d = mContext.getResources().getDrawable(defaultResId);
        }
        setDrawableIntrinsicBounds(d);
        return d;
    }

    private void setDrawableIntrinsicBounds(final Drawable d) {
        if (d != null) {
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
    }

    public static class SimpleDialogFragment extends DialogFragment {
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

    public static class DatePickerDialogFragment extends DialogFragment {
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
}
