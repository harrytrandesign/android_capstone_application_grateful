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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Model.Entries;
import com.htdwps.grateful.Model.User;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.EntryViewHolder;

public class UserPostFragment extends Fragment {

    private OnFragmentInteractionListener   mListener;

    RecyclerView.Adapter<EntryViewHolder>   listAdapter;
    DatabaseReference                       mainReference;
    DatabaseReference                       userPostReference;
    FirebaseUser                            firebaseUser;
    LinearLayoutManager                     linearLayoutManager;
    RecyclerView                            recyclerView;
    TextView                                tvUserWrote;
    User                                    user;

    public UserPostFragment() {
        // Required empty public constructor
    }

    public static UserPostFragment newInstance(DatabaseReference databaseReference) {

        return new UserPostFragment();

    }

    public void runLayout(View view) {
        tvUserWrote = view.findViewById(R.id.tv_user_wrote_title);
        recyclerView = view.findViewById(R.id.fragment_recyclerview_post);

        setTypeFace();
    }

    public void setTypeFace() {
        Typeface headerFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bevan-Regular.ttf");
        tvUserWrote.setTypeface(headerFont);
    }

    public void runInitialize() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseUtil.getCurrentUser();
        mainReference = FirebaseUtil.getBaseRef();
        userPostReference = FirebaseUtil.getUserPostRef().child(user.getUserid());
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
                viewHolder.hideUserDisplayName();

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
        View view = inflater.inflate(R.layout.fragment_user_post, container, false);

        runLayout(view);
        runInitialize();
        createLayoutManager();
        createAdapter(userPostReference);

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
