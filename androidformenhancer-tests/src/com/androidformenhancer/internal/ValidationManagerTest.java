package com.androidformenhancer.internal;

import android.test.ActivityInstrumentationTestCase2;

import com.androidformenhancer.test.DefaultActivity;
import com.androidformenhancer.test.DefaultEntity;
import com.androidformenhancer.test.DefaultForm;

public class ValidationManagerTest extends ActivityInstrumentationTestCase2<DefaultActivity> {

    public ValidationManagerTest() {
        super(DefaultActivity.class);
    }

    public void testCreate() throws Throwable {
        ValidationManager vm = new ValidationManager(getActivity(), DefaultForm.class);
        vm.extractFormFromView(getActivity().findViewById(android.R.id.content));
        vm.validate();
        vm.create(DefaultEntity.class);
    }

}
