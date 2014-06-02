package com.androidformenhancer.helper;

import android.app.Activity;
import android.test.InstrumentationTestCase;

public class ActivityFormHelperTest extends InstrumentationTestCase {

    public static class DummyForm {
    }

    public void testInitWithEmptyActivity() {
        try {
            new ActivityFormHelper(DummyForm.class, new Activity());
        } catch (NullPointerException e) {
            return;
        }
        fail();
    }

    public void testInitWithEmptyFragmentActivity() {
        try {
            new FragmentActivityFormHelper(DummyForm.class, new android.support.v4.app.FragmentActivity());
        } catch (NullPointerException e) {
            return;
        }
        fail();
    }

    public void testInitWithEmptySupportFragment() {
        try {
            new SupportFragmentFormHelper(DummyForm.class, new android.support.v4.app.Fragment());
        } catch (NullPointerException e) {
            return;
        }
        fail();
    }

}
