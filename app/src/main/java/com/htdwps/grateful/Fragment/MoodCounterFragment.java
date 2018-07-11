package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Model.MoodCount;
import com.htdwps.grateful.MoodCountActivity;
import com.htdwps.grateful.R;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Viewholder.MoodCountViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoodCounterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoodCounterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodCounterFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference moodCountListReference;
    RecyclerView moodListRecyclerView;
    FirebaseRecyclerAdapter<MoodCount, MoodCountViewHolder> moodCountAdapter;
    TextView tvRemindAddNewPosts;

    GridLayoutManager gridLayoutManager;

    private OnFragmentInteractionListener mListener;

    public MoodCounterFragment() {
        // Required empty public constructor
    }

    public static MoodCounterFragment newInstance() {
        MoodCounterFragment fragment = new MoodCounterFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_counter, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        moodListRecyclerView = view.findViewById(R.id.rv_mood_counter_list);
        moodListRecyclerView.setLayoutManager(gridLayoutManager);
//        tvRemindAddNewPosts = view.findViewById(R.id.tv_load_some_posts_text);

        if (firebaseUser != null) {
            moodCountListReference = FirebaseUtil.getMoodCounterReference().child(firebaseUser.getUid());
            moodCountAdapter = new FirebaseRecyclerAdapter<MoodCount, MoodCountViewHolder>(
                    MoodCount.class,
                    R.layout.item_mood_count_single_value,
                    MoodCountViewHolder.class,
                    moodCountListReference
            ) {
                @Override
                protected void populateViewHolder(MoodCountViewHolder viewHolder, final MoodCount model, int position) {

                    viewHolder.setMoodValue(model.getMoodName(), model.getValueCount());

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent moodCountActivityIntent = new Intent(getActivity(), MoodCountActivity.class);
                            moodCountActivityIntent.putExtra(MoodCountActivity.MOOD_TYPE_KEY_PARAM, model.getMoodName());
                            startActivity(moodCountActivityIntent);

                        }
                    });

                }

//                @Override
//                public int getItemCount() {
//                    int count = super.getItemCount();
//
//                    if (count == 0) {
//                        tvRemindAddNewPosts.setVisibility(View.VISIBLE);
//                    } else {
//                        tvRemindAddNewPosts.setVisibility(View.GONE);
//                    }
//
//                    return count;
//                }
            };
        }

        moodListRecyclerView.setAdapter(moodCountAdapter);

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
