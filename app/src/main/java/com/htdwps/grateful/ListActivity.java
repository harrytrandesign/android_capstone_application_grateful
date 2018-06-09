package com.htdwps.grateful;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Fragment.UserPostFragment;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.MaterialHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CustomUser user;

    private static final String TERMS_LABEL = "TERMS";
    private static final String PRIVACY_LABEL = "PRIVACY";
    private static final String STATEMENT_TYPE = "StatementType";
    private static final String ALL_POSTS_PARAM = "public_posts";
    private static final String USER_POSTS_PARAM = "user_posts";

    private AdView mAdview;
    private CircleImageView imageProfile;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private NavigationView navigationView;
    private TextView textName;
    private Toolbar toolbar;
    //    private CollapsingToolbarLayout toolbar;
    private AppBarLayout appBarLayout;

    public static int navItemIndex = 0;
    private static final int REQUEST_INVITE = 0;

    // Tags used to attach the fragments
    private static final String TAG_PUBLIC_SUBMITS = "posts_by_all";
    private static final String TAG_PRIVATE_SUBMITS = "posts_by_user";
    private static final String TAG_PUBLIC_JOURNAL = "journal_public";
    private static final String TAG_INVITE = "invite";
    public static String CURRENT_TAG = TAG_PUBLIC_SUBMITS;
    private String[] activityTitles;
    ArrayAdapter<String> emojiExpressionAdapter;

//    private String[] emojiExpressionOptions = new String[]{
//            String.valueOf(Character.toChars(0x1F525)) + " " + EmojiSelectUtil.emojiExpressionTextValue[0],
//            String.valueOf(Character.toChars(0x1F603)) + " " + EmojiSelectUtil.emojiExpressionTextValue[1],
//            String.valueOf(Character.toChars(0x1F60D)) + " " + EmojiSelectUtil.emojiExpressionTextValue[2],
//            String.valueOf(Character.toChars(0x1F60E)) + " " + EmojiSelectUtil.emojiExpressionTextValue[3],
//            String.valueOf(Character.toChars(0x1F914)) + " " + EmojiSelectUtil.emojiExpressionTextValue[4],
//            String.valueOf(Character.toChars(0x1F623)) + " " + EmojiSelectUtil.emojiExpressionTextValue[5],
//            String.valueOf(Character.toChars(0x1F62A)) + " " + EmojiSelectUtil.emojiExpressionTextValue[6],
//            String.valueOf(Character.toChars(0x1F613)) + " " + EmojiSelectUtil.emojiExpressionTextValue[7],
//            String.valueOf(Character.toChars(0x1F61F)) + " " + EmojiSelectUtil.emojiExpressionTextValue[8],
//            String.valueOf(Character.toChars(0x1F620)) + " " + EmojiSelectUtil.emojiExpressionTextValue[9],
//            String.valueOf(Character.toChars(0x1F4A9)) + " " + EmojiSelectUtil.emojiExpressionTextValue[10],
//            String.valueOf(Character.toChars(0x1F4AF)) + " " + EmojiSelectUtil.emojiExpressionTextValue[11],
//            String.valueOf(Character.toChars(0x1F4B0)) + " " + EmojiSelectUtil.emojiExpressionTextValue[12],
//            String.valueOf(Character.toChars(0x1F47C)) + " " + EmojiSelectUtil.emojiExpressionTextValue[13]};

    private Handler mHandler;

    static final String API_URL = "http://api.forismatic.com/api/1.0/?";
    String api_method = "getQuote";     // Method of Api call;
    String api_format = "text";         // Format available xml, json, html, text;
    String api_lang = "en";
    MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);

        mHandler = new Handler();
        MobileAds.initialize(this, ListActivity.this.getResources().getString(R.string.admob_app_id));

        runInitializer();
        runLayout();
//        runQuoteRequest();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_PUBLIC_SUBMITS;
            loadHomeFragment();
        }

    }

    // Load the navigation menu header, such as header background image, profile name, etc
    private void loadNavHeader() {
        textName.setText(String.format("%s %s", String.valueOf(getBaseContext().getResources().getString(R.string.navi_tv_welcome_user)), firebaseUser.getDisplayName()));
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            if (firebaseUser.getPhotoUrl() != null) {
////                GlideUtil.loadProfileIcon(firebaseUser.getPhotoUrl().toString(), imageProfile);
//            }
//        }
    }

    public void runLayout() {
//        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
//        appBarLayout.setBackgroundColor(getResources().getColor(R.color.black_transparent));
//        appBarLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.black_transparent)));
//        toolbar.setBackgroundColor(getResources().getColor(R.color.black_transparent));
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_transparent)));
//        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_transparent)));

        // Advertisements banner top smart_banner size
        mAdview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.mainfeed_navigation_view_layout);
        floatingActionButton = findViewById(R.id.btn_floating_action);
        floatingActionButton.setOnClickListener(this);

        activityTitles = getResources().getStringArray(R.array.navigation_labels);
//        emojiExpressionOptions = getResources().getIntArray(R.array.emoji_spinner_dropdown_choices);
        emojiExpressionAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, EmojiSelectUtil.emojiForSpinnerDropdown);
//        emojiExpressionAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, emojiExpressionOptions);

        // Navigation Drawer View's Header Section
        View viewNavHeader = navigationView.getHeaderView(0);
        textName = viewNavHeader.findViewById(R.id.navigation_tv_user_displayname);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Remove the Circle image completely.
        } else {
            imageProfile = viewNavHeader.findViewById(R.id.navigation_iv_user_photo);
        }

        // Load the navigation menu header
        loadNavHeader();

        // Initializing navigation menu
        setUpNavigationView();

        // TODO: Run an internet connection check here before calling out this Quote.
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.tv_quote_inspire_daily)
                .content(QuoteActivity.runQuoteRequest())
                .positiveText(R.string.tv_close_quote);

        dialog = builder.build();
        dialog.show();
        // Stop internet check here.

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
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Pass param of DatabaseReference to use the same Fragment but swap the database each time
                return UserPostFragment.newInstance(ALL_POSTS_PARAM);
            case 1:
                return UserPostFragment.newInstance(ALL_POSTS_PARAM);
//            case 2:
//                return JournalFragment.newInstance(FirebaseUtil.getAllJournalRef());
            default:
                return UserPostFragment.newInstance(ALL_POSTS_PARAM);
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

        // Return the number found in the menu string array.
        if (navItemIndex < 2) {
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
        Intent discussionIntent = new Intent(ListActivity.this, SubmitActivityV2.class);
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
                    case R.id.navigation_menu_all_posts:

                        CURRENT_TAG = TAG_PUBLIC_SUBMITS;
                        navItemIndex = 0;

                        break;
                    case R.id.navigation_menu_personal_posts:

                        CURRENT_TAG = TAG_PRIVATE_SUBMITS;
                        navItemIndex = 1;

                        break;
//                    case R.id.navigation_menu_public_posts:
//
//                        CURRENT_TAG = TAG_PUBLIC_JOURNAL;
//                        navItemIndex = 2;
//
//                        break;

//                    case R.id.navigation_menu_public_journal:
//
//                        CURRENT_TAG = TAG_PUBLIC_JOURNAL;
//                        navItemIndex = 2;
//
//                        break;

//                    case R.id.navigation_menu_invite_friends:
//
//                        CURRENT_TAG = TAG_INVITE;
//                        navItemIndex = 1;
//
//                        sendInvitationWindow();
//
//                        break;

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
        if (navItemIndex != 2) {
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
            case R.id.settings_menu_invite_friend:

                sendInvitationWindow();

                break;

            case R.id.settings_menu_feedback_link:

                MaterialHelperUtil.submitFeedbackToDeveloper(this, user);

//                FeedbackSubmitDialogBox feedbackSubmitDialogBox = new FeedbackSubmitDialogBox(this);
//                feedbackSubmitDialogBox.cloneInContext(this);

                break;

            case R.id.settings_menu_terms_rules_link:

                Intent termsIntent = new Intent(ListActivity.this, PrivacyTermsActivity.class);
                termsIntent.putExtra(StringConstantsUtil.STATEMENT_TYPE, StringConstantsUtil.TERMS_LABEL);
                startActivity(termsIntent);

                break;

            case R.id.settings_menu_privacy_policy_link:

                Intent privacyIntent = new Intent(ListActivity.this, PrivacyTermsActivity.class);
                privacyIntent.putExtra(StringConstantsUtil.STATEMENT_TYPE, StringConstantsUtil.PRIVACY_LABEL);
                startActivity(privacyIntent);

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

//                CustomUser user = FirebaseUtil.getCurrentUser();
//                createTopic();
                MaterialHelperUtil.createMaterialDialogBeanCreator(this, view, emojiExpressionAdapter, EmojiSelectUtil.emojiForSpinnerDropdown, EmojiSelectUtil.emojiExpressionTextValue, user);

                break;

            default:

                Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
        }
    }
}

