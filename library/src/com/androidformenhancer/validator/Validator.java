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

package com.androidformenhancer.validator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.SparseArray;

import com.androidformenhancer.FieldData;
import com.androidformenhancer.R;
import com.androidformenhancer.annotation.Widget;

import java.lang.annotation.Annotation;

/**
 * Provides the validation functions.
 * <p/>
 * This class prepares the objects to validation for subclasses.
 *
 * @param <T> annotation type which is necessary to the validation
 * @author Soichiro Kashima
 */
public abstract class Validator<T extends Annotation> {

    /**
     * Context to access to the resources.
     */
    private Context mContext;

    private SparseArray<FieldData> mFieldDataArray;

    /**
     * Constructor.
     */
    public Validator() {
        mFieldDataArray = new SparseArray<FieldData>();
    }

    /**
     * Validates the object, and returns a message if there is an error.
     *
     * @param fieldData field value and meta data
     * @return error message
     */
    @SuppressWarnings("unchecked")
    public String validate(final FieldData fieldData) {
        return validate((T) fieldData.getAnnotation(getAnnotationClass()), fieldData);
    }

    /**
     * Validates the object, and returns a message if there is an error.
     *
     * @param annotation annotation which the field has
     * @param fieldData  field value and meta data
     * @return error message
     */
    public abstract String validate(final T annotation, final FieldData fieldData);

    /**
     * Gives the concrete annotation class to the {@linkplain Validator} because
     * the {@linkplain Validator}, the abstract parameterized class cannot
     * determine its parameter's concrete class.<br>
     * When you implement this method, just return the class as follows:
     * <p/>
     * <pre>
     * return SomeAnnotation.class;
     * </pre>
     *
     * @return annotation class
     */
    public abstract Class<T> getAnnotationClass();

    /**
     * Gets the context set by the framework.
     *
     * @return context
     */
    protected Context getContext() {
        return mContext;
    }

    /**
     * Sets the context for validators to access to the resources.
     * <p/>
     * This is designed to use in the framework internally.
     *
     * @param context context
     */
    public void setContext(final Context context) {
        mContext = context;
    }

    /**
     * Sets the array of the field meta data.
     * <p/>
     * This is designed to use in the framework internally.
     *
     * @param fieldDataArray array of the field data
     */
    public void setFieldDataArray(final SparseArray<FieldData> fieldDataArray) {
        mFieldDataArray = fieldDataArray;
    }

    /**
     * Gets the user-readable name of the field for showing error message.<br>
     * This method use resource {@code overrideId}, otherwise
     * {@linkplain Widget#nameResId()} if there is defined, or the name of the
     * field.
     *
     * @param fieldData  field data given by the target field
     * @param overrideId resource ID of the name if you want to override
     * @return
     */
    protected String getName(final FieldData fieldData, final int overrideId) {
        String name = fieldData.getName();
        int nameResId = getNameResourceId(fieldData);
        if (nameResId > 0) {
            name = getContext().getResources().getString(nameResId);
        }
        if (overrideId > 0) {
            name = getContext().getResources().getString(overrideId);
        }
        return name;
    }

    /**
     * Gets an error message from resource.
     *
     * @param index         index of the attributes
     * @param defaultId     message resource ID of the default message
     * @param messageParams array of the parameters needed by message resource
     * @return error message
     */
    protected String getMessage(final int index, final int defaultId, final Object... messageParams) {
        int messageResId = 0;
        if (index > 0) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(null,
                    R.styleable.ValidatorMessages,
                    R.attr.afeValidatorMessages, 0);
            messageResId = a.getResourceId(index, 0);
            a.recycle();
        }
        if (messageResId == 0) {
            messageResId = defaultId;
        }
        return getContext().getResources().getString(messageResId, messageParams);
    }

    /**
     * Gets the value of the other field in the form.
     *
     * @param id resource ID of the target field
     * @return value of the target field
     */
    protected String getValueById(int id) {
        return mFieldDataArray.get(id).getValueAsString();
    }

    private int getNameResourceId(final FieldData formMetaData) {
        Widget widget = formMetaData.getWidget();
        return widget == null ? 0 : widget.nameResId();
    }

}
