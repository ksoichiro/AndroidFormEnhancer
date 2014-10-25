package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;

import com.androidformenhancer.helper.ActivityFormHelper;
import com.androidformenhancer.helper.FormHelper;

public class IllegalStateTest extends ActivityInstrumentationTestCase2<DefaultActivity> {

    public IllegalStateTest() {
        super(DefaultActivity.class);
    }

    public void testFormHelperWithoutContext() {
        FormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        helper.setContext(null);
        try {
            helper.validate();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Cannot create ValidationManager. Context is required", e.getMessage());
        }
    }

}
