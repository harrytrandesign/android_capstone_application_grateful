package com.htdwps.grateful;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.htdwps.grateful.Adapter.CustomSpinnerArrayAdapter;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.MoodCount;
import com.htdwps.grateful.Model.TagName;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class SubmitBeanActivity extends AppCompatActivity {

    private AdView mAdview;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText editText;
    EditText tagText;
    CheckBox checkBox;
    TextView expressionTextLabel;
    ArrayAdapter<String> emojiExpressionAdapter;
    Spinner expressionDropdown;
    String[] emojiList;
    String[] emotionList;
    int expressionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bean);
        setTitle("New Post");

//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        mAdview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        emojiExpressionAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, EmojiSelectUtil.emojiForSpinnerDropdown);
        CustomSpinnerArrayAdapter customSpinnerArrayAdapter = new CustomSpinnerArrayAdapter(this, R.layout.spinner_item_picker_layout, EmojiSelectUtil.emojiForSpinnerDropdown);
        emojiList = EmojiSelectUtil.emojiForSpinnerDropdown;
        emotionList = EmojiSelectUtil.emojiExpressionTextValue;

        editText = findViewById(R.id.et_beans_message_textbox);
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        tagText = findViewById(R.id.et_beans_extra_taglist);
        checkBox = findViewById(R.id.checkbox_public_box);
//        expressionTextLabel = findViewById(R.id.tv_mood_expression_text);
        expressionDropdown = findViewById(R.id.spinner_emoji_expression_moods_dropdown);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean isPublic = b;
            }
        });

        // Testing a custom spinner adapter and layout
//        expressionDropdown.setAdapter(emojiExpressionAdapter);
        expressionDropdown.setAdapter(customSpinnerArrayAdapter);
        expressionDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                expressionValue = i;

//                expressionTextLabel.setText(emotionList[i]);
                Toast.makeText(SubmitBeanActivity.this, emojiList[i] + " " + expressionValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.settings_submit_new_bean:

                Toast.makeText(this, "Submit this data to the database, on complete go back to the previous activity.", Toast.LENGTH_SHORT).show();

                String beanMessage = editText.getText().toString();
                String beanTagArray = tagText.getText().toString();

                if (TextUtils.isEmpty(beanMessage) || beanMessage.length() < 10) {

                    editText.isFocused();
                    editText.setError("Sorry, your message is too short");

                } else {

                    boolean postIsPublic = checkBox.isChecked();

                    Toast.makeText(this, beanMessage, Toast.LENGTH_SHORT).show();

                    final String postKeyGenerated = FirebaseUtil.getUserPostRef().push().getKey();

                    List<String> items = new ArrayList<String>(Arrays.asList(beanTagArray.trim().split("\\s*,+\\s*,*\\s*"))); // Suppose to remove empty elements too
//                    List<String> items = new ArrayList<String>(Arrays.asList(beanTagArray.trim().split("\\s*,\\s*")));
                    ArrayList<String> list = new ArrayList<>(items);
                    final CustomUser user = FirebaseUtil.getCurrentUser();

                    Beans beans = new Beans(user, expressionDropdown.getSelectedItemPosition(), beanMessage, ServerValue.TIMESTAMP, list, postIsPublic);

                    Map<String, Object> beanMap = new HashMap<>();

                    String beanPostPath = "post_private_user/" + user.getUserid() + "/" + postKeyGenerated;

                    beanMap.put(beanPostPath, beans);

                    for (String tag : list) {

                        if (tag.length() > 0) {
                            TagName tagName = new TagName(tag);

                            String taggedPosts = "posts_with_tag_name_list/" + user.getUserid() + "/" + tag + "/" + postKeyGenerated;
                            beanMap.put(taggedPosts, true);

//                        String tagKeyGenerated = FirebaseUtil.getTagsBeanReference().push().getKey();
                            String tagPostPath = "post_tags_list/" + user.getUserid() + "/" + tag;
                            beanMap.put(tagPostPath, tagName);
                        }
                    }

                    if (postIsPublic) {
                        beanMap.put("post_public_all/" + postKeyGenerated, beans);
                    }

                    FirebaseUtil.getBaseRef().updateChildren(beanMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                final DatabaseReference moodRef = FirebaseUtil.getMoodCounterReference().child(user.getUserid());
                                moodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(EmojiSelectUtil.emojiIntConvertToString(expressionDropdown.getSelectedItemPosition()))) {
                                            moodRef.child(EmojiSelectUtil.emojiIntConvertToString(expressionDropdown.getSelectedItemPosition())).child("valueCount").runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                    if (mutableData.getValue() == null) {
                                                        mutableData.setValue(1);
                                                    } else {
                                                        mutableData.setValue((Long) mutableData.getValue() + 1);
                                                    }
                                                    return Transaction.success(mutableData);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                                    if (databaseError != null) {
                                                        Timber.i("Firebase counter increment failed.");
                                                    } else {
                                                        Timber.i("Firebase counter increment succeeded.");
                                                    }
                                                }
                                            });

                                            Intent intent = new Intent(SubmitBeanActivity.this, MainWindowActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            MoodCount moodCount = new MoodCount(EmojiSelectUtil.emojiIntConvertToString(expressionDropdown.getSelectedItemPosition()));
                                            moodRef.child(EmojiSelectUtil.emojiIntConvertToString(expressionDropdown.getSelectedItemPosition())).setValue(moodCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(SubmitBeanActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SubmitBeanActivity.this, MainWindowActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                // Increment comment count up by one using a runTransaction
//
//                                final DatabaseReference moodReference = FirebaseUtil.getBaseRef().child("mood_type_counter_values").child(user.getUserid()).child(EmojiSelectUtil.emojiExpressionTextValue[expressionDropdown.getSelectedItemPosition()]);
//                                moodReference.runTransaction(new Transaction.Handler() {
//                                    @Override
//                                    public Transaction.Result doTransaction(MutableData mutableData) {
//                                        if (mutableData.getValue() == null) {
//                                            mutableData.setValue(1);
//                                        } else {
//                                            mutableData.setValue((Long) mutableData.getValue() + 1);
//                                        }
//                                        return Transaction.success(mutableData);
//                                    }
//
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                                        if (databaseError != null) {
//                                            Timber.i("Firebase counter increment failed.");
//                                        } else {
//                                            Timber.i("Firebase counter increment succeeded.");
//                                        }
//                                    }
//                                });
                            }
                        }
                    });

//                    FirebaseUtil.getUserPostRef().child(user.getUserid()).push().setValue(beans).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            Toast.makeText(SubmitBeanActivity.this, "Complete", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SubmitBeanActivity.this, MainWindowActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(SubmitBeanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnCanceledListener(new OnCanceledListener() {
//                                @Override
//                                public void onCanceled() {
//                                    Toast.makeText(SubmitBeanActivity.this, "Upload has been cancelled by user.", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                }

                break;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
