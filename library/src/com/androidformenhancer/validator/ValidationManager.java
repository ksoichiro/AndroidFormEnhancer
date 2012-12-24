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
import com.androidformenhancer.internal.FieldOrderComparator;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The utility to validate input values.
 * 
 * @author Soichiro Kashima
 */
public final class ValidationManager {

    private static final int STOP_POLICY_CONTINUE_ALL = 0;
    private static final int STOP_POLICY_STOP_ALL_IF_ANY = 1;
    private static final int STOP_POLICY_STOP_AND_RESUME_NEXT = 2;

    private Context mContext;
    private int mStopPolicy;
    private List<Validator> mValidators;

    public ValidationManager(final Context context) {
        mContext = context;
        mValidators = new ArrayList<Validator>();
        init();
    }

    private void init() {
        mValidators.clear();
        TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
                R.styleable.ValidatorDefinitions,
                R.attr.afeValidatorDefinitions, 0);

        mStopPolicy = a.getInt(R.styleable.ValidatorDefinitions_afeStopPolicy,
                STOP_POLICY_CONTINUE_ALL);

        CharSequence[] standards =
                a.getTextArray(R.styleable.ValidatorDefinitions_afeStandardValidators);

        if (standards == null) {
            // Use default standard validators if not specified with style.
            standards = mContext.getResources().getTextArray(R.array.afe__validators);
        }

        if (standards != null) {
            for (CharSequence validatorClassName : standards) {
                try {
                    Class<?> validatorClass = Class.forName(validatorClassName.toString());
                    Validator validator = (Validator) validatorClass.newInstance();
                    validator.setContext(mContext);
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

        CharSequence[] customs =
                a.getTextArray(R.styleable.ValidatorDefinitions_afeCustomValidators);

        if (customs != null) {
            for (CharSequence validatorClassName : customs) {
                try {
                    Class<?> validatorClass = Class.forName(validatorClassName.toString());
                    Validator validator = (Validator) validatorClass.newInstance();
                    validator.setContext(mContext);
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

    /**
     * Validates the input values.
     * <p>
     * Validations are executed in the orders specified by the
     * {@link android.androsuit.entity.annotation.Order}. If this annotation is
     * not specified, the order is determined by field names(asc). The fields
     * with the annotations are prior to the others.
     * 
     * @param context context to access the message resources
     * @param target target object to be validated (the form)
     * @return list to save the error messages
     */
    public ArrayList<String> validate(final Object target) {
        ArrayList<String> errorMessages = new ArrayList<String>();

        // Set validation target
        for (Validator validator : mValidators) {
            validator.setTarget(target);
        }

        // Gets all the public fields and validate
        Field[] fields = target.getClass().getFields();
        Arrays.sort(fields, new FieldOrderComparator());
        validation: for (Field field : fields) {
            for (Validator validator : mValidators) {
                String errorMessage = validator.validate(field);
                if (!TextUtils.isEmpty(errorMessage)) {
                    errorMessages.add(errorMessage);
                    if (mStopPolicy == STOP_POLICY_STOP_ALL_IF_ANY) {
                        break validation;
                    }
                    if (mStopPolicy == STOP_POLICY_STOP_AND_RESUME_NEXT) {
                        break;
                    }
                }
            }
        }

        return errorMessages;
    }
}
