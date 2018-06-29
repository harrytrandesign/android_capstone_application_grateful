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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.FirebaseUiAuthActivity;
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
    RecyclerView moodCounterRecyclerView;
    CustomUser user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PrivateBeansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivateBeansFragment.
     */
    public static PrivateBeansFragment newInstance(String param1, String param2) {
        PrivateBeansFragment fragment = new PrivateBeansFragment();
        Bundle args = new Bundle();
        args.putString(DATABASE_PARAM, param1);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_private_beans, container, false);

        runLayout(view);
        runInitialize();
        createLayoutManager();
//        createAdapter(userOnlyPostsReference);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(createBeanRecyclerViewAdapter(queryRefrence));

        return view;

    }

    public void runLayout(View view) {
        recyclerView = view.findViewById(R.id.fragment_recyclerview_post);
    }

    public void runInitialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        user = FirebaseUtil.getCurrentUser();

        if (user != null) {
            mainAllPostsReference = FirebaseUtil.getUserPostRef();
            userOnlyPostsReference = FirebaseUtil.getGratefulPersonalRef().child(user.getUserid());

            if (queryTypeString.equals(ALL_POSTS_PARAM)) {

                queryRefrence = mainAllPostsReference.child(user.getUserid());

            } else if (queryTypeString.equals(ALL_POSTS_PARAM)) {

                queryRefrence = mainAllPostsReference.child(user.getUserid());

            }

        } else {

            Intent intent = new Intent(PrivateBeansFragment.this.getContext(), FirebaseUiAuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
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

                String year = DateUtils.formatDateTime(getActivity(), (long) model.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR);
                String time = DateUtils.formatDateTime(getActivity(), (long) model.getTimestamp(), DateUtils.FORMAT_SHOW_TIME);
                String dateTime = String.format("%s %s", year, time);

                viewHolder.setBeanPostFields(model.getMoodValue(), dateTime, model.getBeanText(), model.getTagList(), model.isPublic());
                Timber.i("This message's value is " + String.valueOf(model.getMoodValue()));
                Timber.i("This message's message is " + model.getBeanText());

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
