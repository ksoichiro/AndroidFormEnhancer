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

import com.androidformenhancer.R;
import com.androidformenhancer.WidgetType;
import com.androidformenhancer.annotation.Widget;
import com.androidformenhancer.internal.FormMetaData;

import android.content.Context;
import android.content.res.TypedArray;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Provides the validation functions.
 * <p>
 * This class prepares the objects to validation for subclasses.
 * 
 * @author Soichiro Kashima
 */
public abstract class Validator {

    /** Context to access to the resources. */
    private Context mContext;

    /** Target object to validate. */
    private Object mTarget;

    private Map<String, FormMetaData> mFormMetaDataMap;

    /**
     * Validates the object, and returns whether it has any errors or not.
     * 
     * @param value input value
     * @param field target field
     * @return true if there are errors.
     */
    public abstract String validate(final Field field);

    protected Context getContext() {
        return mContext;
    }

    public void setContext(final Context context) {
        mContext = context;
    }

    protected Object getTarget() {
        return mTarget;
    }

    /**
     * Sets the target object.
     * 
     * @param target target object to set
     */
    public void setTarget(final Object target) {
        mTarget = target;
    }

    public void setFormMetaDataMap(final Map<String, FormMetaData> formMetaDataMap) {
        mFormMetaDataMap = formMetaDataMap;
    }

    protected WidgetType getWidgetType(final Field field) {
        if (mFormMetaDataMap == null || field == null) {
            return null;
        }
        return mFormMetaDataMap.get(field.getName()).getWidgetType();
    }

    /**
     * Gets the value of the field from the target object as String type.
     * 
     * @param field target field
     * @return value as String type
     */
    protected String getValueAsString(final Field field) {
        String value;
        try {
            value = (String) field.get(getTarget());
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the value of the field from the target object as List<String> type.
     * 
     * @param field target field
     * @return value as List<String> type
     */
    @SuppressWarnings("unchecked")
    protected List<String> getValueAsStringList(final Field field) {
        List<String> value;
        try {
            value = (List<String>) field.get(getTarget());
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    protected String getName(final Field field, final int overrideId) {
        String name = field.getName();
        int nameResId = getNameResourceId(field);
        if (nameResId > 0) {
            name = getContext().getResources().getString(nameResId);
        }
        if (overrideId > 0) {
            name = getContext().getResources().getString(overrideId);
        }
        return name;
    }

    protected int getNameResourceId(final Field field) {
        Widget widget = (Widget) field.getAnnotation(Widget.class);
        return widget == null ? 0 : widget.nameResId();
    }

    protected String getMessage(final int index, final int defaultId, final Object... messageParams) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorMessages,
                R.attr.afeValidatorMessages, 0);
        int messageResId = a.getResourceId(index, 0);
        a.recycle();
        if (messageResId == 0) {
            messageResId = defaultId;
        }
        return getContext().getResources().getString(messageResId, messageParams);
    }

    protected Object getValueById(int id) {
        for (FormMetaData data : mFormMetaDataMap.values()) {
            if (data.getId() == id) {
                return data.getValue();
            }
        }
        return null;
    }
}
