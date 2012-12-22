
package com.androidformenhancer.validator;

import com.androidformenhancer.form.annotation.Digits;
import com.androidformenhancer.test.DummyActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Test case for DigitsValidator.<br>
 * Include AndroidFormEnhancer project as library and run as Android JUnit test.
 * 
 * @author Soichiro Kashima
 */
public class DigitsValidatorTest extends ActivityInstrumentationTestCase2<DummyActivity> {

    public DigitsValidatorTest() {
        super(DummyActivity.class);
    }

    public DigitsValidatorTest(Class<DummyActivity> activityClass) {
        super(activityClass);
    }

    /**
     * Dummy class which has @Digits field.
     */
    public class Foo {
        @Digits
        public String a;
    }

    public void testValidate() throws Exception {
        Foo foo = new Foo();

        DigitsValidator validator = new DigitsValidator();
        validator.setContext(getActivity());
        validator.setTarget(foo);
        Field field = Foo.class.getDeclaredField("a");

        // Digits
        foo.a = "123";
        String errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNull(errorMessage);

        // Not digits
        foo.a = "a";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Partially digits
        foo.a = "a1";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Not digits and double byte
        foo.a = "あいうえお";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Digits but double byte
        foo.a = "１２３";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNotNull(errorMessage);

        // Empty
        foo.a = "";
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNull(errorMessage);

        // Null
        foo.a = null;
        errorMessage = validator.validate(field);
        Log.i("TEST", "testValidate: " + errorMessage);
        assertNull(errorMessage);
    }
}
