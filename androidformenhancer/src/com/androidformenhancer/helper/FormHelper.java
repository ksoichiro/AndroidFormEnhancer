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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidformenhancer.R;
import com.androidformenhancer.ValidationResult;
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.internal.DialogFragment;
import com.androidformenhancer.internal.ValidationManager;
import com.androidformenhancer.utils.StringUtils;

/**
 * Helper class to use this library's functions.
 *
 * @author Soichiro Kashima
 */
public abstract class FormHelper {

    protected static final String DIALOG_TAG = FormHelper.class.getCanonicalName() + "_dialog";
    protected static final String EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY =
            "You cannot use this method without FragmentActivity "
                    + "because this method use DialogFragment. "
                    + "Check that you set a FragmentActivity instance to the constructor.";

    private Context mContext;
    private Class<?> mFormClass;
    private View mRootView;
    private boolean mValidationErrorIconEnabled;
    private Drawable mIconError;
    private Drawable mIconOk;
    private ValidationManager mValidationManager;

    public FormHelper(final Class<?> clazz, final Context context) {
        mContext = context;
        mFormClass = clazz;
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
     * <p/>
     * Validations are executed in the orders specified by the
     * {@linkplain Widget#validateAfter()}. If this annotation is not specified,
     * the order is undefined.
     *
     * @return result of the validation
     */
    public ValidationResult validate() {
        getValidationManager().extractFormFromView(mRootView);
        ValidationResult validationResult = getValidationManager().validate();
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
        getValidationManager().extractFormFromView(mRootView);
        for (final int id : getValidationManager().getExtractedWidgetIds()) {
            WidgetType widgetType = getValidationManager().getFieldData(id).getWidgetType();
            if (widgetType != WidgetType.TEXT) {
                continue;
            }
            final EditText e = (EditText) mRootView.findViewById(id);
            e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        getValidationManager().extractFormFromView(mRootView);
                        ValidationResult result = getValidationManager().validate(id);
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
        getValidationManager().extractFormFromView(mRootView);
        ValidationResult result = getValidationManager().validate(textViewId);
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
        return getValidationManager().getForm();
    }

    /**
     * Copies an object fields which have same names as the form class.
     *
     * @param clazz the entity class to create
     * @return created entity object
     */
    public <E> E create(final Class<E> clazz) {
        return getValidationManager().create(clazz);
    }

    /**
     * Sets the error icon and popup window to the specified TextView or
     * EditText.<br>
     * Before calling this, you must call {@linkplain #validate()} or
     * {@linkplain #validateText(int)} to get validation result.
     *
     * @param result   validation result
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
     *                               passed to this instance
     */
    public abstract void showDialogFragment(final DialogFragment dialogFragment);

    /**
     * Shows the simple dialog with title and message using DialogFragment. You
     * must set {@linkplain android.support.v4.app.FragmentActivity} instance
     * when you create this instance. This method use only one fragment tag, so
     * if the old fragment has been shown already, it will be dismissed and new
     * one will be shown.
     *
     * @param title      title string
     * @param message    message string
     * @param cancelable true if the dialog is cancelable
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     */
    public abstract void showAlertDialog(final String title, final String message, final boolean cancelable);

    /**
     * Convenience for showAlertDialog(null, Context#getString(messageResId),
     * false)}.
     *
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showAlertDialog(int, int, boolean)
     */
    public void showAlertDialog(final int messageResId) {
        showAlertDialog(null, mContext.getString(messageResId), false);
    }

    /**
     * Convenience for showAlertDialog(null, Context#getString(messageResId),
     * cancelable)}.
     *
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showAlertDialog(int, int, boolean)
     */
    public void showAlertDialog(final int messageResId, final boolean cancelable) {
        showAlertDialog(null, mContext.getString(messageResId), cancelable);
    }

    /**
     * Convenience for showAlertDialog(Context#getString(titleResId),
     * Context#getString(messageResId), false)}.
     *
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showAlertDialog(int, int, boolean)
     */
    public void showAlertDialog(final int titleResId, final int messageResId) {
        showAlertDialog(mContext.getString(titleResId), mContext.getString(messageResId), false);
    }

    /**
     * Convenience for showAlertDialog(Context#getString(titleResId),
     * Context#getString(messageResId), cancelable)}.
     *
     * @param messageResId message resource ID
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showAlertDialog(int, int, boolean)
     */
    public void showAlertDialog(final int titleResId, final int messageResId,
                                final boolean cancelable) {
        showAlertDialog(mContext.getString(titleResId), mContext.getString(messageResId),
                cancelable);
    }

    /**
     * Convenience for showAlertDialog(null, message, false)}.
     *
     * @param message message string
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showAlertDialog(int, int, boolean)
     */
    public void showAlertDialog(final String message) {
        showAlertDialog(null, message, false);
    }

    /**
     * Convenience for showAlertDialog(title, message, false)}.
     *
     * @param title   title string
     * @param message message string
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showAlertDialog(int, int, boolean)
     */
    public void showAlertDialog(final String title, final String message) {
        showAlertDialog(title, message, false);
    }

    /**
     * Sets the TextView (or its subclass such as Button) as date field. If you
     * click the widget, the DatePickerDialog will be shown.
     *
     * @param id               target widget resource ID
     * @param defaultMessageId message resource ID when there is no selection
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     * @see #showDatePickerDialog(int, int)
     */
    public abstract void setAsDateField(final int id, final int defaultMessageId);

    /**
     * Shows a DatePickerDialog. If the date set by dialog, the value will be
     * set to the TextView specified by the id. The date format is
     * {@linkplain java.text.DateFormat#SHORT} with your device's default
     * Locale.<br>
     * Normally, you should use {@linkplain #setAsDateField(int, int)} instead
     * of calling this directly.
     *
     * @param id               target widget resource ID
     * @param defaultMessageId message resource ID when there is no selection
     * @throws IllegalStateException if FragmentActivity instance has not been
     *                               passed to this instance
     */
    public abstract void showDatePickerDialog(final int id, final int defaultMessageId);

    protected void setRootView(final View view) {
        mRootView = view;
    }

    protected void init() {
        if (mContext == null || mContext.getTheme() == null) {
            return;
        }
        TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorDefinitions,
                R.attr.afeValidatorDefinitions, 0);
        if (a == null) {
            return;
        }

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

    private ValidationManager getValidationManager() {
        // Lazy initialization
        if (mValidationManager == null) {
            if (mContext == null) {
                throw new IllegalStateException("Cannot create ValidationManager. Context is required");
            }
            if (mFormClass == null) {
                throw new IllegalStateException("Cannot create ValidationManager. Form class is required");
            }
            mValidationManager = new ValidationManager(mContext, mFormClass);
        }
        return mValidationManager;
    }
}
