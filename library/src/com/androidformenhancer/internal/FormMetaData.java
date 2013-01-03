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

import com.androidformenhancer.WidgetType;

/**
 * Convenient container class to retrieve field information.
 * 
 * @author Soichiro Kashima
 */
public class FormMetaData {

    private int mId;
    private WidgetType mWidgetType;
    private Object mValue;

    public void setId(final int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setWidgetType(final WidgetType type) {
        mWidgetType = type;
    }

    public WidgetType getWidgetType() {
        return mWidgetType;
    }

    public void setValue(final Object value) {
        mValue = value;
    }

    public Object getValue() {
        return mValue;
    }
}
