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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.BeanCommentActivity;
import com.htdwps.grateful.FirebaseUiAuthActivity;
import com.htdwps.grateful.MainWindowActivity;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.BeanPostViewHolder;
import com.htdwps.grateful.Viewholder.EntryViewHolder;
import com.htdwps.grateful.Viewholder.GratePostViewHolder;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrivateBeansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrivateBeansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    public static final String BEAN_POST_PARAM = "public_posting_key";
    public static final String CUSTOM_USER_PARAM = "custom_user_key";

    private DatabaseReference queryRefrence;
    String queryTypeString;

    RecyclerView.Adapter<EntryViewHolder> listAdapter;
    RecyclerView.Adapter<GratePostViewHolder> gratefulAdapter;
    FirebaseRecyclerAdapter<Beans, BeanPostViewHolder> beanPostAdapter;
    DatabaseReference mainAllPostsReference;
    DatabaseReference userOnlyPostsReference;
    FirebaseUser firebaseUser;
    LinearLayoutManager linearLayoutManager;

    TextView tvTextTogglePublicPrivateFeed;
    TextView tvRemindAddText;
    Switch switchToggleValue;
    RecyclerView recyclerView;
    RecyclerView moodCounterRecyclerView;
    CustomUser user;

    boolean isPublicFeedDisplayed = false;

    private OnFragmentInteractionListener mListener;

    public PrivateBeansFragment() {
        // Required empty public constructor
    }

    public static PrivateBeansFragment newInstance(String param1, String param2) {
        PrivateBeansFragment fragment = new PrivateBeansFragment();
        Bundle args = new Bundle();
        args.putString(DATABASE_PARAM, param1);
        args.putString(ALL_POSTS_PARAM, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_private_beans, container, false);

        runLayout(view);
        runInitialize();
        createLayoutManager();
//        createAdapter(userOnlyPostsReference);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(createBeanRecyclerViewAdapter(queryRefrence));

//        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), new RecyclerViewItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (isPublicFeedDisplayed) {
//                    Beans beans = beanPostAdapter.getItem(position);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(BEAN_POST_PARAM, beans);
//
//                    Intent intent = new Intent(getActivity(), BeanCommentActivity.class);
//                    intent.putExtra(BEAN_POST_PARAM, beans);
//                    startActivity(intent);
//                }
//            }
//        }));

        return view;

    }

    public void runLayout(View view) {
        tvRemindAddText = view.findViewById(R.id.tv_load_some_posts_text);
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
            mainAllPostsReference = FirebaseUtil.getUserPostRef();
            userOnlyPostsReference = FirebaseUtil.getGratefulPersonalRef().child(user.getUserid());

            Timber.i(queryTypeString);

            if (queryTypeString.equals(MainWindowActivity.PUBLIC_PARAM)) {

                queryRefrence = FirebaseUtil.getAllPostRef();

            } else if (queryTypeString.equals(MainWindowActivity.PRIVATE_PARAM)) {

                queryRefrence = mainAllPostsReference.child(user.getUserid());

            }

        } else {

            Intent intent = new Intent(PrivateBeansFragment.this.getContext(), FirebaseUiAuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//
//        outState.putBoolean("PublicFeedStatus", isPublicFeedDisplayed);
//
//        super.onSaveInstanceState(outState);
//    }
//
//
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//
//        super.onViewStateRestored(savedInstanceState);
//
//        boolean publicFeedDisplayState = savedInstanceState.getBoolean("PublicFeedStatus");
//        switchToggleValue.setChecked(publicFeedDisplayState);
//        toggleBetweenPublicPrivatePostFeed(publicFeedDisplayState);
//
//    }

    public void toggleBetweenPublicPrivatePostFeed(boolean feed) {

        isPublicFeedDisplayed = feed;

        if (feed) {
//            tvTextTogglePublicPrivateFeed.setText(getResources().getString(R.string.switch_private_text_label));
            queryRefrence = FirebaseUtil.getAllPostRef();
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

//    FirebaseRecyclerAdapter<Beans, BeanPostViewHolder> beanAdapter = new FirebaseRecyclerAdapter<Beans, BeanPostViewHolder>() {
//        @Override
//        protected void populateViewHolder(BeanPostViewHolder viewHolder, Beans model, int position) {
//
//        }
//    }

    public FirebaseRecyclerAdapter<Beans, BeanPostViewHolder> createBeanRecyclerViewAdapter(DatabaseReference databaseReference) {
        beanPostAdapter = new FirebaseRecyclerAdapter<Beans, BeanPostViewHolder>(
                Beans.class,
                R.layout.item_grateful_post_user_posts,
                BeanPostViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BeanPostViewHolder viewHolder, Beans model, int position) {

                String year = DateUtils.formatDateTime(getActivity(), (long) model.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                String time = DateUtils.formatDateTime(getActivity(), (long) model.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                String dateTime = String.format("%s %s", year, time);

                viewHolder.setBeanPostFields(model.getMoodValue(), dateTime, model.getBeanText(), model.getTagList(), model.isPublic(), model.getCustomUser().getUserDisplayName(), isPublicFeedDisplayed);
                Timber.i("This message's value is %s", String.valueOf(model.getMoodValue()));
                Timber.i("This message's message is %s", model.getBeanText());

            }

            @Override
            public BeanPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BeanPostViewHolder beanPostViewHolder = super.onCreateViewHolder(parent, viewType);

                beanPostViewHolder.setOnClickListener(new BeanPostViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isPublicFeedDisplayed) {
                            Toast.makeText(getActivity(), "Public Feed Showing Public Posts, Go to Comment's Section", Toast.LENGTH_SHORT).show();
                            Beans beans = beanPostAdapter.getItem(position);
                            CustomUser customUser = beans.getCustomUser();
//
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(BEAN_POST_PARAM, beans);
//
                            Intent intent = new Intent(getActivity(), BeanCommentActivity.class);
                            intent.putExtra(BEAN_POST_PARAM, beans);
                            intent.putExtra(CUSTOM_USER_PARAM, customUser);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Nothing going to happen here. Clicked at position " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return beanPostViewHolder;
            }

//            @Override
//            public int getItemCount() {
//                int count = super.getItemCount();
//
//                if (count == 0) {
//                    tvRemindAddText.setVisibility(View.VISIBLE);
//                } else {
//                    tvRemindAddText.setVisibility(View.GONE);
//                }
//
//                return count;
//            }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
