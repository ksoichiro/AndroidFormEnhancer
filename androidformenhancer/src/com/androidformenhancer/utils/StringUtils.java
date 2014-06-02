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

import java.util.List;

public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Converts the list of the error messages to one string joined with the
     * line separator.<br>
     * Returns blank string if the errorMessages is null.
     *
     * @param errorMessages the list of the error messages to convert
     * @return converted messages
     */
    public static String serialize(final List<String> errorMessages) {
        if (errorMessages == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final String lineSeparator = System.getProperty("line.separator");
        for (String error : errorMessages) {
            sb.append(lineSeparator + error);
        }
        if (sb.length() > 0) {
            return sb.toString().substring(lineSeparator.length());
        }
        return "";
    }

}
