package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Model.MoodCount;
import com.htdwps.grateful.MoodCountActivity;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.DatabaseReferenceHelperUtil;
import com.htdwps.grateful.Util.GeneralActivityHelperUtil;
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.MoodCountLayoutViewHolder;

public class MoodCounterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DatabaseReference moodCountDirectoryReference;

    private FirebaseRecyclerAdapter<MoodCount, MoodCountLayoutViewHolder> adapterMoodCountList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerViewMoodCountList;

    public MoodCounterFragment() {
        // Required empty public constructor
    }

    public static MoodCounterFragment newInstance() {
        return new MoodCounterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mood_list_layout, container, false);

        setupLayoutView(view);
        setupInitializers();

        if (firebaseUser != null) {

            moodCountDirectoryReference = DatabaseReferenceHelperUtil.getMoodCountDirectoryRef(firebaseUser.getUid());

            recyclerViewMoodCountList.setAdapter(createMoodCounterGridListAdapter(moodCountDirectoryReference));

        }

        return view;
    }

    private void setupLayoutView(View view) {

        recyclerViewMoodCountList = view.findViewById(R.id.rv_mood_counter_list);
        recyclerViewMoodCountList.setLayoutManager(GeneralActivityHelperUtil.createGridLayoutManager(getActivity(), 3));

    }

    private void setupInitializers() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    public FirebaseRecyclerAdapter<MoodCount, MoodCountLayoutViewHolder> createMoodCounterGridListAdapter(DatabaseReference databaseReference) {

        adapterMoodCountList = new FirebaseRecyclerAdapter<MoodCount, MoodCountLayoutViewHolder>(
                MoodCount.class,
                R.layout.item_mood_icon_count_value,
                MoodCountLayoutViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(MoodCountLayoutViewHolder viewHolder, final MoodCount model, int position) {

                viewHolder.setMoodValue(model.getMoodName(), model.getValueCount());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent moodCountActivityIntent = new Intent(getActivity(), MoodCountActivity.class);
                        moodCountActivityIntent.putExtra(StringConstantsUtil.MOOD_TYPE_KEY_PARAM, model.getMoodName());
                        startActivity(moodCountActivityIntent);

                    }
                });

            }

        };

        return adapterMoodCountList;

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
