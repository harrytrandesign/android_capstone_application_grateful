package com.htdwps.grateful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Util.DatabaseReferenceHelperUtil;
import com.htdwps.grateful.Util.GeneralActivityHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.BeanLayoutViewHolder;

import java.util.ArrayList;

import timber.log.Timber;

public class TagPostActivity extends AppCompatActivity {

    private DatabaseReference personalBeanListDirectoryReference;
    private DatabaseReference postsByTagsBooleanDirectoryReference;

    private RecyclerView.Adapter<BeanLayoutViewHolder> adapterAllPostWithSelectedTagName;

    private FirebaseUser firebaseUser;

    private RecyclerView recyclerViewAllPostWithSelectedTagName;

    private String tagTermSelected = "";
    private String userIdKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_post);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userIdKey = firebaseUser.getUid();

        if (getIntent().getExtras() != null) {

            tagTermSelected = getIntent().getExtras().getString(StringConstantsUtil.TAG_WORD_KEY_PARAM);

            initializeDatabaseReferenceLinks(userIdKey, tagTermSelected);

            setupLayout();

            recyclerViewAllPostWithSelectedTagName.setAdapter(createTagPostRecyclerViewAdapter(postsByTagsBooleanDirectoryReference));

//            Toast.makeText(this, "This tag activity is for all the posts with the following tag: " + tagTermSelected, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Couldn't get a message", Toast.LENGTH_SHORT).show();

            finish();

        }
    }

    private void initializeDatabaseReferenceLinks(String userId, String tagName) {

        personalBeanListDirectoryReference = DatabaseReferenceHelperUtil.getUserPostsMatchingThisUserIdDirectoryRef(userId);
        postsByTagsBooleanDirectoryReference = DatabaseReferenceHelperUtil.getTaggedPostBooleanDirectoryRef(userId, tagName);

    }

    private void setupLayout() {

        recyclerViewAllPostWithSelectedTagName = findViewById(R.id.rv_posts_with_tag);
        recyclerViewAllPostWithSelectedTagName.setLayoutManager(GeneralActivityHelperUtil.createVerticalLinearLayout(this, LinearLayout.VERTICAL, true, true));

    }

    public RecyclerView.Adapter<BeanLayoutViewHolder> createTagPostRecyclerViewAdapter(DatabaseReference databaseReference) {

        adapterAllPostWithSelectedTagName = new FirebaseRecyclerAdapter<Boolean, BeanLayoutViewHolder>(
                Boolean.class,
                R.layout.item_bean_user_single_post,
                BeanLayoutViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final BeanLayoutViewHolder viewHolder, Boolean model, int position) {

                final String thisPostKey = this.getRef(position).getKey();

                personalBeanListDirectoryReference.child(thisPostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BeanPosts beanPost = dataSnapshot.getValue(BeanPosts.class);

                        if (beanPost != null) {
                            String year = DateUtils.formatDateTime(TagPostActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                            String time = DateUtils.formatDateTime(TagPostActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                            String dateAndTime = String.format("%s %s", year, time);

                            // Grab all necessary data from the BeanPosts class
                            int mood = beanPost.getMoodValue();
                            String textMessage = beanPost.getBeanText();
                            ArrayList<String> listOfTags = beanPost.getTagList();
                            boolean isPublic = beanPost.isPublic();
                            String userDisplayName = beanPost.getUserProfile().getUserDisplayName();
                            boolean isOnPublicFeed = false;

                            viewHolder.setBeanPostFields(mood, dateAndTime, textMessage, listOfTags, isPublic, userDisplayName, isOnPublicFeed);
                            Timber.i("This message's value is " + String.valueOf(beanPost.getMoodValue()));
                            Timber.i("This message's message is " + beanPost.getBeanText());

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        };

        return adapterAllPostWithSelectedTagName;

    }

}
