
package com.androidformenhancer.sample.demos;

import com.androidformenhancer.utils.FormHelper;
import com.androidformenhancer.utils.StringUtils;
import com.androidformenhancer.validator.ValidationManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DefaultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
    }

    public void onSubmit(View v) {
        FormHelper<DefaultForm> helper = new FormHelper<DefaultForm>();

        DefaultForm form = helper.extractFormFromView(this, DefaultForm.class);

        // Validation
        ValidationManager validationManager = new ValidationManager(this);
        ArrayList<String> errorMessages = validationManager.validate(form);
        if (errorMessages.size() > 0) {
            // Error
            Toast.makeText(
                    this,
                    StringUtils.serialize(errorMessages),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Create entity and do what you want
            // e.g. insert into database, send to server by HTTP
            DefaultEntity entity = helper.createEntityFromForm(DefaultEntity.class);
        }
    }

}
