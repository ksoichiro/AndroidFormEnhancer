package com.androidformenhancer.helper;

import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.androidformenhancer.internal.impl.DatePickerDialogFragment;
import com.androidformenhancer.test.DefaultActivity;
import com.androidformenhancer.test.DefaultForm;
import com.androidformenhancer.test.EntrySupportFragment;
import com.androidformenhancer.test.EntrySupportFragmentActivity;

public class SupportFragmentFormHelperIllegalStateTest extends ActivityInstrumentationTestCase2<EntrySupportFragmentActivity> {

    Fragment fragment;

    public SupportFragmentFormHelperIllegalStateTest() {
        super(EntrySupportFragmentActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragment = getActivity().getSupportFragmentManager().findFragmentById(com.androidformenhancer.test.R.id.parent);
    }

    public void testActivityFormHelper_ValidateWithoutContext() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        helper.setContext(null);
        try {
            helper.validate();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Cannot create ValidationManager. Context is required", e.getMessage());
        }
    }

    public void testActivityFormHelper_GetFormBeforeValidate() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        try {
            helper.getForm();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Form is not initialized or validated.", e.getMessage());
        }
    }

    public void testActivityFormHelper_ShowAlertDialogWithoutFragment() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        helper.setFragment(null);
        try {
            helper.showAlertDialog("Test", "Hello, world!", true);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("You cannot use this method without FragmentActivity "
                            + "because this method use DialogFragment. "
                            + "Check that you set a FragmentActivity instance to the constructor.",
                    e.getMessage());
        }
    }

    public void testActivityFormHelper_ShowDatePickerDialogWithoutFragment() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        helper.setFragment(null);
        try {
            helper.showDatePickerDialog(com.androidformenhancer.test.R.id.textfield_birthday, com.androidformenhancer.test.R.string.msg_validation_sample);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("You cannot use this method without FragmentActivity "
                            + "because this method use DialogFragment. "
                            + "Check that you set a FragmentActivity instance to the constructor.",
                    e.getMessage());
        }
    }

    public void testActivityFormHelper_ShowDatePickerDialogToNonTextView() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        try {
            helper.showDatePickerDialog(android.R.id.content, com.androidformenhancer.test.R.string.msg_validation_sample);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Target view must be valid TextView: "));
        }
    }

    public void testActivityFormHelper_SetAsDateFieldWithoutFragment() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        helper.setFragment(null);
        try {
            helper.setAsDateField(com.androidformenhancer.test.R.id.textfield_birthday, com.androidformenhancer.test.R.string.msg_validation_sample);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("You cannot use this method without FragmentActivity "
                            + "because this method use DialogFragment. "
                            + "Check that you set a FragmentActivity instance to the constructor.",
                    e.getMessage());
        }
    }

    public void testActivityFormHelper_ShowDialogFragmentWithoutFragment() {
        SupportFragmentFormHelper helper = new SupportFragmentFormHelper(DefaultForm.class, fragment);
        helper.setFragment(null);
        try {
            helper.showDialogFragment(new DatePickerDialogFragment());
            fail();
        } catch (IllegalStateException e) {
            assertEquals("You cannot use this method without FragmentActivity "
                            + "because this method use DialogFragment. "
                            + "Check that you set a FragmentActivity instance to the constructor.",
                    e.getMessage());
        }
    }

}
