package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class VarietyActivityTest extends ActivityInstrumentationTestCase2<VarietyActivity> {

    private VarietyActivity activity;

    public VarietyActivityTest() {
        super(VarietyActivity.class);
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

    public void testSubmit_Valid() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText) activity.findViewById(R.id.textfield_string)).setText("Android");
                ((EditText) activity.findViewById(R.id.textfield_int)).setText("21");
                ((EditText) activity.findViewById(R.id.textfield_long)).setText("10000000000");
                ((EditText) activity.findViewById(R.id.textfield_float)).setText("1.2");
                ((EditText) activity.findViewById(R.id.textfield_double)).setText("1.2");
                ((EditText) activity.findViewById(R.id.textfield_boolean)).setText("true");
                ((EditText) activity.findViewById(R.id.textfield_short)).setText("1");
                ((EditText) activity.findViewById(R.id.textfield_char)).setText("a");
                activity.findViewById(R.id.btn_submit).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

}
