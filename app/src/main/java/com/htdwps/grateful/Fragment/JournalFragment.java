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
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Model.Entries;
import com.htdwps.grateful.Model.User;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.EntryViewHolder;

public class JournalFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView.Adapter<EntryViewHolder>   listAdapter;
    DatabaseReference                       mainReference;
    DatabaseReference                       userJournalReference;
    FirebaseUser                            firebaseUser;
    LinearLayoutManager                     linearLayoutManager;
    RecyclerView                            recyclerView;
    User                                    user;

    public JournalFragment() {
        // Required empty public constructor
    }

    public static JournalFragment newInstance(DatabaseReference databaseReference) {

        return new JournalFragment();

    }

    public void runLayout(View view) {
        recyclerView = view.findViewById(R.id.fragment_recyclerview_journal_all);

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
        userJournalReference = FirebaseUtil.getAllJournalRef();
    }

    public LinearLayoutManager createLayoutManager() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);

        return linearLayoutManager;
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
                // viewHolder.hideUserDisplayName();

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
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

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
