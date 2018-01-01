package com.htdwps.grateful;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Adapter.FeedbackSubmitDialogBox;
import com.htdwps.grateful.Fragment.JournalFragment;
import com.htdwps.grateful.Fragment.UserJournalFragment;
import com.htdwps.grateful.Fragment.UserPostFragment;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.GlideUtil;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private CircleImageView imageProfile;
    private TextView textName;
    private FloatingActionButton floatingActionButton;

    public static int navItemIndex = 0;
    private static final int REQUEST_INVITE = 0;

    // Tags used to attach the fragments
    private static final String TAG_PRIVATE_POST = "posts_private";
    private static final String TAG_PRIVATE_JOURNAL = "journal_private";
    private static final String TAG_PUBLIC_JOURNAL = "journal_public";
    private static final String TAG_INVITE = "invite";
    public static String CURRENT_TAG = TAG_PRIVATE_POST;
    private String[] activityTitles;

    private Handler mHandler;

    static final String API_URL = "http://api.forismatic.com/api/1.0/?";
    String api_method = "getQuote";     // Method of Api call;
    String api_format = "text";         // Format available xml, json, html, text;
    String api_lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);

        mHandler = new Handler();

        runInitializer();
        runLayout();
//        runQuoteRequest();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PRIVATE_POST;
            loadHomeFragment();
        }

    }

    // Load the navigation menu header, such as header background image, profile name, etc
    private void loadNavHeader() {
        textName.setText(String.format("%s %s", String.valueOf(getBaseContext().getResources().getString(R.string.navi_tv_welcome_user)), firebaseUser.getDisplayName()));
        GlideUtil.loadProfileIcon(firebaseUser.getPhotoUrl().toString(), imageProfile);
    }

    public void runLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.mainfeed_navigation_view_layout);
        floatingActionButton = findViewById(R.id.btn_floating_action);
        floatingActionButton.setOnClickListener(this);

        activityTitles = getResources().getStringArray(R.array.navigation_labels);

        // Navigation Drawer View's Header Section
        View viewNavHeader = navigationView.getHeaderView(0);
        textName = viewNavHeader.findViewById(R.id.navigation_tv_user_displayname);
        imageProfile = viewNavHeader.findViewById(R.id.navigation_iv_user_photo);

        // Load the navigation menu header
        loadNavHeader();

        // Initializing navigation menu
        setUpNavigationView();
    }

    public void runInitializer() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Pass param of DatabaseReference to use the same Fragment but swap the database each time
                return UserPostFragment.newInstance(FirebaseUtil.getAllPostRef());
            case 1:
                return UserJournalFragment.newInstance(FirebaseUtil.getJournalListRef());
            case 2:
                return JournalFragment.newInstance(FirebaseUtil.getAllJournalRef());
            default:
                return new UserPostFragment();
        }
    }

    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            toggleContentButton();
            return;
        }

        if (navItemIndex < 3) {
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = getHomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

            mHandler.post(mPendingRunnable);

        }

        toggleContentButton();

        // Close the navigation drawer when item is pressed.
        drawerLayout.closeDrawers();

        // Refresh the toolbar menu
        invalidateOptionsMenu();

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setToolbarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        }
    }

    private void signOffUser() {
        FirebaseAuth.getInstance().signOut();

        Intent mainIntent = new Intent(ListActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void createTopic() {
        Intent discussionIntent = new Intent(ListActivity.this, SubmitActivity.class);
        startActivity(discussionIntent);
    }

    public void sendInvitationWindow() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getBaseContext().getString(R.string.invite_title_label))
                .setMessage(getBaseContext().getString(R.string.invite_message_label))
                .setDeepLink(Uri.parse("/link"))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
        // TODO: On completion of task or cancel of task send back to loadHomeFragment with navItemIndex of 0
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu_personal_posts:

                        CURRENT_TAG = TAG_PRIVATE_POST;
                        navItemIndex = 0;

                        break;
                    case R.id.navigation_menu_personal_journal:

                        CURRENT_TAG = TAG_PRIVATE_JOURNAL;
                        navItemIndex = 1;

                        break;
//                    case R.id.navigation_menu_public_posts:
//
//                        CURRENT_TAG = TAG_PUBLIC_JOURNAL;
//                        navItemIndex = 2;
//
//                        break;

                    case R.id.navigation_menu_public_journal:

                        CURRENT_TAG = TAG_PUBLIC_JOURNAL;
                        navItemIndex = 2;

                        break;

                    case R.id.navigation_menu_invite_friends:

                        CURRENT_TAG = TAG_INVITE;
                        navItemIndex = 3;

                        sendInvitationWindow();

                        break;

                    case R.id.navigation_menu_log_off:

                        signOffUser();

                        return true;

                    default:
                        navItemIndex = 0;
                }

                drawerLayout.closeDrawers();
                toggleContentButton();
                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

    }

    private void toggleContentButton() {
        if (navItemIndex != 3) {
            floatingActionButton.show();
        } else {
            floatingActionButton.hide();
        }
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
            case R.id.settings_menu_feedback_link:

                FeedbackSubmitDialogBox feedbackSubmitDialogBox = new FeedbackSubmitDialogBox(this);
                feedbackSubmitDialogBox.cloneInContext(this);

                break;

            case R.id.settings_menu_terms_rules_link:

                break;

            case R.id.settings_menu_privacy_policy_link:

//                startActivity(new Intent(this, PrivacyActivity.class));

                break;

            case R.id.settings_menu_logout_link:

                signOffUser();

                break;

            default:

                Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_floating_action:

                createTopic();

                break;

            default:

                Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();

                break;
        }
    }
}

