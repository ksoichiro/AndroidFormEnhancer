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

import com.androidformenhancer.annotation.Order;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Compares the order of the fields by {@link Order} annotation.
 * <p>
 * This comparator evaluates the smaller value to the forward. If the field to
 * compare has no annotations, the comparator evaluates to backward, and
 * determines the order by field names.
 * 
 * @author Soichiro Kashima
 */
public final class FieldOrderComparator implements Comparator<Field> {
    @Override
    public int compare(final Field object1, final Field object2) {
        // Field which has Order annotaion will be evaluated to forward
        final Order order1 = object1.getAnnotation(Order.class);
        final Order order2 = object2.getAnnotation(Order.class);
        if (order1 != null && order2 != null) {
            return order1.value() - order2.value();
        } else if (order1 == null && order2 != null) {
            return 1;
        } else if (order1 != null && order2 == null) {
            return -1;
        }
        return object1.getName().compareTo(object2.getName());
    }
}
