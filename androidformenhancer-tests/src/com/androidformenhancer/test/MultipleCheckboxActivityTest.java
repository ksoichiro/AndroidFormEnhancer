package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class MultipleCheckboxActivityTest extends ActivityInstrumentationTestCase2<MultipleCheckboxActivity> {

    private MultipleCheckboxActivity activity;

    public MultipleCheckboxActivityTest() {
        super(MultipleCheckboxActivity.class);
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

    public void testCheckMultipleCheckBoxes() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity.findViewById(R.id.cbg_dynamic_multiple);
                ((CheckBox) parent.getChildAt(0).findViewById(R.id.cb1)).setChecked(true);
                ((CheckBox) parent.getChildAt(0).findViewById(R.id.cb2)).setChecked(true);
                activity.findViewById(R.id.btn_submit).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}
