package com.htdwps.grateful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Util.EmojiSelectUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.BeanPostViewHolder;

import timber.log.Timber;

public class MoodCountActivity extends AppCompatActivity {

    public static final String MOOD_TYPE_KEY_PARAM = "mood_param";

    RecyclerView.Adapter<BeanPostViewHolder> moodListAdapter;

    FirebaseUser firebaseUser;
    RecyclerView rvMoodList;
    LinearLayoutManager linearLayoutManager;

    DatabaseReference moodsListedPostReference;
    DatabaseReference moodListDirectoryReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_count);

        rvMoodList = findViewById(R.id.rv_mood_all_posts_by_user);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().getExtras() != null) {

            String moodValue = getIntent().getExtras().getString(MOOD_TYPE_KEY_PARAM);
            int moodIntValue = EmojiSelectUtil.emojiStringConvertToInt(moodValue);

            setTitle("Posts labeled \"" + EmojiSelectUtil.emojiIntConvertToString(moodIntValue) + "\"");

            moodsListedPostReference = FirebaseUtil.getMoodBeanListReference().child(firebaseUser.getUid()).child(String.valueOf(moodIntValue));

            moodListDirectoryReference = FirebaseUtil.getUserPostRef().child(firebaseUser.getUid());

            Toast.makeText(this, String.valueOf(moodIntValue), Toast.LENGTH_LONG).show();

            rvMoodList.setLayoutManager(createLayoutManager());

            rvMoodList.setAdapter(createMoodListRecyclerViewAdapter(moodsListedPostReference));

        } else {

            Toast.makeText(this, "Couldn't get a message", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
    }

    public RecyclerView.Adapter<BeanPostViewHolder> createMoodListRecyclerViewAdapter(DatabaseReference databaseReference) {

        moodListAdapter = new FirebaseRecyclerAdapter<Boolean, BeanPostViewHolder>(
                Boolean.class,
                R.layout.item_grateful_post_user_posts,
                BeanPostViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(final BeanPostViewHolder viewHolder, Boolean model, int position) {

                final String postKey = this.getRef(position).getKey();

                moodListDirectoryReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Beans beanPost = dataSnapshot.getValue(Beans.class);

                        if (beanPost != null) {
                            String year = DateUtils.formatDateTime(MoodCountActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                            String time = DateUtils.formatDateTime(MoodCountActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                            String dateTime = String.format("%s %s", year, time);

                            viewHolder.setBeanPostFields(beanPost.getMoodValue(), dateTime, beanPost.getBeanText(), beanPost.getTagList(), beanPost.isPublic(), beanPost.getCustomUser().getUserDisplayName(), false);
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

        return moodListAdapter;
    }

}
