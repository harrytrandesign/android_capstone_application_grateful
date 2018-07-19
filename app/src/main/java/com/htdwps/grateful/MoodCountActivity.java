package com.htdwps.grateful;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.GeneralActivityHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.BeanLayoutViewHolder;

public class MoodCountActivity extends AppCompatActivity {

    private DatabaseReference moodListPostsDirectoryReference;
    private DatabaseReference userPostsMatchingPostDirectoryReference;

    private RecyclerView.Adapter<BeanLayoutViewHolder> adapterMoodPostList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerViewMoodPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_count);

        setupFirebaseInitialize();
        setupLayoutView();

        if (getIntent().getExtras() != null) {

            String moodNameReceived = getIntent().getExtras().getString(StringConstantsUtil.MOOD_TYPE_KEY_PARAM);
            String userId = firebaseUser.getUid();
            int moodIntValue = EmojiSelectUtil.emojiStringConvertToInt(moodNameReceived);

            setTitle("All \"" + EmojiSelectUtil.emojiIntConvertToString(moodIntValue) + "\" Posts");

            connectToDatabaseReferenceLinks(userId, moodIntValue);

            recyclerViewMoodPostList.setAdapter(createMoodListRecyclerViewAdapter(moodListPostsDirectoryReference));

        } else {

            Toast.makeText(this, "Error connecting to server", Toast.LENGTH_SHORT).show();

            finish();

        }
    }

    private void connectToDatabaseReferenceLinks(String userId, int moodIntValue) {

        // This gets the mood post key true values
        moodListPostsDirectoryReference = DatabaseReferenceHelperUtil.getMoodPostBooleanDirectoryRef(userId, String.valueOf(moodIntValue)); // moodListPostsDirectoryReference = FirebaseUtil.getMoodBeanListBooleanDirectoryReference().child(firebaseUser.getUid()).child(String.valueOf(moodIntValue));

        // This one tracks the userProfile's personal beans for post data
        userPostsMatchingPostDirectoryReference = DatabaseReferenceHelperUtil.getUserPostsMatchingThisUserIdDirectoryRef(userId); // FirebaseUtil.getPrivateUserBeanPostDirectoryReference().child(firebaseUser.getUid());

    }

    private void setupLayoutView() {

        ActionBar actionBar = MoodCountActivity.this.getSupportActionBar();

        GeneralActivityHelperUtil.backButtonReturnToParentArrowSetup(actionBar);

        recyclerViewMoodPostList = findViewById(R.id.rv_mood_all_posts_by_user);
        recyclerViewMoodPostList.setLayoutManager(GeneralActivityHelperUtil.createVerticalLinearLayout(this, LinearLayout.VERTICAL, true, true));

    }

    private void setupFirebaseInitialize() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    private RecyclerView.Adapter<BeanLayoutViewHolder> createMoodListRecyclerViewAdapter(DatabaseReference databaseReference) {

        adapterMoodPostList = new FirebaseRecyclerAdapter<Boolean, BeanLayoutViewHolder>(
                Boolean.class,
                R.layout.item_bean_user_single_post,
                BeanLayoutViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(final BeanLayoutViewHolder viewHolder, Boolean model, int position) {

                final String postKey = this.getRef(position).getKey();

                userPostsMatchingPostDirectoryReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        BeanPosts beanPost = dataSnapshot.getValue(BeanPosts.class);

                        if (beanPost != null) {
                            String year = DateUtils.formatDateTime(MoodCountActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                            String time = DateUtils.formatDateTime(MoodCountActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                            String dateTime = String.format("%s %s", year, time);

                            viewHolder.setBeanPostFields(beanPost.getMoodValue(), dateTime, beanPost.getBeanText(), beanPost.getTagList(), beanPost.isPublic(), beanPost.getUserProfile().getUserDisplayName(), false);
//                            Timber.d("This message's value is " + String.valueOf(beanPost.getMoodValue()));
//                            Timber.d("This message's message is " + beanPost.getBeanText());

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        return adapterMoodPostList;
    }

}
