package com.androidformenhancer.helper;

import android.test.ActivityInstrumentationTestCase2;

import com.androidformenhancer.helper.ActivityFormHelper;
import com.androidformenhancer.internal.impl.DatePickerDialogFragment;
import com.androidformenhancer.test.DefaultActivity;
import com.androidformenhancer.test.DefaultForm;

public class ActivityFormHelperIllegalStateTest extends ActivityInstrumentationTestCase2<DefaultActivity> {

    public ActivityFormHelperIllegalStateTest() {
        super(DefaultActivity.class);
    }

    public void testActivityFormHelper_ValidateWithoutContext() {
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        helper.setContext(null);
        try {
            helper.validate();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Cannot create ValidationManager. Context is required", e.getMessage());
        }
    }

    public void testActivityFormHelper_GetFormBeforeValidate() {
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        try {
            helper.getForm();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Form is not initialized or validated.", e.getMessage());
        }
    }

    public void testActivityFormHelper_ShowAlertDialogWithoutActivity() {
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        helper.setActivity(null);
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

    public void testActivityFormHelper_ShowDatePickerDialogWithoutActivity() {
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        helper.setActivity(null);
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
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        try {
            helper.showDatePickerDialog(android.R.id.content, com.androidformenhancer.test.R.string.msg_validation_sample);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Target view must be valid TextView: "));
        }
    }

    public void testActivityFormHelper_SetAsDateFieldWithoutActivity() {
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        helper.setActivity(null);
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

    public void testActivityFormHelper_ShowDialogFragmentWithoutActivity() {
        ActivityFormHelper helper = new ActivityFormHelper(DefaultForm.class, getActivity());
        helper.setActivity(null);
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
