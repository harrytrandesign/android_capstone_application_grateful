package com.htdwps.grateful;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Model.MoodCount;
import com.htdwps.grateful.Model.TagName;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

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

    SharedPreferences sharedPreferences;
    Boolean postPublicSettingDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bean);
        setTitle("New Post");

        mAdview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
//        MobileAds.initialize(this, SubmitBeanActivity.this.getResources().getString(R.string.admob_app_id));

        searchUserDefaults();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        emojiExpressionAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, EmojiSelectUtil.emojiForSpinnerDropdown);
        CustomSpinnerArrayAdapter customSpinnerArrayAdapter = new CustomSpinnerArrayAdapter(this, R.layout.spinner_picker_layout, EmojiSelectUtil.emojiForSpinnerDropdown);
        emojiList = EmojiSelectUtil.emojiForSpinnerDropdown;
        emotionList = EmojiSelectUtil.emojiExpressionTextValue;

        editText = findViewById(R.id.et_beans_message_textbox);
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        tagText = findViewById(R.id.et_beans_extra_taglist);
        checkBox = findViewById(R.id.checkbox_public_box);
//        expressionTextLabel = findViewById(R.id.tv_mood_expression_text);
        expressionDropdown = findViewById(R.id.spinner_emoji_expression_moods_dropdown);

        checkBox.setChecked(postPublicSettingDefault);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean isPublic = b;
                if (b) {
                    postPublicSettingDefault = getResources().getBoolean(R.bool.publish_public_setting_true);
                    isPublic = b;
                } else {
                    postPublicSettingDefault = getResources().getBoolean(R.bool.publish_private_default_setting_false);
                    isPublic = b;
                }
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

    public void searchUserDefaults() {
        // Get any settings from PreferenceFragment first such as anonymous posting by default
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        postPublicSettingDefault = sharedPreferences.getBoolean(getResources().getString(R.string.setting_option_publishing_settings_key), getResources().getBoolean(R.bool.publish_private_default_setting_false));
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

//                Toast.makeText(this, "Submit this data to the database, on complete go back to the previous activity.", Toast.LENGTH_SHORT).show();

                String beanMessage = editText.getText().toString();
                String beanTagArray = tagText.getText().toString();

                if (TextUtils.isEmpty(beanMessage) || beanMessage.length() < 10) {

                    editText.isFocused();
                    editText.setError("Sorry, your message is too short");

                } else {

                    final String postKeyGenerated = FirebaseUtil.getPrivateUserBeanPostDirectoryReference().push().getKey();

                    final UserProfile user = FirebaseUtil.getCurrentUser();

                    List<String> items = new ArrayList<String>(Arrays.asList(beanTagArray.trim().split("\\s*,+\\s*,*\\s*"))); // Suppose to remove empty elements too // List<String> items = new ArrayList<String>(Arrays.asList(beanTagArray.trim().split("\\s*,\\s*")));

                    ArrayList<String> list = new ArrayList<>(items);

                    boolean postIsPublic = checkBox.isChecked();

                    final String userId = user.getUserid();

                    final int expressionSelected = expressionDropdown.getSelectedItemPosition();

                    Map<String, Object> generateNewBeanPostForUser = new HashMap<>();

                    BeanPosts beanPosts = new BeanPosts(user, expressionSelected, beanMessage, ServerValue.TIMESTAMP, list, postIsPublic, postKeyGenerated);

                    String beanPostPath = StringConstantsUtil.createPostForUserDirectoryPath(userId, postKeyGenerated); //"personal_beans_list/" + userProfile.getUserid() + "/" + postKeyGenerated;

                    generateNewBeanPostForUser.put(beanPostPath, beanPosts);

                    // Keep track of the tags the userProfile is using
                    for (String tag : list) {

                        if (tag.length() > 0) {

                            TagName tagName = new TagName(tag);

                            String postsTaggedWithTagNamePath = StringConstantsUtil.POST_EXISTS_TAG_NAME_BOOLEAN_PATH + "/" + userId + "/" + tag + "/" + postKeyGenerated;
                            String tagNameThatIsUsedPath = StringConstantsUtil.TAG_NAME_USED_PATH + "/" + userId + "/" + tag;

                            generateNewBeanPostForUser.put(postsTaggedWithTagNamePath, true);
                            generateNewBeanPostForUser.put(tagNameThatIsUsedPath, tagName);

                        }
                    }

                    // Keep track of mood based posts, moodname -> pushkey:true;
                    String postsUsingMoodTypePath = StringConstantsUtil.POST_EXISTS_MOOD_TYPE_BOOLEAN_PATH + "/" + userId + "/" + expressionSelected + "/" + postKeyGenerated;
                    generateNewBeanPostForUser.put(postsUsingMoodTypePath, true);

                    if (postIsPublic) {
                        generateNewBeanPostForUser.put(StringConstantsUtil.PUBLICLY_SHARED_BEANS_PATH + "/" + postKeyGenerated, beanPosts);
                    }

                    FirebaseUtil.getBaseRef().updateChildren(generateNewBeanPostForUser, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                // Increment the count for each mood that is uploaded
                                final DatabaseReference moodCountDirectoryReference = FirebaseUtil.getMoodCounterDirectoryReference().child(userId);
                                moodCountDirectoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(EmojiSelectUtil.emojiIntConvertToString(expressionSelected))) {
                                            moodCountDirectoryReference.child(EmojiSelectUtil.emojiIntConvertToString(expressionSelected)).child(StringConstantsUtil.MOOD_INCREMENT_VALUE_COUNT).runTransaction(new Transaction.Handler() {
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
                                            moodCountDirectoryReference.child(EmojiSelectUtil.emojiIntConvertToString(expressionDropdown.getSelectedItemPosition())).setValue(moodCount).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            }
                        }
                    });

                }

                break;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
