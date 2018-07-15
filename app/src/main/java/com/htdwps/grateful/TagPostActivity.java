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
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.BeanLayoutViewHolder;

import timber.log.Timber;

public class TagPostActivity extends AppCompatActivity {

    public static final String TAG_WORD_KEY_PARAM = "tag_param";

    FirebaseUser firebaseUser;
    RecyclerView.Adapter<BeanLayoutViewHolder> tagPostAdapter;
    RecyclerView rvPostsWithTag;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference mainPostsReference;
    DatabaseReference tagListReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_post);

        String taggedListing;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().getExtras() != null) {
            taggedListing = getIntent().getExtras().getString(TAG_WORD_KEY_PARAM);
            mainPostsReference = FirebaseUtil.getPrivateUserBeanPostDirectoryReference().child(firebaseUser.getUid());
            tagListReference = FirebaseUtil.getTagsPostsWithTagDirectoryReference().child(firebaseUser.getUid()).child(taggedListing);

            rvPostsWithTag = findViewById(R.id.rv_posts_with_tag);

            rvPostsWithTag.setLayoutManager(createLayoutManager());

            rvPostsWithTag.setAdapter(createTagPostRecyclerViewAdapter(tagListReference));

//            Toast.makeText(this, "This tag activity is for all the posts with the following tag: " + taggedListing, Toast.LENGTH_SHORT).show();

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

    public RecyclerView.Adapter<BeanLayoutViewHolder> createTagPostRecyclerViewAdapter(DatabaseReference databaseReference) {
        tagPostAdapter = new FirebaseRecyclerAdapter<Boolean, BeanLayoutViewHolder>(
                Boolean.class,
                R.layout.item_bean_user_single_post,
                BeanLayoutViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final BeanLayoutViewHolder viewHolder, Boolean model, int position) {

                final String postKey = this.getRef(position).getKey();

                mainPostsReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BeanPosts beanPost = dataSnapshot.getValue(BeanPosts.class);

                        if (beanPost != null) {
                            String year = DateUtils.formatDateTime(TagPostActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                            String time = DateUtils.formatDateTime(TagPostActivity.this, (long) beanPost.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                            String dateTime = String.format("%s %s", year, time);

                            viewHolder.setBeanPostFields(beanPost.getMoodValue(), dateTime, beanPost.getBeanText(), beanPost.getTagList(), beanPost.isPublic(), beanPost.getUserProfile().getUserDisplayName(), false);
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

        return tagPostAdapter;
    }


}
