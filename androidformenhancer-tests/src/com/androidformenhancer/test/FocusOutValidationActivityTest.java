package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

public class FocusOutValidationActivityTest extends ActivityInstrumentationTestCase2<FocusOutValidationActivity> {

    private FocusOutValidationActivity activity;

    public FocusOutValidationActivityTest() {
        super(FocusOutValidationActivity.class);
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

    public void testFocusOut() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.textfield_name).requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_TAB); // Focus out
        getInstrumentation().waitForIdleSync();
    }

}
