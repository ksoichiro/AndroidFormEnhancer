package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;

public class ValidationErrorIconDisabledActivityTest extends ActivityInstrumentationTestCase2<ValidationErrorIconDisabledActivity> {

    private ValidationErrorIconDisabledActivity activity;

    public ValidationErrorIconDisabledActivityTest() {
        super(ValidationErrorIconDisabledActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testSubmit() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.btn_submit).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

}
