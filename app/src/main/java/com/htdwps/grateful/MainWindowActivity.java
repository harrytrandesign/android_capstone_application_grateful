package com.htdwps.grateful;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htdwps.grateful.Fragment.MoodCounterFragment;
import com.htdwps.grateful.Fragment.PrivateBeansFragment;
import com.htdwps.grateful.Fragment.TagsCounterFragment;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.Util.FirebaseHelper;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.MaterialDialogHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_INVITE = 0;
    private static final String SAVE_INSTANCE_PARAM = "public_feed_status";
    public static final String PUBLIC_PARAM = "public_feed";
    public static final String PRIVATE_PARAM = "private_feed";

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    UserProfile user;

    private FloatingActionButton floatingActionButton;
    private Switch toggleSwitchFeedValue;

    private TextView tvTogglePublicPrivateFeed;

    boolean showPublicFeed = false;
    SharedPreferences sharedPreferences;
    Boolean quoteShowingPreference;


    // TODO Redo the fragment to condense into a single method
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_grateful:

                    enableToggleSwitch(true);

                    if (showPublicFeed) {
                        fragment = PrivateBeansFragment.newInstance(PUBLIC_PARAM);
                    } else {
                        fragment = PrivateBeansFragment.newInstance(PRIVATE_PARAM);
                    }
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    return true;

                case R.id.navigation_mood_count:

                    enableToggleSwitch(false);

                    fragment = MoodCounterFragment.newInstance();
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    return true;

                case R.id.navigation_tag_list:

                    enableToggleSwitch(false);

                    fragment = TagsCounterFragment.newInstance();
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    return true;

            }
            return false;
        }
    };

    public void enableToggleSwitch(boolean toggle) {
        if (toggle) {
            toggleSwitchFeedValue.setEnabled(true);
            toggleSwitchFeedValue.setClickable(true);
        } else {
            toggleSwitchFeedValue.setEnabled(false);
            toggleSwitchFeedValue.setClickable(false);
        }
    }

    public void visitMainActivity() {
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        setTitle(R.string.app_tag_line);

        displayingQuotesSetting();

        tvTogglePublicPrivateFeed = findViewById(R.id.tv_public_private_display_text);
        floatingActionButton = findViewById(R.id.btn_add_bean_floating_action);
        floatingActionButton.setOnClickListener(this);
        toggleSwitchFeedValue = findViewById(R.id.switch_toggle_public_private_feed);
        toggleSwitchFeedValue.setChecked(showPublicFeed);
        toggleSwitchFeedValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                togglePublicPrivatePosts(b);
            }
        });

        runInitializer();

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_grateful);

        Fragment fragment;
        if (showPublicFeed) {
            fragment = PrivateBeansFragment.newInstance(PUBLIC_PARAM);
        } else {
            fragment = PrivateBeansFragment.newInstance(PRIVATE_PARAM);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        if (quoteShowingPreference) MaterialDialogHelperUtil.generateInspirationalQuote(this);

    }

    public void refreshFeedFragment(boolean value) {

        Fragment fragment;

        if (value) {
            fragment = PrivateBeansFragment.newInstance(PUBLIC_PARAM);
        } else {
            fragment = PrivateBeansFragment.newInstance(PRIVATE_PARAM);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void togglePublicPrivatePosts(boolean feed) {

        showPublicFeed = feed;

        if (feed) {

            tvTogglePublicPrivateFeed.setText(getResources().getString(R.string.switch_private_text_label));

        } else {

            tvTogglePublicPrivateFeed.setText(getResources().getString(R.string.switch_public_text_label));

        }

        refreshFeedFragment(showPublicFeed);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVE_INSTANCE_PARAM, showPublicFeed);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean restoreState = savedInstanceState.getBoolean(SAVE_INSTANCE_PARAM);
        toggleSwitchFeedValue.setChecked(restoreState);
        togglePublicPrivatePosts(restoreState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings_menu_new_quote:

                MaterialDialogHelperUtil.generateInspirationalQuote(this);

                break;

            case R.id.settings_menu_setting_link:

                Intent settingIntent = new Intent(MainWindowActivity.this, SettingsActivity.class);
                startActivity(settingIntent);

                break;

            case R.id.settings_menu_invite_friend:

                sendInvitationWindow();

                break;

            case R.id.settings_menu_feedback_link:

                MaterialDialogHelperUtil.submitFeedbackToDeveloper(this, user);

                break;

            case R.id.settings_menu_terms_rules_link:

                Intent termsIntent = new Intent(MainWindowActivity.this, PrivacyTermsActivity.class);
                termsIntent.putExtra(StringConstantsUtil.STATEMENT_TYPE, StringConstantsUtil.TERMS_LABEL);
                startActivity(termsIntent);

                break;

            case R.id.settings_menu_privacy_policy_link:

                Intent privacyIntent = new Intent(MainWindowActivity.this, PrivacyTermsActivity.class);
                privacyIntent.putExtra(StringConstantsUtil.STATEMENT_TYPE, StringConstantsUtil.PRIVACY_LABEL);
                startActivity(privacyIntent);

                break;

            case R.id.settings_menu_logout_link:

                FirebaseHelper.signOffUser(MainWindowActivity.this);
                finish();

                break;

            default:

                Timber.d("Nothing pressed");

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void runInitializer() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        user = FirebaseUtil.getCurrentUser();
        Timber.d("Firebase is being initialized, firebase is complete.");
    }

    public void displayingQuotesSetting() {
        // Get any settings from PreferenceFragment first such as anonymous posting by default
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        quoteShowingPreference = sharedPreferences.getBoolean(getResources().getString(R.string.setting_quotes_settings_key), getResources().getBoolean(R.bool.quote_enable_default_setting_true));
    }

    public void sendInvitationWindow() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getBaseContext().getString(R.string.invite_title_label))
                .setMessage(getBaseContext().getString(R.string.invite_message_label))
                .setDeepLink(Uri.parse("/link"))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
        // TODO: On completion of task or cancel of task send back to loadHomeFragment with navItemIndex of 0
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_add_bean_floating_action:

                Intent submitBeanIntent = new Intent(MainWindowActivity.this, SubmitBeanActivity.class);
                startActivity(submitBeanIntent);

                break;

            default:

                Toast.makeText(this, "Error. Please Try Again.", Toast.LENGTH_SHORT).show();

                break;


        }

    }
}
