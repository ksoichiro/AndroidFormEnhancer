package com.androidformenhancer.helper;

import android.test.ActivityInstrumentationTestCase2;

import com.androidformenhancer.R;
import com.androidformenhancer.internal.ValidationManager;
import com.androidformenhancer.test.DefaultActivity;
import com.androidformenhancer.test.DefaultEntity;
import com.androidformenhancer.test.DefaultForm;

public class FormHelperTest extends ActivityInstrumentationTestCase2<DefaultActivity> {

    private DefaultActivity activity;

    public FormHelperTest() {
        super(DefaultActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testCreate() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FormHelper helper = new ActivityFormHelper(DefaultForm.class, activity);
                helper.validate();
                helper.create(DefaultEntity.class);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testShowAlertDialog() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FormHelper helper = new ActivityFormHelper(DefaultForm.class, activity);
                helper.showAlertDialog("Test", "Message");
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testShowAlertDialog2() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FormHelper helper = new ActivityFormHelper(DefaultForm.class, activity);
                helper.showAlertDialog(com.androidformenhancer.test.R.string.app_name,
                        com.androidformenhancer.test.R.string.msg_validation_sample);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testShowAlertDialog3() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FormHelper helper = new ActivityFormHelper(DefaultForm.class, activity);
                helper.showAlertDialog(com.androidformenhancer.test.R.string.msg_validation_sample);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testShowAlertDialog4() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FormHelper helper = new ActivityFormHelper(DefaultForm.class, activity);
                helper.showAlertDialog(com.androidformenhancer.test.R.string.app_name,
                        com.androidformenhancer.test.R.string.msg_validation_sample,
                        true);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testShowAlertDialog5() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FormHelper helper = new ActivityFormHelper(DefaultForm.class, activity);
                helper.showAlertDialog(com.androidformenhancer.test.R.string.msg_validation_sample,
                        true);
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}
