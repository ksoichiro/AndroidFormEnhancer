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

import com.androidformenhancer.R;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.WidgetValue;
import com.androidformenhancer.internal.FormMetaData;
import com.androidformenhancer.internal.ValidationManager;
import com.androidformenhancer.utils.StringUtils;

import android.app.Activity;
import android.content.Context;
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

    /**
     * Constructor. You must specify the Form class representing widget details
     * and validation specifications.
     * 
     * @param clazz class of the form
     */
    public FormHelper(final Class<?> clazz) {
        mFormClass = clazz;
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
    public ValidationResult validate(final Activity activity) {
        extractFormFromView(activity);
        ValidationManager validationManager = new ValidationManager(activity);
        ValidationResult validationResult = validationManager
                .validate(mForm, null, mFormMetaDataMap);
        for (int id : validationResult.getValidatedIds()) {
            View v = activity.findViewById(id);
            if (!(v instanceof TextView)) {
                continue;
            }
            TextView tv = (TextView) v;
            if (validationResult.hasErrorFor(id)) {
                tv.setError(StringUtils.serialize(validationResult.getErrorsFor(id)));
                tv.setCompoundDrawables(null, null, getErrorIcon(activity), null);
            } else {
                tv.setError(null);
                tv.setCompoundDrawables(null, null, getOkIcon(activity), null);
            }
        }
        return validationResult;
    }

    /**
     * Validates the input values.
     * <p>
     * Validations are executed in the orders specified by the
     * {@linkplain Widget#validateAfter()}. If this annotation is not specified,
     * the order is undefined.
     * 
     * @param fragment fragment which has the form
     * @return result of the validation
     */
    public ValidationResult validate(final Fragment fragment) {
        return validate(fragment.getActivity());
    }

    /**
     * Sets the {@linkplain android.view.View.OnFocusChangedListener} to
     * validate on focus out.
     * 
     * @param activity activity which has the form
     */
    public void setOnFocusOutValidation(final Activity activity) {
        setOnFocusOutValidation(activity,
                activity.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    /**
     * Sets the {@linkplain android.view.View.OnFocusChangedListener} to
     * validate on focus out.
     * 
     * @param fragment fragment which has the form
     */
    public void setOnFocusOutValidation(final Fragment fragment) {
        setOnFocusOutValidation(fragment.getActivity(),
                fragment.getView().findViewById(android.R.id.content));
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

    private void extractFormFromView(final Activity activity) {
        extractFormFromView(activity,
                activity.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    /**
     * Reads the form information from the form, and creates a new object.
     * 
     * @param context target context
     * @param rootView root view of the form
     */
    private void extractFormFromView(final Context context, final View rootView) {
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
                View view = rootView.findViewById(widget.id());
                if (view instanceof EditText) {
                    addFormMetaData(field, WidgetType.TEXT);
                    String value = ((EditText) view).getText().toString();
                    field.set(mForm, value);
                    continue;
                }
                if (view instanceof RadioGroup) {
                    addFormMetaData(field, WidgetType.RADIO);
                    RadioGroup radioGroup = (RadioGroup) view;
                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    WidgetValue[] values = widget.values();
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].id() == checkedId) {
                            field.set(mForm, values[i].value());
                            break;
                        }
                    }
                    continue;
                }
                if (view instanceof Spinner) {
                    addFormMetaData(field, WidgetType.SPINNER);
                    int index = ((Spinner) view).getSelectedItemPosition();
                    field.set(mForm, Integer.toString(index));
                    continue;
                }
                if (view instanceof ViewGroup) {
                    addFormMetaData(field, WidgetType.CHECKBOX);
                    ViewGroup group = (ViewGroup) view;
                    List<String> checkedValues = new ArrayList<String>();
                    for (WidgetValue checkBoxValue : widget.values()) {
                        CheckBox cb = (CheckBox) group.findViewById(checkBoxValue.id());
                        if (cb != null && cb.isChecked()) {
                            checkedValues.add(checkBoxValue.value());
                        }
                    }
                    field.set(mForm, checkedValues);
                    continue;
                }
            }
        } catch (Exception e) {
            mForm = null;
            Log.v(TAG, e.getMessage(), e);
            throw new RuntimeException("Failed to create form instance or retrive data.", e);
        }
    }

    private void setOnFocusOutValidation(final Context context, final View rootView) {
        extractFormFromView(context, rootView);
        final ValidationManager validationManager = new ValidationManager(context);

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
            final EditText e = (EditText) rootView.findViewById(widget.id());
            e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        extractFormFromView(context, rootView);
                        ValidationResult result =
                                validationManager.validate(mForm, field, mFormMetaDataMap);
                        if (result.hasError()) {
                            e.setError(StringUtils.serialize(result.getAllErrors()),
                                    getErrorIcon(context));
                            e.setCompoundDrawables(null, null, getErrorIcon(context), null);
                        } else {
                            e.setCompoundDrawables(null, null, getOkIcon(context), null);
                        }
                    }
                }
            });
        }

    }

    private Drawable getOkIcon(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }
        final Drawable d = context.getResources().getDrawable(R.drawable.ic_textfield_ok);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
    }

    private Drawable getErrorIcon(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }
        final Drawable d = context.getResources().getDrawable(R.drawable.ic_textfield_error);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
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

    private void addFormMetaData(final Field field, WidgetType type) {
        FormMetaData data = new FormMetaData();
        data.setWidgetType(type);
        mFormMetaDataMap.put(field.getName(), data);
    }
}
