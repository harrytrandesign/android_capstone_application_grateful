package com.htdwps.grateful;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.htdwps.grateful.Fragment.MoodCounterFragment;
import com.htdwps.grateful.Fragment.PrivateBeansFragment;
import com.htdwps.grateful.Fragment.TagsCounterFragment;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Util.FirebaseHelper;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.MaterialHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MainWindowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_INVITE = 0;
    private static final String ALL_POSTS_PARAM = "public_posts";

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CustomUser user;

    private FloatingActionButton floatingActionButton;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    fragment = PrivateBeansFragment.newInstance(ALL_POSTS_PARAM, ALL_POSTS_PARAM);
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    fragment = MoodCounterFragment.newInstance();
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    visitMainActivity();
                    fragment = TagsCounterFragment.newInstance();
                    fragmentTransaction.replace(R.id.main_frame_layout, fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    return true;
            }
            return false;
        }
    };

    public void visitMainActivity() {
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        setTitle(R.string.app_tag_line);

        floatingActionButton = findViewById(R.id.btn_add_bean_floating_action);
        floatingActionButton.setOnClickListener(this);

        runInitializer();

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment = PrivateBeansFragment.newInstance(ALL_POSTS_PARAM, ALL_POSTS_PARAM);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame_layout, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        MaterialHelperUtil.generateInspirationalQuote(this);

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

                MaterialHelperUtil.generateInspirationalQuote(this);

                break;

            case R.id.settings_menu_invite_friend:

                sendInvitationWindow();

                break;

            case R.id.settings_menu_feedback_link:

                MaterialHelperUtil.submitFeedbackToDeveloper(this, user);

//                FeedbackSubmitDialogBox feedbackSubmitDialogBox = new FeedbackSubmitDialogBox(this);
//                feedbackSubmitDialogBox.cloneInContext(this);

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

                Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();

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
        Timber.i("Firebase is being initialized, firebase is complete.");
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
