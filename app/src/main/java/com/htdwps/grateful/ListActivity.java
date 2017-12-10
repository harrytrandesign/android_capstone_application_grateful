package com.htdwps.grateful;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.htdwps.grateful.Util.GlideUtil;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

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
    private static final String TAG_DISCUSSION = "discussion";
    private static final String TAG_PERSONAL = "personal";
    private static final String TAG_LIKED = "liked";
    private static final String TAG_INVITE = "invite";
    private static final String TAG_CATEGORY = "category";
    public static String CURRENT_TAG = TAG_DISCUSSION;
    private String[] activityTitles;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);

        mHandler = new Handler();

        runInitializer();
        runLayout();


    }

    // Load the navigation menu header, such as header background image, profile name, etc
    private void loadNavHeader() {
        textName.setText(firebaseUser.getDisplayName());
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

    private void signOffUser() {
        FirebaseAuth.getInstance().signOut();

        Intent mainIntent = new Intent(ListActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void createTopic() {
//        Intent discussionIntent = new Intent(ListActivity.this, SubmitPostActivity.class);
//        startActivity(discussionIntent);
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
                    case R.id.navigation_menu_general_posts:

                        CURRENT_TAG = TAG_DISCUSSION;
                        navItemIndex = 0;

                        break;
                    case R.id.navigation_menu_personal_posts:

                        CURRENT_TAG = TAG_PERSONAL;
                        navItemIndex = 1;

                        break;
                    case R.id.navigation_menu_comments_list:

                        CURRENT_TAG = TAG_LIKED;
                        navItemIndex = 2;

                        break;

                    default:
                        navItemIndex = 0;
                }

//                if (navItemIndex < 4) {
//                    selectNavMenu();
//                }
//
//                if (navItemIndex != 3) {
//                    setToolbarTitle();
//                    switchFragment();
//                }

                drawerLayout.closeDrawers();
                toggleContentButton();
//                loadHomeFragment();

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

//                Intent commentTestIntent = new Intent(FeedActivity.this, PostCommentActivity.class);
//                startActivity(commentTestIntent);

//                startActivity(new Intent(this, PostCommentActivity.class));

//                FeedbackDialogBoxOpen feedbackDialogBoxOpen = new FeedbackDialogBoxOpen(this);
//                feedbackDialogBoxOpen.cloneInContext(this);

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

