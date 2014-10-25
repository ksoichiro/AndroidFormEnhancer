package com.androidformenhancer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class DontStopActivityTest extends ActivityInstrumentationTestCase2<DontStopActivity> {

    private DontStopActivity activity;

    public DontStopActivityTest() {
        super(DontStopActivity.class);
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
                ((EditText) activity.findViewById(R.id.textfield_name)).setText("Android");
                ((EditText) activity.findViewById(R.id.textfield_hiragana)).setText("あんどろいど");
                ((EditText) activity.findViewById(R.id.textfield_katakana)).setText("アンドロイド");
                ((EditText) activity.findViewById(R.id.textfield_age)).setText("21");
                ((RadioButton) activity.findViewById(R.id.radio_gender_male)).setChecked(true);
                ((EditText) activity.findViewById(R.id.textfield_phone)).setText("111-1111-1111");
                ((EditText) activity.findViewById(R.id.textfield_birthday)).setText("2008/10/21");
                ((Spinner) activity.findViewById(R.id.spn_credit_card_company)).setSelection(1);
                ((CheckBox) activity.findViewById(R.id.cb_got_to_know_by_internet)).setChecked(true);
                activity.findViewById(R.id.btn_submit).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

}
