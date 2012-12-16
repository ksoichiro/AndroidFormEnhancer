
package com.androidformenhancer.sample.demos;

import com.androidformenhancer.utils.FormHelper;
import com.androidformenhancer.utils.StringUtils;
import com.androidformenhancer.validator.ValidationManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.ArrayList;

public class DefaultFragmentActivity extends FragmentActivity {

    private static final String DIALOG_TAG = "dialog";

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
            showAlertDialog(StringUtils.serialize(errorMessages));
        } else {
            // Create entity and do what you want
            // e.g. insert into database, send to server by HTTP
            DefaultEntity entity = helper.createEntityFromForm(DefaultEntity.class);
        }
    }

    private void showAlertDialog(final String message) {
        showDialogFragment(new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, null);
                Dialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                return dialog;
            }
        });
    }

    public void showDialogFragment(final DialogFragment dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        if (dialogFragment != null) {
            dialogFragment.show(ft, DIALOG_TAG);
        }
    }

}
