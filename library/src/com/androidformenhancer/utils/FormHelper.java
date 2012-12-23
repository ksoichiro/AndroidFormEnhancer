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

import com.androidformenhancer.form.annotation.Radio;
import com.androidformenhancer.form.annotation.RadioValue;
import com.androidformenhancer.form.annotation.Spinner;
import com.androidformenhancer.form.annotation.Text;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.lang.reflect.Field;

/**
 * Utility about the form operations.
 * 
 * @author Soichiro Kashima
 */
public class FormHelper<T> {

    private static final String TAG = "FormHelper";

    private T mForm;

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
            final Field[] fields = clazz.getFields();
            for (Field field : fields) {
                // Text box type
                Text text = (Text) field.getAnnotation(Text.class);
                if (text != null) {
                    String value =
                            ((EditText) rootView.findViewById(text.id())).getText().toString();
                    field.set(mForm, value);
                    continue;
                }

                // Radio button type
                Radio radio = (Radio) field.getAnnotation(Radio.class);
                if (radio != null) {
                    int groupId = radio.groupId();
                    RadioGroup radioGroup = (RadioGroup) rootView.findViewById(groupId);
                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    RadioValue[] values = radio.values();
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].id() == checkedId) {
                            field.set(mForm, values[i].value());
                            break;
                        }
                    }
                    continue;
                }

                // Spinner type
                Spinner spinner = (Spinner) field.getAnnotation(Spinner.class);
                if (spinner != null) {
                    int index = ((android.widget.Spinner) rootView.findViewById(spinner.id()))
                            .getSelectedItemPosition();
                    field.set(mForm, Integer.toString(index));
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
                            "Entity field types must be primitive types or String: "
                                    + dstType.getCanonicalName());
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
            // Ensure the field types in form class are all String.
            Class<?> type = field.getType();
            if (!type.equals(String.class)) {
                throw new IllegalArgumentException(""
                        + "All the form instance fields must be String. "
                        + "If you want to use types other than String, "
                        + "create an 'entity class' and use FormHelper#createEntityFromForm() "
                        + "after calling this method. Field name: "
                        + field.getName());
            }
        }
    }
}
