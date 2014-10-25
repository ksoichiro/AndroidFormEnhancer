package com.androidformenhancer.helper;

import android.test.ActivityInstrumentationTestCase2;

import com.androidformenhancer.internal.impl.DatePickerDialogFragment;
import com.androidformenhancer.test.DefaultActivity;
import com.androidformenhancer.test.DefaultForm;
import com.androidformenhancer.test.EntryActivity;
import com.androidformenhancer.test.EntryForm;

public class FragmentActivityFormHelperIllegalStateTest extends ActivityInstrumentationTestCase2<EntryActivity> {

    public FragmentActivityFormHelperIllegalStateTest() {
        super(EntryActivity.class);
    }

    public void testActivityFormHelper_ValidateWithoutContext() {
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
        helper.setContext(null);
        try {
            helper.validate();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Cannot create ValidationManager. Context is required", e.getMessage());
        }
    }

    public void testActivityFormHelper_GetFormBeforeValidate() {
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
        try {
            helper.getForm();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Form is not initialized or validated.", e.getMessage());
        }
    }

    public void testActivityFormHelper_ShowAlertDialogWithoutActivity() {
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
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
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
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
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
        try {
            helper.showDatePickerDialog(android.R.id.content, com.androidformenhancer.test.R.string.msg_validation_sample);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Target view must be valid TextView: "));
        }
    }

    public void testActivityFormHelper_SetAsDateFieldWithoutActivity() {
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
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
        FragmentActivityFormHelper helper = new FragmentActivityFormHelper(EntryForm.class, getActivity());
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
