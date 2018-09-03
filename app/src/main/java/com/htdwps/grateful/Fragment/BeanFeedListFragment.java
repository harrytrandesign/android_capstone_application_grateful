package com.htdwps.grateful.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htdwps.grateful.BeanCommentActivity;
import com.htdwps.grateful.FirebaseUiAuthActivity;
import com.htdwps.grateful.Model.BeanPosts;
import com.htdwps.grateful.Model.UserProfile;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.DatabaseReferenceHelperUtil;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.GeneralActivityHelperUtil;
import com.htdwps.grateful.Util.ProgressDialogUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.BeanLayoutViewHolder;

import timber.log.Timber;

public class BeanFeedListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    // TODO - Pull the boolean from the Activity
    boolean isPublicFeedDisplayed = false;

    private static final int FIRST_POSITION = 0;
    private static final int LAST_POSITION = 50;
    private static final int INCREMENT_VALUE = 50;

    private UserProfile userProfile;

    private DatabaseReference generalPostsQueryDirectoryReference;
    private DatabaseReference publicPostsListDirectoryReference;
    private DatabaseReference privateUserOnlyListDirectoryReference;

    private FirebaseRecyclerAdapter<BeanPosts, BeanLayoutViewHolder> adapterForBeanPosts;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;

    private RecyclerView recyclerViewMainBeanFeedsList;

    private String queryDirectoryPathString;

    public BeanFeedListFragment() {
        // Required empty public constructor
    }

    public static BeanFeedListFragment newInstance(String publicPrivateStatusValue) {
        BeanFeedListFragment fragment = new BeanFeedListFragment();
        Bundle args = new Bundle();
        args.putString(StringConstantsUtil.ALL_POSTS_PARAM, publicPrivateStatusValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            queryDirectoryPathString = getArguments().getString(StringConstantsUtil.ALL_POSTS_PARAM);
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

        recyclerViewMainBeanFeedsList.setAdapter(createBeanRecyclerViewAdapter(generalPostsQueryDirectoryReference));

        return view;

    }

    public void runLayout(View view) {

        recyclerViewMainBeanFeedsList = view.findViewById(R.id.rv_all_posts_list);
        recyclerViewMainBeanFeedsList.setLayoutManager(GeneralActivityHelperUtil.createVerticalLinearLayout(this.getActivity(), LinearLayout.VERTICAL, true, true));

    }

    public void runInitialize() {

        progressDialog = ProgressDialogUtil.showProgressDialog(getActivity(), getString(R.string.progress_dialog_loading_message));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userProfile = FirebaseUtil.getCurrentUser();

        if (userProfile != null) {

            String userId = userProfile.getUserid();

            publicPostsListDirectoryReference = DatabaseReferenceHelperUtil.getAllPostsSubmittedToServiceDirectoryRef();

            privateUserOnlyListDirectoryReference = DatabaseReferenceHelperUtil.getUserPostsMatchingThisUserIdDirectoryRef(userId);

//            Timber.d(queryDirectoryPathString);

            if (TextUtils.equals(StringConstantsUtil.PUBLIC_PARAM, queryDirectoryPathString)) {

                generalPostsQueryDirectoryReference = publicPostsListDirectoryReference;

                isPublicFeedDisplayed = true;

            } else if (TextUtils.equals(StringConstantsUtil.PRIVATE_PARAM, queryDirectoryPathString)) {

                generalPostsQueryDirectoryReference = privateUserOnlyListDirectoryReference;

                isPublicFeedDisplayed = false;

            }

            checkIfDataExistsCall(generalPostsQueryDirectoryReference);

        } else {

            Intent intent = new Intent(BeanFeedListFragment.this.getContext(), FirebaseUiAuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private void checkIfDataExistsCall(DatabaseReference generalPostsQueryDirectoryReference) {

        generalPostsQueryDirectoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProgressDialogUtil.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public FirebaseRecyclerAdapter<BeanPosts, BeanLayoutViewHolder> createBeanRecyclerViewAdapter(DatabaseReference databaseReference) {
        adapterForBeanPosts = new FirebaseRecyclerAdapter<BeanPosts, BeanLayoutViewHolder>(
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

                if (!isPublicFeedDisplayed) {

                    viewHolder.itemView.setClickable(false);

                } else {

                    viewHolder.setOnClickListener(new BeanLayoutViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            if (isPublicFeedDisplayed) {

                                String debugMsg = getString(R.string.debug_message_show_public_post);
                                Timber.d(debugMsg);

                                // Create this Parcelable objects to send to Comment Activity
                                BeanPosts beanPosts = adapterForBeanPosts.getItem(position);

                                Intent intent = new Intent(getActivity(), BeanCommentActivity.class);
                                intent.putExtra(StringConstantsUtil.BEAN_POST_PARAM, beanPosts);
                                startActivity(intent);

                            } else {

                                Timber.d(getString(R.string.log_nothing_happening) + position);

                            }
                        }
                    });
                }

            }

        };

        return adapterForBeanPosts;
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
