package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Model.GratefulPost;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.GratePostViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonProfileFragment extends Fragment {

    RecyclerView.Adapter<GratePostViewHolder> gratefulAdapter;
    DatabaseReference theUsersPostsReference;

    private RecyclerView gridRecyclerView;
    private TextView postOwnerDisplayName;
//    private CircleImageView postOwnerDisplayImage;
    private LinearLayoutManager linearLayoutManager;

    private static final String USER_PROFILE_KEY = "user_profile_id_key";
    private static final String USER_DISPLAY_NAME = "user_displayname";
    private static final String USER_PICTURE = "user_photo_url";
    private String userReferenceKey;
    private String userDisplayName;
    private String userPhotoStringUrl;

    private OnFragmentInteractionListener mListener;

    public PersonProfileFragment() {
        // Required empty public constructor
    }

    public static PersonProfileFragment newInstance(String userIdKey, String userDisplayName, String userPhotoString) {
        PersonProfileFragment fragment = new PersonProfileFragment();
        Bundle args = new Bundle();
        args.putString(USER_PROFILE_KEY, userIdKey);
        args.putString(USER_DISPLAY_NAME, userDisplayName);
        args.putString(USER_PICTURE, userPhotoString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userReferenceKey = getArguments().getString(USER_PROFILE_KEY);
            userDisplayName = getArguments().getString(USER_DISPLAY_NAME);
            userPhotoStringUrl = getArguments().getString(USER_PICTURE);
        }
    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
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

                viewHolder.populatePostEntry(userDisplayName, model.getGratefulMessage(), model.getPhotoUrlString());

            }
        };

        return gratefulAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_profile, container, false);

        gridRecyclerView = view.findViewById(R.id.rv_users_posts);
        postOwnerDisplayName = view.findViewById(R.id.tv_user_display_name);
//        postOwnerDisplayImage = view.findViewById(R.id.iv_user_image_icon);

        postOwnerDisplayName.setText("Posts by " + userDisplayName);

        theUsersPostsReference = FirebaseUtil.getGratefulPersonalRef().child(userReferenceKey);

        createLayoutManager();
        createRecyclerViewAdater(theUsersPostsReference);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(PersonProfileFragment.this.getContext(), 2);
        gridLayoutManager.setReverseLayout(false);

        gridRecyclerView.setLayoutManager(gridLayoutManager);
        gridRecyclerView.setAdapter(gratefulAdapter);
        return view;

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
