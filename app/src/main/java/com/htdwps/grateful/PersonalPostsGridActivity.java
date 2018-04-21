package com.htdwps.grateful;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.htdwps.grateful.Fragment.PersonProfileFragment;

public class PersonalPostsGridActivity extends AppCompatActivity {

    private static final String USER_PROFILE_KEY = "user_profile_id_key";
    private static final String USER_DISPLAY_NAME = "user_displayname";
    private static final String USER_PICTURE = "user_photo_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_posts_grid);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        Toolbar toolbar = findViewById(R.id.toolbar_grid);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(Color.WHITE);

        String userReferenceKey = getIntent().getStringExtra(USER_PROFILE_KEY);
        String userDisplayName = getIntent().getStringExtra(USER_DISPLAY_NAME);
        String userPhotoStringUrl = getIntent().getStringExtra(USER_PICTURE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame_layout_grid_activity, PersonProfileFragment.newInstance(userReferenceKey, userDisplayName, userPhotoStringUrl)).commit();

    }
}
