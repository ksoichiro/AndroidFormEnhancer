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
import com.androidformenhancer.annotation.WidgetValue;
import com.androidformenhancer.internal.FormMetaData;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Helper class to use this library's functions.
 * 
 * @author Soichiro Kashima
 */
public class FormHelper {

    private static final String DIALOG_TAG = FormHelper.class.getCanonicalName() + "_dialog";
    private static final String TAG = "FormHelper";
    private static final String EXCEPTION_MSG_WITHOUT_FRAGMENT_ACTIVITY =
            "You cannot use this method without FragmentActivity "
                    + "because this method use DialogFragment. "
                    + "Check that you set a FragmentActivity instance to the constructor.";

    private Class<?> mFormClass;
    private Object mForm;
    private HashMap<String, FormMetaData> mFormMetaDataMap;
    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private boolean mValidationErrorIconEnabled;
    private Drawable mIconError;
    private Drawable mIconOk;

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     * 
     * @param clazz class of the form
     * @param activity activity which create this object
     */
    public FormHelper(final Class<?> clazz, final Activity activity) {
        mFormClass = clazz;
        mContext = activity;
        if (activity instanceof FragmentActivity) {
            mActivity = (FragmentActivity) activity;
        }
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
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
        mFormClass = clazz;
        mContext = fragment.getActivity().getBaseContext();
        mRootView = fragment.getView().getRootView();
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
     * @param activity activity which has the form
     * @return result of the validation
     */
    public ValidationResult validate() {
        extractFormFromView();
        ValidationManager validationManager = new ValidationManager(mContext);
        ValidationResult validationResult = validationManager
                .validate(mForm, null, mFormMetaDataMap);
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
     * Sets the {@linkplain android.view.View.OnFocusChangedListener} to
     * validate on focus out.<br>
     * If you want to implement your own
     * {@linkplain android.view.View.OnFocusChangedListener}, use
     * {@linkplain #validateText(int)}.
     */
    public void setOnFocusOutValidation() {
        extractFormFromView();
        final ValidationManager validationManager = new ValidationManager(mContext);

        final Field[] fields = mFormClass.getFields();
        for (final Field field : fields) {
            if (!mFormMetaDataMap.containsKey(field.getName())) {
                continue;
            }
            WidgetType widgetType = mFormMetaDataMap.get(field.getName()).getWidgetType();
            if (widgetType != WidgetType.TEXT) {
                continue;
            }
            final Widget widget = field.getAnnotation(Widget.class);
            if (widget == null) {
                continue;
            }
            final EditText e = (EditText) mRootView.findViewById(widget.id());
            e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        extractFormFromView();
                        ValidationResult result =
                                validationManager.validate(mForm, field, mFormMetaDataMap);
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
        extractFormFromView();
        final ValidationManager validationManager = new ValidationManager(mContext);
        final Field[] fields = mFormClass.getFields();
        Field field = null;
        for (final Field f : fields) {
            Widget widget = f.getAnnotation(Widget.class);
            if (widget == null) {
                continue;
            }
            if (widget.id() == textViewId) {
                field = f;
                break;
            }
        }
        if (field == null) {
            throw new IllegalArgumentException("Specified TextView not found!");
        }
        ValidationResult result =
                validationManager.validate(mForm, field, mFormMetaDataMap);
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
        if (mForm == null || mFormClass == null) {
            throw new IllegalStateException("Form is not initialized or validated.");
        }

        try {
            Object form = mFormClass.newInstance();
            for (Field field : mFormClass.getFields()) {
                // We do not copy non-widget field
                if (field.getAnnotation(Widget.class) == null) {
                    continue;
                }

                // Ignore null field
                Object value = field.get(mForm);
                if (value == null) {
                    continue;
                }

                // Allows only String and List<String> fields
                Class<?> type = field.getType();
                if (type.equals(List.class)) {
                    @SuppressWarnings("unchecked")
                    List<String> srcList = (List<String>) value;
                    List<String> dstList = new ArrayList<String>();
                    for (String srcValue : srcList) {
                        dstList.add(srcValue);
                    }
                    field.set(form, dstList);
                } else if (type.equals(String.class)) {
                    field.set(form, field.get(mForm));
                } else {
                    throw new IllegalStateException(
                            "Form class can have only String and List<String> fields but "
                                    + type + " found.");
                }
            }
            return form;
        } catch (InstantiationException e) {
            Log.v(TAG, "Failed to copy form.", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copies an object fields which have same names as the form class.
     * 
     * @param clazz the entity class to create
     * @return created entity object
     */
    public <E> E create(final Class<E> clazz) {
        Field[] srcFields = mForm.getClass().getFields();

        try {
            E dst = clazz.newInstance();
            for (Field srcField : srcFields) {
                Object value = srcField.get(mForm);
                if (value == null) {
                    continue;
                }
                String name = srcField.getName();
                Field dstField = clazz.getField(name);
                Class<?> dstType = dstField.getType();
                if (dstType.equals(List.class)) {
                    @SuppressWarnings("unchecked")
                    List<String> srcList = (List<String>) value;
                    List<String> dstList = new ArrayList<String>();
                    for (String srcValue : srcList) {
                        dstList.add(srcValue);
                    }
                    dstField.set(dst, dstList);
                } else {
                    String valueString = (String) value;
                    if (dstType.equals(String.class)) {
                        dstField.set(dst, valueString);
                    } else if (dstType.equals(int.class)) {
                        dstField.setInt(dst, Integer.parseInt(valueString));
                    } else if (dstType.equals(float.class)) {
                        dstField.setFloat(dst, Float.parseFloat(valueString));
                    } else if (dstType.equals(double.class)) {
                        dstField.setDouble(dst, Double.parseDouble(valueString));
                    } else if (dstType.equals(boolean.class)) {
                        dstField.setBoolean(dst, Boolean.parseBoolean(valueString));
                    } else if (dstType.equals(long.class)) {
                        dstField.setLong(dst, Long.parseLong(valueString));
                    } else if (dstType.equals(short.class)) {
                        dstField.setShort(dst, Short.parseShort(valueString));
                    } else if (dstType.equals(char.class)) {
                        dstField.setChar(dst, valueString.charAt(0));
                    } else {
                        throw new IllegalArgumentException(
                                "Entity field types must be primitive types or String or List<String>: "
                                        + dstType.getCanonicalName());
                    }
                }
            }
            return dst;
        } catch (Exception e) {
            Log.v(TAG, e.getMessage(), e);
            throw new RuntimeException("Failed to instantiate entity.", e);
        }
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
        showDialogFragment(new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                setCancelable(cancelable);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (!TextUtils.isEmpty(title)) {
                    builder.setTitle(title);
                }
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, null);
                return builder.create();
            }
        });
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
     * @param messageResId message resource ID
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
     * @param messageResId message resource ID
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
     * {@linkplain java.text.DateFormat.SHORT} with your device's default
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
        final TextView tv = (TextView) v;
        if (TextUtils.isEmpty(tv.getText())) {
            tv.setText(mActivity.getString(defaultMessageId));
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
        final Dialog dialog = new DatePickerDialog(mActivity, callBack, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)) {
        };
        showDialogFragment(new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return dialog;
            }
        });
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

    /**
     * Reads the form information from the form, and creates a new object.
     * 
     * @param context target context
     * @param rootView root view of the form
     */
    private void extractFormFromView() {
        ensureFormFieldsTypes();

        try {
            mForm = mFormClass.newInstance();
            mFormMetaDataMap = new HashMap<String, FormMetaData>();
            final Field[] fields = mFormClass.getFields();
            for (Field field : fields) {
                Widget widget = field.getAnnotation(Widget.class);
                if (widget == null) {
                    continue;
                }
                View view = mRootView.findViewById(widget.id());
                if (view instanceof EditText) {
                    String value = ((EditText) view).getText().toString();
                    field.set(mForm, value);
                    addFormMetaData(field, WidgetType.TEXT, value);
                    continue;
                }
                if (view instanceof RadioGroup) {
                    RadioGroup radioGroup = (RadioGroup) view;
                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    WidgetValue[] values = widget.values();
                    String value = null;
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].id() == checkedId) {
                            field.set(mForm, values[i].value());
                            value = values[i].value();
                            break;
                        }
                    }
                    addFormMetaData(field, WidgetType.RADIO, value);
                    continue;
                }
                if (view instanceof Spinner) {
                    int index = ((Spinner) view).getSelectedItemPosition();
                    field.set(mForm, Integer.toString(index));
                    addFormMetaData(field, WidgetType.SPINNER, Integer.toString(index));
                    continue;
                }
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    List<String> checkedValues = new ArrayList<String>();
                    for (WidgetValue checkBoxValue : widget.values()) {
                        CheckBox cb = (CheckBox) group.findViewById(checkBoxValue.id());
                        if (cb != null && cb.isChecked()) {
                            checkedValues.add(checkBoxValue.value());
                        }
                    }
                    field.set(mForm, checkedValues);
                    addFormMetaData(field, WidgetType.CHECKBOX, checkedValues);
                    continue;
                }
            }
        } catch (Exception e) {
            mForm = null;
            Log.v(TAG, e.getMessage(), e);
            throw new RuntimeException("Failed to create form instance or retrive data.", e);
        }
    }

    private void ensureFormFieldsTypes() {
        final Field[] fields = mFormClass.getFields();
        for (Field field : fields) {
            // Ensure the field types in form class are all String or
            // List<String>.
            Class<?> type = field.getType();
            if (!type.equals(String.class) && !type.equals(List.class)) {
                throw new IllegalArgumentException(""
                        + "All the form instance fields must be String or List<String>. "
                        + "If you want to use types other than String and List<String>, "
                        + "create an 'entity class' and use FormHelper#createEntityFromForm() "
                        + "after calling this method. Field name: "
                        + field.getName()
                        + " Field type: "
                        + type);
            }
        }
    }

    private void addFormMetaData(final Field field, WidgetType type, Object value) {
        FormMetaData data = new FormMetaData();
        Widget widget = (Widget) field.getAnnotation(Widget.class);
        if (widget != null) {
            data.setId(widget.id());
        }
        data.setWidgetType(type);
        data.setValue(value);
        mFormMetaDataMap.put(field.getName(), data);
    }

    private void setDrawableIntrinsicBounds(final Drawable d) {
        if (d != null) {
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
    }

}
