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

package com.androidformenhancer.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.androidformenhancer.FieldData;
import com.androidformenhancer.R;
import com.androidformenhancer.ValidationException;
import com.androidformenhancer.ValidationResult;
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.annotation.WidgetValue;
import com.androidformenhancer.validator.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The utility to validate input values.
 *
 * @author Soichiro Kashima
 */
public final class ValidationManager {
    private static final String TAG = "AFE";

    public static final int STOP_POLICY_CONTINUE_ALL = 0;
    public static final int STOP_POLICY_STOP_ALL_IF_ANY = 1;
    public static final int STOP_POLICY_STOP_AND_RESUME_NEXT = 2;

    private int mStopPolicy;
    private List<Validator<?>> mValidators;

    private Class<?> mFormClass;
    private Object mForm;
    private SparseArray<FieldData> mFieldDataArray;

    /**
     * Constructor.
     *
     * @param context   context
     * @param formClass POJO form class which has information of the form
     */
    public ValidationManager(final Context context, final Class<?> formClass) {
        mValidators = new ArrayList<Validator<?>>();
        mStopPolicy = STOP_POLICY_CONTINUE_ALL;
        mFormClass = formClass;
        init(context);
    }

    /**
     * Reads the form information from the form, and creates a new object.
     *
     * @param rootView root view of the form
     */
    public void extractFormFromView(final View rootView) {
        ensureFormFieldsTypes();

        try {
            mForm = mFormClass.newInstance();
            mFieldDataArray = new SparseArray<FieldData>();
            final Field[] fields = mFormClass.getFields();
            for (Field field : fields) {
                Widget widget = field.getAnnotation(Widget.class);
                if (widget == null) {
                    continue;
                }
                View view = rootView.findViewById(widget.id());
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
                    if (widget.values() != null && widget.values().length > 0) {
                        for (WidgetValue checkBoxValue : widget.values()) {
                            CheckBox cb = (CheckBox) group.findViewById(checkBoxValue.id());
                            if (cb != null && cb.isChecked()) {
                                checkedValues.add(checkBoxValue.value());
                            }
                        }
                    } else {
                        List<CheckBox> cbs = findCheckboxes(group);
                        for (CheckBox cb : cbs) {
                            if (cb != null && cb.isChecked() && cb.getTag() != null) {
                                Object tag = cb.getTag();
                                checkedValues.add(tag.toString());
                            }
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

    /**
     * Validates the input values of all the fields in the form.
     * <p/>
     * Validations are executed in the orders specified by the
     * {@link android.androsuit.entity.annotation.Order}. If this annotation is
     * not specified, the order is determined by field names(asc). The fields
     * with the annotations are prior to the others.
     *
     * @return result of the validation
     */
    public ValidationResult validate() {
        return validate(0);
    }

    /**
     * Validates the input values of the field specified by {@code id}.
     *
     * @param id resource ID of the target field
     * @return result of the validation
     */
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public ValidationResult validate(final int id) {
        ValidationResult validationResult = new ValidationResult();

        for (Validator<?> validator : mValidators) {
            validator.setFieldDataArray(mFieldDataArray);
        }

        FieldData[] sorted = id == 0 ? sort() : new FieldData[]{
                mFieldDataArray.get(id),
        };
        validation:
        for (FieldData f : sorted) {
            Widget widget = f.getWidget();
            validationResult.addValidatedId(widget.id());
            for (Validator validator : mValidators) {
                Annotation annotation = f.getAnnotation(validator.getAnnotationClass());
                if (annotation == null) {
                    continue;
                }
                String errorMessage = validator.validate(annotation, getFieldData(widget.id()));
                if (!TextUtils.isEmpty(errorMessage)) {
                    validationResult.addError(widget.id(), errorMessage);
                    if (mStopPolicy == STOP_POLICY_STOP_ALL_IF_ANY) {
                        break validation;
                    }
                    if (mStopPolicy == STOP_POLICY_STOP_AND_RESUME_NEXT) {
                        break;
                    }
                }
            }
        }

        return validationResult;
    }

    /**
     * Gets the field data of the field specified by the {@code id}.
     * <p/>
     * You should execute {@linkplain #extractFormFromView(View)} to extract
     * field data before calling this method.
     *
     * @param id resource ID of the target field
     * @return field data
     */
    public FieldData getFieldData(final int id) {
        return mFieldDataArray.get(id);
    }

    /**
     * Gets the resource IDs of the fields extracted by
     * {@linkplain #extractFormFromView(View)}.
     *
     * @return set of the resource IDs
     */
    public Set<Integer> getExtractedWidgetIds() {
        Set<Integer> ids = new HashSet<Integer>();
        for (int i = 0; i < mFieldDataArray.size(); i++) {
            ids.add(mFieldDataArray.keyAt(i));
        }
        return ids;
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
                                    + type + " found."
                    );
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
                    if (valueString.length() == 0) {
                        continue;
                    }
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
                                        + dstType.getCanonicalName()
                        );
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
     * Clears all the loaded validators. This will mainly be used for tests.
     */
    public void clearValidators() {
        mValidators.clear();
    }

    /**
     * Adds a validator to be used by {@linkplain #validate()}.
     *
     * @param validator validator object to add
     */
    public void addValidator(final Validator<?> validator) {
        mValidators.add(validator);
    }

    private void init(final Context context) {
        TypedArray a = context.getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorDefinitions,
                R.attr.afeValidatorDefinitions, 0);

        mStopPolicy = a.getInt(R.styleable.ValidatorDefinitions_afeStopPolicy,
                ValidationManager.STOP_POLICY_CONTINUE_ALL);

        CharSequence[] standards =
                a.getTextArray(R.styleable.ValidatorDefinitions_afeStandardValidators);

        if (standards == null) {
            // Use default standard validators if not specified with style.
            standards = context.getResources().getTextArray(R.array.afe__validators);
        }

        if (standards != null) {
            for (CharSequence validatorClassName : standards) {
                try {
                    Class<?> validatorClass = Class.forName(validatorClassName.toString());
                    Validator<?> validator = (Validator<?>) validatorClass.newInstance();
                    validator.setContext(context);
                    addValidator(validator);
                } catch (ClassNotFoundException e) {
                    throw new ValidationException(e);
                } catch (InstantiationException e) {
                    throw new ValidationException(e);
                } catch (IllegalAccessException e) {
                    throw new ValidationException(e);
                }
            }
        }

        CharSequence[] customs =
                a.getTextArray(R.styleable.ValidatorDefinitions_afeCustomValidators);

        if (customs != null) {
            for (CharSequence validatorClassName : customs) {
                try {
                    Class<?> validatorClass = Class.forName(validatorClassName.toString());
                    Validator<?> validator = (Validator<?>) validatorClass.newInstance();
                    validator.setContext(context);
                    mValidators.add(validator);
                } catch (ClassNotFoundException e) {
                    throw new ValidationException(e);
                } catch (InstantiationException e) {
                    throw new ValidationException(e);
                } catch (IllegalAccessException e) {
                    throw new ValidationException(e);
                }
            }
        }

        a.recycle();
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
        FieldData data = new FieldData(field, type, value);
        if (data.getWidget() == null) {
            throw new IllegalStateException();
        }
        mFieldDataArray.put(data.getId(), data);
    }

    private FieldData[] sort() {
        if (mFieldDataArray.size() == 0) {
            return null;
        }
        if (mFieldDataArray.size() == 1) {
            return new FieldData[]{
                    mFieldDataArray.valueAt(0),
            };
        }
        SparseArray<List<FieldData>> pool = new SparseArray<List<FieldData>>();
        LinkedList<FieldData> unchecked = new LinkedList<FieldData>();
        List<FieldData> result = new ArrayList<FieldData>();
        for (int i = 0; i < mFieldDataArray.size(); i++) {
            FieldData fieldData = mFieldDataArray.valueAt(i);
            Widget widget = fieldData.getWidget();
            if (pool.indexOfKey(widget.validateAfter()) >= 0) {
                pool.get(widget.validateAfter()).add(fieldData);
            } else {
                List<FieldData> l = new ArrayList<FieldData>();
                l.add(fieldData);
                pool.put(widget.validateAfter(), l);
            }
            if (widget.validateAfter() == 0) {
                unchecked.add(fieldData);
            }
        }
        while (unchecked.size() > 0) {
            FieldData fieldData = unchecked.remove(0);
            result.add(fieldData);
            Widget widget = fieldData.getWidget();
            if (pool.indexOfKey(widget.id()) >= 0) {
                List<FieldData> l = pool.get(widget.id());
                pool.remove(widget.id());
                int idx = 0;
                for (FieldData f : l) {
                    unchecked.add(idx++, f);
                }
            }
        }
        return result.toArray(new FieldData[]{});
    }

    private List<CheckBox> findCheckboxes(ViewGroup group) {
        List<CheckBox> list = new ArrayList<CheckBox>();
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                list.add(cb);
            } else if (child instanceof ViewGroup) {
                list.addAll(findCheckboxes((ViewGroup) child));
            }
        }
        return list;
    }
}
