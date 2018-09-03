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
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Model.CommentBean;
import com.htdwps.grateful.Model.Feedback;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HTDWPS on 6/5/18.
 */
public class MaterialDialogHelperUtil {

    private static final int MIN_FEEDBACK_LENGTH = 10;
    private static final int MAX_FEEDBACK_LENGTH = 300;

    private static DatabaseReference feedbackforDeveloperDatabaseReference = FirebaseUtil.getFeedbackForDeveloperDirectoryReference();

    public static void generateInspirationalQuote(Context context) {
        MaterialDialog dialog;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(R.string.tv_quote_inspire_daily)
                .content(QuoteGetRequestHelperUtil.runQuoteRequest())
                .positiveText(R.string.tv_close_quote);

        dialog = builder.build();
        dialog.show();
    }

    // Submit feedback to developer, feedback goes to the fb database
    public static void submitFeedbackToDeveloper(final Context context, final UserProfile userProfile) {

        MaterialDialog dialog;

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(R.string.material_dialog_submit_feedback)
                .content(R.string.material_dialog_feedback_content)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                .inputRange(MIN_FEEDBACK_LENGTH, MAX_FEEDBACK_LENGTH, context.getResources().getColor(R.color.colorAccent))
                .input(R.string.material_dialog_feedback_hint, R.string.material_dialog_feedback_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        String feedbackTextValue = input.toString();
                        Object feedbackTimeSubmitted = ServerValue.TIMESTAMP;

                        if (TextUtils.isEmpty(feedbackTextValue) || feedbackTextValue.length() < 10) {

                            Toast.makeText(context, R.string.string_feedback_length_too_short, Toast.LENGTH_SHORT).show();
                            // Repeat the feedback box
                            submitFeedbackToDeveloper(context, userProfile);

                        } else {

                            Feedback userFeedbackForDeveloper = new Feedback(userProfile, feedbackTextValue, feedbackTimeSubmitted);

                            feedbackforDeveloperDatabaseReference.push().setValue(userFeedbackForDeveloper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context, R.string.string_feedback_received_response, Toast.LENGTH_SHORT).show();

                                }

                            });

                        }
                    }
                })
                .positiveText("Ok")
                .negativeText("Close");

        dialog = builder.build();
        dialog.show();

    }

    // Unused method
    private static void populateCommentBoxForPost(final Context context, final UserProfile userProfile, final String postKeyPushId, final DatabaseReference commentDirectoryRef) {

        MaterialDialog materialDialog;

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title("Add a comment")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                .input("Must be at least 10 characters long.", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                        if (TextUtils.isEmpty(input) || input.length() < 10) {

                            Toast.makeText(context, R.string.toast_comment_too_short, Toast.LENGTH_SHORT).show();
                            populateCommentBoxForPost(context, userProfile, postKeyPushId, commentDirectoryRef);

                        } else {

                            // Submit to database
                            String commentGeneratePushId = commentDirectoryRef.push().getKey();
                            CommentBean commentBean = new CommentBean(userProfile, postKeyPushId, commentGeneratePushId, input.toString(), ServerValue.TIMESTAMP);

                            commentDirectoryRef.child(commentGeneratePushId).setValue(commentBean).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context, R.string.toast_comment_posted, Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }
                });

        materialDialog = builder.build();
        materialDialog.show();

    }

    // Unused method
    // Submit a grateful bean to the fb database here
    public static void createMaterialDialogBeanCreator(final Context context, View view, ArrayAdapter<String> adapter, final String[] emojiList, final String[] emotionList, final UserProfile userProfile) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        final View theView = layoutInflater.inflate(R.layout.material_dialog_custom_layout_view, null);
        final EditText editText = theView.findViewById(R.id.et_beans_message_textbox);
        final EditText tagText = theView.findViewById(R.id.et_tags_by_comma_textbox);
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
            public void onItemSelected(AdapterView<?> adapterView, View view, int expressionValue, long l) {

                expressionTextLabel.setText(emotionList[expressionValue]);
                Toast.makeText(context, emojiList[expressionValue] + " " + expressionValue, Toast.LENGTH_SHORT).show();
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
                            editText.setError(context.getString(R.string.string_message_too_short_error_alert));
                            Toast.makeText(context, context.getString(R.string.string_message_too_short_error_alert), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            int num;

                            List<String> items = new ArrayList<>(Arrays.asList(tags.split("\\s*,\\s*")));

                            ArrayList<String> list = new ArrayList<>(items);

                            BeanPosts beanPosts = new BeanPosts(userProfile, expressionDrop.getSelectedItemPosition(), text, ServerValue.TIMESTAMP, list, checkBox.isChecked(), "");
                            FirebaseUtil.getPrivateUserBeanPostDirectoryReference().child(userProfile.getUserid()).push().setValue(beanPosts).addOnCompleteListener(new OnCompleteListener<Void>() {
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

}
