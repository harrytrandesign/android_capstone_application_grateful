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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
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
import com.htdwps.grateful.Util.DatabaseReferenceHelperUtil;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class SubmitBeanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private Boolean postPublicSettingDefault;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ArrayAdapter<String> adapterEmojiExpressionsForSpinner;
    private CustomSpinnerArrayAdapter customSpinnerArrayAdapter;
    private String[] emojiList;
    private String[] emotionList;

    private AdView mAdview;
    private CheckBox checkBoxSharePostPublicly;
    private Spinner spinnerEmojiExpressionDropdown;
    private EditText etMainMessageTextWindow;
    private EditText etTagForPostTextWindow;

    private int moodExpressionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bean);
        setTitle("Start A New Post");

//        MobileAds.initialize(this, SubmitBeanActivity.this.getResources().getString(R.string.admob_app_id));

        runInitializeOnFirebase();

        searchUserDefaults();

        setupLayoutViews();

        // Testing a custom spinner adapter and layout
        spinnerEmojiExpressionDropdown.setAdapter(getCustomSpinnerArrayAdapter());

    }

    private void generateDataStructureLists() {

        adapterEmojiExpressionsForSpinner = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, EmojiSelectUtil.emojiForSpinnerDropdown);
        emojiList = EmojiSelectUtil.emojiForSpinnerDropdown;
        emotionList = EmojiSelectUtil.emojiExpressionTextValue;

    }

    @NonNull
    private CustomSpinnerArrayAdapter getCustomSpinnerArrayAdapter() {

        return new CustomSpinnerArrayAdapter(this, R.layout.spinner_picker_layout, EmojiSelectUtil.emojiForSpinnerDropdown);

    }

    private void setupLayoutViews() {

        // Request an Ad - Admob key stored in build properties file
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview = findViewById(R.id.adView);
        mAdview.setAdSize(AdSize.SMART_BANNER);
        mAdview.setAdUnitId(BuildConfig.ADMOB_BANNER_ID_KEY);
        mAdview.loadAd(adRequest);

        spinnerEmojiExpressionDropdown = findViewById(R.id.spinner_emoji_expression_moods_dropdown);
        spinnerEmojiExpressionDropdown.setOnItemSelectedListener(this);

        etMainMessageTextWindow = findViewById(R.id.et_beans_message_textbox);
        etMainMessageTextWindow.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etMainMessageTextWindow.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        etTagForPostTextWindow = findViewById(R.id.et_tags_by_comma_textbox);

        checkBoxSharePostPublicly = findViewById(R.id.checkbox_public_box);
        checkBoxSharePostPublicly.setChecked(postPublicSettingDefault);
        checkBoxSharePostPublicly.setOnCheckedChangeListener(this);

    }

    private void runInitializeOnFirebase() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            generateDataStructureLists();
        } else {
            Intent notSignedOnIntent = new Intent(this, FirebaseUiAuthActivity.class);
            startActivity(notSignedOnIntent);
            finish();
        }

    }

    public void searchUserDefaults() {

        // Get any settings from PreferenceFragment first such as anonymous posting by default
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        postPublicSettingDefault = sharedPreferences.getBoolean(getResources().getString(R.string.setting_option_publishing_settings_key), getResources().getBoolean(R.bool.publish_private_default_setting_false));

    }

    /**
     *
     * @param user          UserProfile data from the logged user including their displayname, id, and email
     * @param beanMessage   The text message that is used in the post submit
     * @param beanTagArray  String of tags that are seperated by commas to be trimmed into a list
     */
    private void submitNewPostToDatabase(@NonNull UserProfile user, @NonNull String beanMessage, @NonNull String beanTagArray) {

        if (TextUtils.isEmpty(beanMessage) || beanMessage.length() < 10) {

            etMainMessageTextWindow.isFocused();
            etMainMessageTextWindow.setError("Sorry, your message is too short");

        } else {

            // Create the random key values and the list data structures
            final String postKeyGenerated = FirebaseUtil.getPrivateUserBeanPostDirectoryReference().push().getKey();

//            final UserProfile user = FirebaseUtil.getCurrentUser();

            List<String> items = new ArrayList<>(Arrays.asList(beanTagArray.trim().split("\\s*,+\\s*,*\\s*"))); // Suppose to remove empty elements too // List<String> items = new ArrayList<String>(Arrays.asList(beanTagArray.trim().split("\\s*,\\s*")));

            ArrayList<String> list = new ArrayList<>(items);

            boolean postIsPublic = checkBoxSharePostPublicly.isChecked();

            final String userId = user.getUserid();

            final int expressionSelected = spinnerEmojiExpressionDropdown.getSelectedItemPosition();

            // Create the BeanPost POJO Object to submit to database
            BeanPosts beanPosts = new BeanPosts(user, expressionSelected, beanMessage, ServerValue.TIMESTAMP, list, postIsPublic, postKeyGenerated);

            // Start a Map for a new post to insert data into it
            Map<String, Object> generateNewBeanPostForUser = new HashMap<>();

            generateNewBeanPostForUser.put(StringConstantsUtil.createPostForUserDirectoryPath(userId, postKeyGenerated), beanPosts);

            // Keep track of the tags the userProfile is using
            for (String tag : list) {

                if (tag.length() > 0) {

                    TagName tagName = new TagName(tag);

                    // public void tagInsertIntoMap(Map mapName, String userId, String tagName, String postKey);
                    generateNewBeanPostForUser.put(StringConstantsUtil.createTagsPostsUsedBooleanDirectoryPath(userId, tag, postKeyGenerated), true);
                    generateNewBeanPostForUser.put(StringConstantsUtil.createTagsNamesForTagRvListDirectoryPath(userId, tag), tagName);

                }
            }

            // Keep track of mood based posts, moodname -> pushkey:true;
//            String postsUsingMoodTypePath = StringConstantsUtil.POST_EXISTS_MOOD_TYPE_BOOLEAN_PATH + "/" + userId + "/" + expressionSelected + "/" + postKeyGenerated;
            generateNewBeanPostForUser.put(StringConstantsUtil.createMoodForPostsBooleanDirectoryPath(userId, expressionSelected, postKeyGenerated), true);

            if (postIsPublic) {
                generateNewBeanPostForUser.put(StringConstantsUtil.createPostForPublicViewDirectoryPath(postKeyGenerated), beanPosts);
//                generateNewBeanPostForUser.put(StringConstantsUtil.PUBLICLY_SHARED_BEANS_PATH + "/" + postKeyGenerated, beanPosts);
            }

            FirebaseUtil.getBaseRef().updateChildren(generateNewBeanPostForUser, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {

                        // Increment the count for each mood that is uploaded
                        final DatabaseReference moodCountDirectoryReference = DatabaseReferenceHelperUtil.getMoodCountDirectoryRef(userId);
//                        final DatabaseReference moodCountDirectoryReference = FirebaseUtil.getMoodCounterDirectoryReference().child(userId);
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

                                    String moodName = EmojiSelectUtil.emojiIntConvertToString(spinnerEmojiExpressionDropdown.getSelectedItemPosition());

                                    MoodCount moodCount = new MoodCount(moodName);

                                    moodCountDirectoryReference.child(moodName).setValue(moodCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(SubmitBeanActivity.this, R.string.toast_complete, Toast.LENGTH_SHORT).show();
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

                String beanMessage = etMainMessageTextWindow.getText().toString();
                String beanTagArray = etTagForPostTextWindow.getText().toString();

                final UserProfile user = FirebaseUtil.getCurrentUser();

                if (user != null) {
                    submitNewPostToDatabase(user, beanMessage, beanTagArray);
                }

                break;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        boolean isPublic = b;

        if (b) {

            postPublicSettingDefault = getResources().getBoolean(R.bool.publish_public_setting_true);
//            isPublic = b;

        } else {

            postPublicSettingDefault = getResources().getBoolean(R.bool.publish_private_default_setting_false);
//            isPublic = b;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        moodExpressionValue = i;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
