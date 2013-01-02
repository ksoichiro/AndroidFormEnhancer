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
 * @author Soichiro Kashima
 */
public final class ValidationResult {

    private ArrayList<Integer> mValidatedIds;
    private LinkedHashMap<Integer, ArrayList<String>> mErrorMessages;

    public ValidationResult() {
        mValidatedIds = new ArrayList<Integer>();
        mErrorMessages = new LinkedHashMap<Integer, ArrayList<String>>();
    }

    public void addValidatedId(final int id) {
        mValidatedIds.add(id);
    }

    public ArrayList<Integer> getValidatedIds() {
        return mValidatedIds;
    }

    public boolean hasError() {
        return mErrorMessages.size() > 0;
    }

    public boolean hasErrorFor(final int id) {
        return mErrorMessages.containsKey(id);
    }

    public void addError(final int id, final String errorMessage) {
        if (!mErrorMessages.containsKey(id)) {
            ArrayList<String> list = new ArrayList<String>();
            mErrorMessages.put(id, list);
        }
        mErrorMessages.get(id).add(errorMessage);
    }

    public ArrayList<String> getErrorsFor(final int id) {
        if (mErrorMessages.containsKey(id)) {
            return mErrorMessages.get(id);
        }
        return new ArrayList<String>();
    }

    public Set<Integer> getIds() {
        return mErrorMessages.keySet();
    }

    public ArrayList<String> getAllErrors() {
        ArrayList<String> stringList = new ArrayList<String>();
        for (int key : mErrorMessages.keySet()) {
            stringList.addAll(mErrorMessages.get(key));
        }
        return stringList;
    }

    public String getAllSerializedErrors() {
        return StringUtils.serialize(getAllErrors());
    }

}
