package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.graphics.Typeface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htdwps.grateful.Model.CustomUser;
import com.htdwps.grateful.Model.Entries;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.EntryViewHolder;

public class UserJournalFragment extends Fragment {

    private OnFragmentInteractionListener   mListener;

    RecyclerView.Adapter<EntryViewHolder>   listAdapter;
    DatabaseReference                       mainReference;
    DatabaseReference                       userJournalReference;
    DatabaseReference                       publicJournalReference;
    FirebaseUser                            firebaseUser;
    LinearLayoutManager                     linearLayoutManager;
    RecyclerView                            recyclerView;
    CustomUser user;

    public UserJournalFragment() {
        // Required empty public constructor
    }

    public static UserJournalFragment newInstance(DatabaseReference databaseReference) {

        return new UserJournalFragment();

    }

    public void runLayout(View view) {
        recyclerView = view.findViewById(R.id.fragment_recyclerview_journal);

        setTypeFace();
    }

    // Unused font typeface setter
    public void setTypeFace() {
        Typeface headerFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Courgette-Regular.ttf");
    }

    public void runInitialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseUtil.getCurrentUser();
        mainReference = FirebaseUtil.getBaseRef();
        userJournalReference = FirebaseUtil.getUserJournalRef().child(user.getUserid());
        publicJournalReference = FirebaseUtil.getAllJournalRef();
    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
    }

    public RecyclerView.Adapter<EntryViewHolder> createAdapter(DatabaseReference databaseReference) {
        listAdapter = new FirebaseRecyclerAdapter<Boolean, EntryViewHolder>
                (
                        Boolean.class,
                        R.layout.item_grateful_post,
                        EntryViewHolder.class,
                        databaseReference
                ) {

            @Override
            protected void populateViewHolder(final EntryViewHolder viewHolder, Boolean bool, int position) {
                final String postKey = this.getRef(position).getKey();

                publicJournalReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Entries entries = dataSnapshot.getValue(Entries.class);

                        if (entries != null) {
                            String displayName;
                            String userId = entries.getUser().getUserid();

                            // Own journals don't need to show the you wrote... header above each journal post
                            if (userId != null && userId.equals(firebaseUser.getUid())) {

                                displayName = "You";
                                viewHolder.hideUserDisplayName();

                            } else {

                                displayName = entries.getUser().getUserDisplayName();

                            }

                            viewHolder.setViewObjects(entries.getEntryType(), displayName, entries.getPostText(), entries.getJournalText(), DateUtils.getRelativeTimeSpanString((long) entries.getTimestamp()).toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        return listAdapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_journal, container, false);

        runLayout(view);
        runInitialize();
        createLayoutManager();
        createAdapter(userJournalReference);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(listAdapter);

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
