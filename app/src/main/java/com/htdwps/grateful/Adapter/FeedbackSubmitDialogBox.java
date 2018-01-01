package com.htdwps.grateful.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Model.Feedback;
import com.htdwps.grateful.Model.User;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;

/**
 * Created by HTDWPS on 12/31/17.
 */

public class FeedbackSubmitDialogBox extends LayoutInflater implements View.OnTouchListener {

    private AlertDialog alertDialog;
    private DatabaseReference feedbackReference;
    private String user_feedback_text;
    private EditText feedback_text_field;

    public FeedbackSubmitDialogBox(Context context) {
        super(context);
    }


    @Override
    public LayoutInflater cloneInContext(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.alert_dialog_feedback_window, null);

        feedbackReference = FirebaseUtil.getFeedbackRef();
        final User user = FirebaseUtil.getCurrentUser();

        feedback_text_field = (EditText) view.findViewById(R.id.wyd_feedback_add);

        feedback_text_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() < 10) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryLight));
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                }

            }
        });

        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (feedback_text_field.getText().toString().length() < 10 || feedback_text_field.getText() == null) {

                            feedback_text_field.setError("Your input is too short. Please enter at least 10 characters.");

                        } else {

                            user_feedback_text = feedback_text_field.getText().toString();
                            feedback_text_field.setText("");

                            Feedback user_feedback = new Feedback(user, user_feedback_text, ServerValue.TIMESTAMP, false);

                            feedbackReference.push().setValue(user_feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(getContext(), "Your feedback has been submitted. We thank you for your input to improve our app.", Toast.LENGTH_SHORT).show();

                                }

                            });
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        alertDialog = alertDialogBuilder.create();

        // Set color of the AlertDialog positive and negative buttons
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimaryLight));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
        });

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        return layoutInflater;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
