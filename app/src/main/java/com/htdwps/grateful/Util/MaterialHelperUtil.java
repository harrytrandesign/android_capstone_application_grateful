package com.htdwps.grateful.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.Feedback;
import com.htdwps.grateful.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HTDWPS on 6/5/18.
 */
public class MaterialHelperUtil {

    //    Context mContext;
    private static final DatabaseReference feedbackDatabaseReference = FirebaseUtil.getFeedbackRef();

    // Submit a grateful bean to the fb database here
    public static void createMaterialDialogBeanCreator(final Context context, View view, ArrayAdapter<String> adapter, final String[] emojiList, final String[] emotionList, final CustomUser customUser) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        final View theView = layoutInflater.inflate(R.layout.material_dialog_custom_layout_view, null);
        final EditText editText = theView.findViewById(R.id.et_beans_message_textbox);
        final EditText tagText = theView.findViewById(R.id.et_beans_extra_taglist);
        final CheckBox checkBox = theView.findViewById(R.id.checkbox_public_box);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean isPublic = b;
            }
        });

        final TextView expressionTextLabel = theView.findViewById(R.id.tv_mood_expression_text);
        final Spinner expressionDrop = theView.findViewById(R.id.spinner_emoji_expression_moods_dropdown);
        expressionDrop.setAdapter(adapter);
        expressionDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int expressionValue = i;

                expressionTextLabel.setText(emotionList[i]);
                Toast.makeText(context, emojiList[i] + " " + expressionValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new MaterialDialog.Builder(context)
                .customView(theView, false)
                .positiveText("Submit")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String text = editText.getText().toString();
                        String tags = tagText.getText().toString();

                        if (TextUtils.isEmpty(text) || text.length() < 10) {
                            editText.setError("Sorry, your message is too short");
                            Toast.makeText(context, "Sorry, your message is too short", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            int num;
//                            expressionDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                    num = i;
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                }
//                            });
                            List<String> items = new ArrayList<String>(Arrays.asList(tags.split("\\s*,\\s*")));
//                            List<String> list = new ArrayList<String>(Arrays.asList(tags.split(" , ")));

                            ArrayList<String> list = new ArrayList<>(items);
                            // Option 1
//                            list.addAll(items);
                            // Option 2
//                            for (String tag : items) {
//                                list.add(tag);
//                            }

                            Beans beans = new Beans(customUser, expressionDrop.getSelectedItemPosition(), text, ServerValue.TIMESTAMP, list, checkBox.isChecked());
                            FirebaseUtil.getUserPostRef().child(customUser.getUserid()).push().setValue(beans).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        // Connect this part to Firebase Database UI. Extend a helper method class
                    }
                })
                .show();
    }

    // Submit feedback to developer, feedback goes to the fb database
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
