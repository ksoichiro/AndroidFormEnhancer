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

import com.androidformenhancer.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Result of the validation such as validated widget IDs and error messages.
 *
 * @author Soichiro Kashima
 */
public final class ValidationResult {

    private ArrayList<Integer> mValidatedIds;
    private LinkedHashMap<Integer, ArrayList<String>> mErrorMessages;

    /**
     * Constructor.
     */
    public ValidationResult() {
        mValidatedIds = new ArrayList<Integer>();
        mErrorMessages = new LinkedHashMap<Integer, ArrayList<String>>();
    }

    /**
     * Adds a widget ID to be validated.
     *
     * @param id ID to add
     */
    public void addValidatedId(final int id) {
        mValidatedIds.add(id);
    }

    /**
     * Returns the widget IDs which has been validated.
     *
     * @return list of widget IDs
     */
    public ArrayList<Integer> getValidatedIds() {
        return mValidatedIds;
    }

    /**
     * Check if there were any errors on the validation.
     *
     * @return true if there were some errors
     */
    public boolean hasError() {
        return mErrorMessages.size() > 0;
    }

    /**
     * Check if there were any errors for the specific widget on the validation.
     *
     * @param id ID of the target widget
     * @return true if there were some errors
     */
    public boolean hasErrorFor(final int id) {
        return mErrorMessages.containsKey(id);
    }

    /**
     * Adds a validation error message for the specified widget.
     *
     * @param id           ID of the target widget
     * @param errorMessage validation error message to add
     */
    public void addError(final int id, final String errorMessage) {
        if (!mErrorMessages.containsKey(id)) {
            ArrayList<String> list = new ArrayList<String>();
            mErrorMessages.put(id, list);
        }
        mErrorMessages.get(id).add(errorMessage);
    }

    /**
     * Returns the validation error messages for the specified widget as a list.<br>
     * Note that this method returns empty list when there are no errors.
     *
     * @param id ID of the target widget
     * @return list of error messages
     */
    public ArrayList<String> getErrorsFor(final int id) {
        if (mErrorMessages.containsKey(id)) {
            return mErrorMessages.get(id);
        }
        return new ArrayList<String>();
    }

    /**
     * Gets the set of the widget IDs which have some validation errors.
     *
     * @return set of the IDs
     */
    public Set<Integer> getErrorIds() {
        return mErrorMessages.keySet();
    }

    /**
     * Returns all the validation error messages of all the widget as a list.
     *
     * @return list of error messages
     */
    public ArrayList<String> getAllErrors() {
        ArrayList<String> stringList = new ArrayList<String>();
        for (int key : mErrorMessages.keySet()) {
            stringList.addAll(mErrorMessages.get(key));
        }
        return stringList;
    }

    /**
     * Returns all the validation error messages of all the widget as a String.<br>
     * If there are two or more errors, this method inserts new line character
     * between error messages.
     *
     * @return error messages
     */
    public String getAllSerializedErrors() {
        return StringUtils.serialize(getAllErrors());
    }

}
