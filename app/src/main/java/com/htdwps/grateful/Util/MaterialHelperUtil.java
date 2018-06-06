package com.htdwps.grateful.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.Feedback;
import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 6/5/18.
 */
public class MaterialHelperUtil {

//    Context mContext;
    private static final DatabaseReference feedbackDatabaseReference = FirebaseUtil.getFeedbackRef();

    public static void submitFeedbackToDeveloper(final Context context, final CustomUser customUser) {

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(R.string.material_dialog_submit_feedback)
                .content(R.string.material_dialog_feedback_content)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                .input(R.string.material_dialog_feedback_hint, R.string.material_dialog_feedback_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (TextUtils.isEmpty(input) || input.length() < 10) {

                            Toast.makeText(context, "Your feedback is too short.", Toast.LENGTH_SHORT).show();

                        } else {

                            Feedback user_feedback = new Feedback(customUser, input.toString(), ServerValue.TIMESTAMP, false);

                            feedbackDatabaseReference.push().setValue(user_feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context, "Your feedback has been submitted. We thank you for your input to improve our app.", Toast.LENGTH_SHORT).show();

                                }

                            });

                        }
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();

    }
}
