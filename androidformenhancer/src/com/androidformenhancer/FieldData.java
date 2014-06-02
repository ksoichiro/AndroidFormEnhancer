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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * Convenient container class to retrieve field information.
 *
 * @author Soichiro Kashima
 */
public class FieldData {

    private int mId;
    private String mName;
    private Widget mWidget;
    private WidgetType mWidgetType;
    private boolean mArray;
    private Object mValue;
    private HashMap<Class<? extends Annotation>, Annotation> mAnnotations;

    /**
     * Constructor.
     *
     * @param field      field information
     * @param widgetType type of the widget
     */
    public FieldData(final Field field, final WidgetType widgetType) {
        init(field, widgetType);
    }

    /**
     * Constructor.
     *
     * @param field      field information
     * @param widgetType type of the widget
     * @param value      value of the field
     */
    public FieldData(final Field field, final WidgetType widgetType, final Object value) {
        init(field, widgetType);
        setValue(value);
    }

    /**
     * Sets the resource ID of the field.
     *
     * @param id resource ID
     */
    public void setId(final int id) {
        mId = id;
    }

    /**
     * Gets the resource ID of the field.
     *
     * @return resource ID
     */
    public int getId() {
        return mId;
    }

    /**
     * Sets the name of the field in the form class.
     *
     * @param name name of the field
     */
    public void setName(final String name) {
        mName = name;
    }

    /**
     * Gets the name of the field in the form class.<br>
     * This value may be obfuscated by ProGuard.
     *
     * @return name of the field
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the widget annotation given to the field definition.
     *
     * @return widget annotation
     */
    public Widget getWidget() {
        return mWidget;
    }

    /**
     * Sets the type of the widget related to this field.
     *
     * @param type type of the widget
     */
    public void setWidgetType(final WidgetType type) {
        mWidgetType = type;
    }

    /**
     * Gets the type of the widget related to this field.
     *
     * @return type of the widget
     */
    public WidgetType getWidgetType() {
        return mWidgetType;
    }

    /**
     * Sets whether the field is an array(List) or not.
     *
     * @param array true if the field is an array(List)
     */
    public void setArray(final boolean array) {
        mArray = array;
    }

    /**
     * Checks if the value of the field is an array(List).
     *
     * @return true if the field is an array(List)
     */
    public boolean isArray() {
        return mArray;
    }

    /**
     * Sets the value of the field.
     *
     * @param value value of the field
     */
    public void setValue(final Object value) {
        mValue = value;
    }

    /**
     * Sets the annotations given to the field.
     *
     * @param annotations annotations given to the field
     */
    public void setAnnotations(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            mAnnotations.put(annotation.annotationType(), annotation);
        }
        mWidget = (Widget) getAnnotation(Widget.class);
    }

    /**
     * Gets an annotation from the field.
     *
     * @param annotationClass annotation class to get
     * @return annotation object if exists
     */
    public Annotation getAnnotation(final Class<? extends Annotation> annotationClass) {
        return mAnnotations.get(annotationClass);
    }

    /**
     * Gets the value of the field from the target object.
     *
     * @return value of the field
     * @see #getValueAsString()
     * @see #getValueAsStringList()
     */
    public Object getValue() {
        return mValue;
    }

    /**
     * Gets the value of the field from the target object as String type.
     *
     * @param field target field
     * @return value as String type
     */
    public String getValueAsString() {
        if (mValue == null) {
            return null;
        }
        if (mValue instanceof String) {
            return (String) mValue;
        }
        throw new IllegalStateException(
                "Cannot convert value to String. Isn't it a List<String>?");
    }

    /**
     * Gets the value of the field from the target object as List<String> type.
     *
     * @param field target field
     * @return value as List<String> type
     */
    @SuppressWarnings("unchecked")
    public List<String> getValueAsStringList() {
        if (mValue == null) {
            return null;
        }
        if (mValue instanceof List) {
            return (List<String>) mValue;
        }
        throw new IllegalStateException(
                "Cannot convert value to List<String>. Isn't it a String?");
    }

    private void init(final Field field, final WidgetType widgetType) {
        mAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
        Widget widget = field.getAnnotation(Widget.class);
        if (widget == null) {
            throw new IllegalArgumentException("Field must have @Widget.");
        }
        setId(widget.id());
        setName(field.getName());
        setAnnotations(field.getAnnotations());
        setWidgetType(widgetType);
        final Class<?> fieldType = field.getType();
        if (fieldType.equals(List.class)) {
            setArray(true);
        }
    }
}
