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
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class to use this library's functions.
 * 
 * @author Soichiro Kashima
 */
public class FormHelper {

    private static final String TAG = "FormHelper";

    private Class<?> mFormClass;
    private Object mForm;
    private HashMap<String, FormMetaData> mFormMetaDataMap;
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

    public void setValidationErrorIconEnabled(final boolean enabled) {
        mValidationErrorIconEnabled = enabled;
    }

    public void setIconError(final Drawable d) {
        mIconError = d;
        setDrawableIntrinsicBounds(mIconError);
    }

    public void setIconError(final int resId) {
        mIconError = mContext.getResources().getDrawable(resId);
        setDrawableIntrinsicBounds(mIconError);
    }

    public void setIconOk(final Drawable d) {
        mIconOk = d;
        setDrawableIntrinsicBounds(mIconOk);
    }

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
     * Copies an object fields which have same names as the form class.<br>
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
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].id() == checkedId) {
                            field.set(mForm, values[i].value());
                            addFormMetaData(field, WidgetType.RADIO, values[i].value());
                            break;
                        }
                    }
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
