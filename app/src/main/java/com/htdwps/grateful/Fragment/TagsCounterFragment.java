package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.TagListViewHolder;

public class TagsCounterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    DatabaseReference tagListReference;
    RecyclerView tagListRecyclerView;
    FirebaseRecyclerAdapter<String, TagListViewHolder> tagListAdapter;

    public TagsCounterFragment() {
        // Required empty public constructor
    }

    public static TagsCounterFragment newInstance() {
        TagsCounterFragment fragment = new TagsCounterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public LinearLayoutManager createLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tags_counter, container, false);

        tagListRecyclerView = view.findViewById(R.id.rv_tag_counter_list);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            tagListReference = FirebaseUtil.getTagsBeanReference().child(firebaseUser.getUid());

            tagListAdapter = new FirebaseRecyclerAdapter<String, TagListViewHolder>(
                    String.class,
                    R.layout.item_tag_single_label,
                    TagListViewHolder.class,
                    tagListReference
            ) {
                @Override
                protected void populateViewHolder(final TagListViewHolder viewHolder, String model, int position) {

                    tagListReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String tag;

                            for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                                tag = childSnapShot.getValue().toString();
                                String tags = childSnapShot.getKey();
                                viewHolder.setTagName(tag);

                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                };
            };

            tagListRecyclerView.setLayoutManager(createLayoutManager());
            tagListRecyclerView.setAdapter(tagListAdapter);

        }

        return view;
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
