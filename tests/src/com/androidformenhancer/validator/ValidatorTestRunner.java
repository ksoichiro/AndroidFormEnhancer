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

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import junit.framework.TestSuite;

/**
 * @author Soichiro Kashima
 */
public class ValidatorTestRunner extends InstrumentationTestRunner {

    @Override
    public TestSuite getAllTests() {
        final InstrumentationTestSuite testSuite = new InstrumentationTestSuite(this);

        testSuite.addTestSuite(RequiredValidatorTest.class);
        testSuite.addTestSuite(DigitsValidatorTest.class);
        testSuite.addTestSuite(MaxLengthValidatorTest.class);
        testSuite.addTestSuite(MaxValueValidatorTest.class);
        testSuite.addTestSuite(NumberValidatorTest.class);

        return testSuite;
    }

    @Override
    public ClassLoader getLoader() {
        return ValidatorTestRunner.class.getClassLoader();
    }

}
