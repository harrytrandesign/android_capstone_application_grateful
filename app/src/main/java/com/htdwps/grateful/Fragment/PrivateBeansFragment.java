package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.BeanCommentActivity;
import com.htdwps.grateful.FirebaseUiAuthActivity;
import com.htdwps.grateful.MainWindowActivity;
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.BeanLayoutViewHolder;

import timber.log.Timber;

public class PrivateBeansFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int FIRST_POSITION = 0;
    private static final int LAST_POSITION = 50;
    private static final int INCREMENT_VALUE = 50;

    private static final String DATABASE_PARAM = "database_reference_param";
    private static final String ALL_POSTS_PARAM = "public_posts";
    private static final String USER_POSTS_PARAM = "user_posts";

    private static final String USER_PROFILE_KEY = "user_profile_id_key";
    private static final String USER_DISPLAY_NAME = "user_displayname";
    private static final String USER_PICTURE = "user_photo_url";

    // Key Params for wrapping Parcelable Objects
    public static final String BEAN_POST_PARAM = "public_posting_key";
    public static final String CUSTOM_USER_PARAM = "custom_user_key";

    private DatabaseReference queryRefrence;
    String queryTypeString;

    FirebaseRecyclerAdapter<BeanPosts, BeanLayoutViewHolder> beanPostAdapter;
    DatabaseReference mainAllPostsReference;
    DatabaseReference userOnlyPostsReference;
    FirebaseUser firebaseUser;
    LinearLayoutManager linearLayoutManager;

    TextView tvTextTogglePublicPrivateFeed;
    Switch switchToggleValue;
    RecyclerView recyclerView;
    RecyclerView moodCounterRecyclerView;
    UserProfile user;

    boolean isPublicFeedDisplayed = false;

    private OnFragmentInteractionListener mListener;

    public PrivateBeansFragment() {
        // Required empty public constructor
    }

    public static PrivateBeansFragment newInstance(String param1) {
        PrivateBeansFragment fragment = new PrivateBeansFragment();
        Bundle args = new Bundle();
        args.putString(DATABASE_PARAM, param1);
        args.putString(ALL_POSTS_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            queryTypeString = getArguments().getString(ALL_POSTS_PARAM);
        } else {
            throw new RuntimeException("You must specify a property reference.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beans_feed_layout, container, false);

        runLayout(view);
        runInitialize();
        createLayoutManager();
//        createAdapter(userOnlyPostsReference);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(createBeanRecyclerViewAdapter(queryRefrence));

        return view;

    }

    public void runLayout(View view) {
//        tvTextTogglePublicPrivateFeed = view.findViewById(R.id.tv_public_private_display_text);
//        switchToggleValue = view.findViewById(R.id.switch_toggle_public_private_feed_display);
        recyclerView = view.findViewById(R.id.fragment_recyclerview_post);

//        switchToggleValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                toggleBetweenPublicPrivatePostFeed(b);
//            }
//        });
    }

    public void runInitialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        user = FirebaseUtil.getCurrentUser();

        if (user != null) {
            mainAllPostsReference = FirebaseUtil.getPrivateUserBeanPostDirectoryReference();
            userOnlyPostsReference = FirebaseUtil.getPrivateUserBeanPostDirectoryReference().child(user.getUserid());

            Timber.i(queryTypeString);

            if (queryTypeString.equals(MainWindowActivity.PUBLIC_PARAM)) {

                queryRefrence = FirebaseUtil.getBeanPublicDirectoryReference();

            } else if (queryTypeString.equals(MainWindowActivity.PRIVATE_PARAM)) {

                queryRefrence = mainAllPostsReference.child(user.getUserid());

            }

        } else {

            Intent intent = new Intent(PrivateBeansFragment.this.getContext(), FirebaseUiAuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    public void toggleBetweenPublicPrivatePostFeed(boolean feed) {

        isPublicFeedDisplayed = feed;

        if (feed) {
//            tvTextTogglePublicPrivateFeed.setText(getResources().getString(R.string.switch_private_text_label));
            queryRefrence = FirebaseUtil.getBeanPublicDirectoryReference();
        } else {
//            tvTextTogglePublicPrivateFeed.setText(getResources().getString(R.string.switch_public_text_label));
            queryRefrence = mainAllPostsReference.child(user.getUserid());
        }

        recyclerView.setAdapter(createBeanRecyclerViewAdapter(queryRefrence));

    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
    }

    public FirebaseRecyclerAdapter<BeanPosts, BeanLayoutViewHolder> createBeanRecyclerViewAdapter(DatabaseReference databaseReference) {
        beanPostAdapter = new FirebaseRecyclerAdapter<BeanPosts, BeanLayoutViewHolder>(
                BeanPosts.class,
                R.layout.item_bean_user_single_post,
                BeanLayoutViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BeanLayoutViewHolder viewHolder, BeanPosts model, int position) {

                String year = DateUtils.formatDateTime(getActivity(), (long) model.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                String time = DateUtils.formatDateTime(getActivity(), (long) model.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                String dateTime = String.format("%s %s", year, time);

                viewHolder.setBeanPostFields(model.getMoodValue(), dateTime, model.getBeanText(), model.getTagList(), model.isPublic(), model.getUserProfile().getUserDisplayName(), isPublicFeedDisplayed);
                Timber.i("This message's value is %s", String.valueOf(model.getMoodValue()));
                Timber.i("This message's message is %s", model.getBeanText());

            }

            @Override
            public BeanLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BeanLayoutViewHolder beanPostsViewHolder = super.onCreateViewHolder(parent, viewType);

                beanPostsViewHolder.setOnClickListener(new BeanLayoutViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isPublicFeedDisplayed) {
                            String debugMsg = "Public Feed Showing Public Posts, Go to Comment's Section";
                            Timber.d(debugMsg);
//                            Toast.makeText(getActivity(), debugMsg, Toast.LENGTH_SHORT).show();

                            // Create Parcelable objects to send to Comment Activity
                            BeanPosts beanPosts = beanPostAdapter.getItem(position);
                            UserProfile userProfile = beanPosts.getUserProfile();

                            Intent intent = new Intent(getActivity(), BeanCommentActivity.class);
                            intent.putExtra(BEAN_POST_PARAM, beanPosts);
                            intent.putExtra(CUSTOM_USER_PARAM, userProfile);
                            startActivity(intent);
                        } else {
                            Timber.d("Nothing going to happen here. Clicked at position " + position);
                        }
                    }
                });

                return beanPostsViewHolder;
            }

        };

        return beanPostAdapter;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
