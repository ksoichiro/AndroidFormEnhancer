
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

        testSuite.addTestSuite(DigitsValidatorTest.class);

        return testSuite;
    }

    @Override
    public ClassLoader getLoader() {
        return ValidatorTestRunner.class.getClassLoader();
    }

}
