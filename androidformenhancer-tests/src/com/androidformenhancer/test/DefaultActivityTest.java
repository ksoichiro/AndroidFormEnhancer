package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;

public class DefaultActivityTest extends ActivityInstrumentationTestCase2<DefaultActivity> {

    private DefaultActivity activity;

    public DefaultActivityTest() {
        super(DefaultActivity.class);
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
