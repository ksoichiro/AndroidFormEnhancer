package com.androidformenhancer.sample.demos.test.robolectric;

import android.widget.Button;

import com.androidformenhancer.sample.demos.DefaultActivity;
import com.androidformenhancer.sample.demos.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class DefaultActivityTest {

    private DefaultActivity activity;
    private Button sendButton;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(DefaultActivity.class).create().visible().get();
        sendButton = (Button) activity.findViewById(R.id.btn_submit);
    }

    @Test
    public void shouldHaveAButtonThatSaysSubmit() throws Exception {
        assertThat((String) sendButton.getText(), equalTo("Submit"));
    }

}
