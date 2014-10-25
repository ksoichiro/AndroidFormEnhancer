package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

public class EntrySupportFragmentActivityTest extends ActivityInstrumentationTestCase2<EntrySupportFragmentActivity> {

    private EntrySupportFragmentActivity activity;

    public EntrySupportFragmentActivityTest() {
        super(EntrySupportFragmentActivity.class);
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

    public void testSelectBirthday() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(R.id.textfield_birthday).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        sendKeys(KeyEvent.KEYCODE_TAB);
        sendKeys(KeyEvent.KEYCODE_TAB);
        sendKeys(KeyEvent.KEYCODE_TAB); // Year
        sendKeys(KeyEvent.KEYCODE_TAB); // Month
        sendKeys(KeyEvent.KEYCODE_TAB); // Day
        sendKeys(KeyEvent.KEYCODE_TAB); // Done
        sendKeys(KeyEvent.KEYCODE_ENTER);
        getInstrumentation().waitForIdleSync();
    }
}
