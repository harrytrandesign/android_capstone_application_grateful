package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Model.Beans;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.Entries;
import com.htdwps.grateful.Model.GratefulPost;
import com.htdwps.grateful.PersonalPostsGridActivity;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.BeanPostViewHolder;
import com.htdwps.grateful.Viewholder.EntryViewHolder;
import com.htdwps.grateful.Viewholder.GratePostViewHolder;

public class UserPostFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final String DATABASE_PARAM = "database_reference_param";
    private static final String ALL_POSTS_PARAM = "public_posts";
    private static final String USER_POSTS_PARAM = "user_posts";

    private static final String USER_PROFILE_KEY = "user_profile_id_key";
    private static final String USER_DISPLAY_NAME = "user_displayname";
    private static final String USER_PICTURE = "user_photo_url";

    private DatabaseReference queryRefrence;
    String queryTypeString;

    RecyclerView.Adapter<EntryViewHolder> listAdapter;
    RecyclerView.Adapter<GratePostViewHolder> gratefulAdapter;
    RecyclerView.Adapter<BeanPostViewHolder> beanPostAdapter;
    DatabaseReference mainAllPostsReference;
    DatabaseReference userOnlyPostsReference;
    FirebaseUser firebaseUser;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    CustomUser user;

    public UserPostFragment() {
        // Required empty public constructor
    }

    public static UserPostFragment newInstance(String databaseReferenceString) {

        UserPostFragment userPostFragment = new UserPostFragment();

        Bundle args = new Bundle();
        args.putString(DATABASE_PARAM, databaseReferenceString);
        userPostFragment.setArguments(args);

        return userPostFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            queryTypeString = getArguments().getString(DATABASE_PARAM);
        } else {
            throw new RuntimeException("You must specify a property reference.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_post, container, false);

        runLayout(view);
        runInitialize();
        createLayoutManager();
//        createAdapter(userOnlyPostsReference);
        createBeanRecyclerViewAdapter(queryRefrence);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(beanPostAdapter);

        return view;
    }

    public void runLayout(View view) {
        recyclerView = view.findViewById(R.id.fragment_recyclerview_post);

        setTypeFace();
    }

    //
    public void setTypeFace() {
        Typeface headerFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Courgette-Regular.ttf");
    }

    public void runInitialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseUtil.getCurrentUser();
        mainAllPostsReference = FirebaseUtil.getUserPostRef();
        userOnlyPostsReference = FirebaseUtil.getGratefulPersonalRef().child(user.getUserid());

        if (queryTypeString.equals(ALL_POSTS_PARAM)) {

            queryRefrence = mainAllPostsReference.child(user.getUserid());

        } else if (queryTypeString.equals(ALL_POSTS_PARAM)) {

            queryRefrence = mainAllPostsReference.child(user.getUserid());

        }
    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
    }

    public Fragment launchFragment(String userKey, String userDisplayName, String userImageString) {
        Fragment fragment = null;

        fragment = PersonProfileFragment.newInstance(userKey, userDisplayName, userImageString);

        return fragment;
    }

    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
    }

    public RecyclerView.Adapter<BeanPostViewHolder> createBeanRecyclerViewAdapter(DatabaseReference databaseReference) {
        beanPostAdapter = new FirebaseRecyclerAdapter<Beans, BeanPostViewHolder>(
                Beans.class,
                R.layout.item_grateful_post_user_posts,
                BeanPostViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(BeanPostViewHolder viewHolder, Beans model, int position) {

                viewHolder.setBeanPostFields(model.getMoodType(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString(), model.getBeanText(), model.getTagList(), model.isPublic());

            }
        };

        return beanPostAdapter;
    }

    public RecyclerView.Adapter<GratePostViewHolder> createRecyclerViewAdater(DatabaseReference databaseReference) {
        gratefulAdapter = new FirebaseRecyclerAdapter<GratefulPost, GratePostViewHolder>(
                GratefulPost.class,
                R.layout.item_grateful_post_v2,
                GratePostViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(GratePostViewHolder viewHolder, final GratefulPost model, int position) {

                String displayname = model.getUser().getUserid().equals(firebaseUser.getUid()) ? "You" : model.getUserDisplayName();

                viewHolder.populatePostEntry(displayname, model.getGratefulMessage(), model.getPhotoUrlString());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent gridIntent = new Intent(UserPostFragment.this.getContext(), PersonalPostsGridActivity.class);
                        gridIntent.putExtra(USER_PROFILE_KEY, model.getUser().getUserid());
                        gridIntent.putExtra(USER_DISPLAY_NAME, model.getUser().getUserDisplayName());
//                        gridIntent.putExtra(USER_PICTURE, model.getUser().getUserPhoto());
                        startActivity(gridIntent);

                    }
                });

            }
        };

        return gratefulAdapter;
    }

    public RecyclerView.Adapter<EntryViewHolder> createAdapter(DatabaseReference databaseReference) {
        listAdapter = new FirebaseRecyclerAdapter<Entries, EntryViewHolder>
                (
                        Entries.class,
                        R.layout.item_grateful_post,
                        EntryViewHolder.class,
                        databaseReference
                ) {

            @Override
            protected void populateViewHolder(EntryViewHolder viewHolder, Entries model, int position) {
                final String postKey = ((FirebaseRecyclerAdapter) listAdapter).getRef(position).getKey();

                String displayName;
                String userId = model.getUser().getUserid();
                if (userId.equals(firebaseUser.getUid())) {
                    displayName = "You";
                } else {
                    displayName = model.getUserDisplayName();
                }

                viewHolder.setViewObjects(model.getEntryType(), displayName, model.getPostText(), model.getJournalText(), DateUtils.getRelativeTimeSpanString((long) model.getTimestamp()).toString());
                viewHolder.hideUserDisplayName();

            }
        };

        return listAdapter;
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
