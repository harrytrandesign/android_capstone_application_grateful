package com.htdwps.grateful;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htdwps.grateful.Fragment.MoodCounterFragment;
import com.htdwps.grateful.Fragment.BeanFeedListFragment;
import com.htdwps.grateful.Fragment.TagsCounterFragment;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.Util.FirebaseHelper;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.GeneralActivityHelperUtil;
import com.htdwps.grateful.Util.MaterialDialogHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean showPublicFeed = false;
    private Boolean quoteShowingPreference;

    private UserProfile userProfile;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private FloatingActionButton fabSwitchToSubmitActivity;
    private Switch switchToggleFeedPrivacySettingValue;

    private TextView tvTogglePublicPrivateFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        setTitle(R.string.app_main_feed_title);

        displayingQuotesSetting();
        displayInspirationQuoteOnLoadComplete(this, quoteShowingPreference);

        initializeViews();

        runInitializer();

        refreshFeedFragment(showPublicFeed);

    }

    private void initializeViews() {

        tvTogglePublicPrivateFeed = findViewById(R.id.tv_public_private_display_text);

        fabSwitchToSubmitActivity = findViewById(R.id.fab_switch_activity_submit_bean);
        fabSwitchToSubmitActivity.setOnClickListener(this);

        switchToggleFeedPrivacySettingValue = findViewById(R.id.switch_toggle_public_private_feed);
        switchToggleFeedPrivacySettingValue.setChecked(showPublicFeed);
        switchToggleFeedPrivacySettingValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                togglePublicPrivatePosts(b);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_grateful);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment replacementFragment;

            switch (item.getItemId()) {

                case R.id.navigation_grateful:

                    enableToggleSwitch(true);

                    if (showPublicFeed) {

                        replacementFragment = BeanFeedListFragment.newInstance(StringConstantsUtil.PUBLIC_PARAM);

                    } else {

                        replacementFragment = BeanFeedListFragment.newInstance(StringConstantsUtil.PRIVATE_PARAM);

                    }

                    replaceFragmentLoaderCall(replacementFragment);

                    return true;

                case R.id.navigation_mood_count:

                    enableToggleSwitch(false);

                    replacementFragment = MoodCounterFragment.newInstance();

                    replaceFragmentLoaderCall(replacementFragment);

                    return true;

                case R.id.navigation_tag_list:

                    enableToggleSwitch(false);

                    replacementFragment = TagsCounterFragment.newInstance();

                    replaceFragmentLoaderCall(replacementFragment);

                    return true;

            }

            return false;

        }
    };

    public void refreshFeedFragment(boolean value) {

        Fragment newFragment;

        if (value) {

            newFragment = BeanFeedListFragment.newInstance(StringConstantsUtil.PUBLIC_PARAM);

        } else {

            newFragment = BeanFeedListFragment.newInstance(StringConstantsUtil.PRIVATE_PARAM);

        }

        replaceFragmentLoaderCall(newFragment);

    }

    public void replaceFragmentLoaderCall(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void enableToggleSwitch(boolean toggle) {

        if (toggle) {

            switchToggleFeedPrivacySettingValue.setEnabled(true);
            switchToggleFeedPrivacySettingValue.setClickable(true);

        } else {

            switchToggleFeedPrivacySettingValue.setEnabled(false);
            switchToggleFeedPrivacySettingValue.setClickable(false);

        }
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

        outState.putBoolean(StringConstantsUtil.SAVE_INSTANCE_PARAM, showPublicFeed);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean restoreState = savedInstanceState.getBoolean(StringConstantsUtil.SAVE_INSTANCE_PARAM);

        switchToggleFeedPrivacySettingValue.setChecked(restoreState);
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

                displayInspirationQuoteOnLoadComplete(this,true);

                break;

            case R.id.settings_menu_setting_link:

                Intent settingIntent = new Intent(MainWindowActivity.this, SettingsActivity.class);
                startActivity(settingIntent);

                break;

            case R.id.settings_menu_invite_friend:

                startActivityForResult(GeneralActivityHelperUtil.sendInvitationWindow(this), StringConstantsUtil.REQUEST_INVITE);

                break;

            case R.id.settings_menu_feedback_link:

                MaterialDialogHelperUtil.submitFeedbackToDeveloper(this, userProfile);

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

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userProfile = FirebaseUtil.getCurrentUser();
        Timber.d("Firebase is being initialized, firebase is complete.");

    }

    public void displayingQuotesSetting() {

        // Get any settings from PreferenceFragment first such as anonymous posting by default
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        quoteShowingPreference = sharedPreferences.getBoolean(getResources().getString(R.string.setting_quotes_settings_key), getResources().getBoolean(R.bool.quote_enable_default_setting_true));

    }

    private void displayInspirationQuoteOnLoadComplete(Context context, boolean is_quote_display) {

        if (is_quote_display) MaterialDialogHelperUtil.generateInspirationalQuote(context);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fab_switch_activity_submit_bean:

                Intent submitBeanIntent = new Intent(MainWindowActivity.this, SubmitBeanActivity.class);
                startActivity(submitBeanIntent);

                break;

            default:

                Toast.makeText(this, R.string.toast_error_try_again, Toast.LENGTH_SHORT).show();

                break;


        }

    }
}
