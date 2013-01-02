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

package com.androidformenhancer.utils;

import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.WidgetValue;
import com.androidformenhancer.validator.ValidationManager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Utility about the form operations.
 * 
 * @author Soichiro Kashima
 */
public class FormHelper<T> {

    private static final String TAG = "FormHelper";

    private T mForm;
    private HashMap<String, FormMetaData> mFormMetaDataMap;

    /**
     * Validates the input values.
     * <p>
     * Validations are executed in the orders specified by the
     * {@link android.androsuit.entity.annotation.Order}. If this annotation is
     * not specified, the order is determined by field names(asc). The fields
     * with the annotations are prior to the others.
     * 
     * @param context context to access the message resources
     * @param target target object to be validated (the form)
     * @return list to save the error messages
     */
    public ArrayList<String> validate(final Activity activity, Class<T> clazz) {
        T form = extractFormFromView(activity, clazz);
        ValidationManager validationManager = new ValidationManager(activity);
        return validationManager.validate(form, mFormMetaDataMap);
    }

    public ArrayList<String> validate(final Fragment fragment, Class<T> clazz) {
        T form = extractFormFromView(fragment, clazz);
        ValidationManager validationManager = new ValidationManager(fragment.getActivity());
        return validationManager.validate(form, mFormMetaDataMap);
    }

    public T extractFormFromView(final Activity activity, Class<T> clazz) {
        return extractFormFromView(activity,
                activity.getWindow().getDecorView().findViewById(android.R.id.content),
                clazz);
    }

    public T extractFormFromView(final Fragment fragment, Class<T> clazz) {
        return extractFormFromView(fragment.getActivity(),
                fragment.getView().findViewById(android.R.id.content),
                clazz);
    }

    /**
     * Reads the form information from the activity, and creates a new object.
     * 
     * @param context target context
     * @param rootView root view of the form
     * @param dstFormClass the destination form class
     * @return created object
     */
    public T extractFormFromView(final Context context, final View rootView, Class<T> clazz) {
        ensureFormFieldsTypes(clazz);

        try {
            mForm = clazz.newInstance();
            mFormMetaDataMap = new HashMap<String, FormMetaData>();
            final Field[] fields = clazz.getFields();
            for (Field field : fields) {
                Widget widget = (Widget) field.getAnnotation(Widget.class);
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
            return mForm;
        } catch (Exception e) {
            mForm = null;
            Log.v(TAG, e.getMessage(), e);
            throw new RuntimeException("Failed to create form instance or retrive data.", e);
        }
    }

    /**
     * Copies an object fields which have same names to the other object.<br>
     * 
     * @param src source object
     * @param dstClass destination object's class
     * @return copied object
     */
    public <E> E createEntityFromForm(final Class<E> clazz) {
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

    private void ensureFormFieldsTypes(Class<T> clazz) {
        final Field[] fields = clazz.getFields();
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
