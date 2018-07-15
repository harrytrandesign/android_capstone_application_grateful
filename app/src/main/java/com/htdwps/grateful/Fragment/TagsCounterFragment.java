package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.htdwps.grateful.Adapter.ChipLayoutManager;
import com.htdwps.grateful.Model.TagName;
import com.htdwps.grateful.R;
import com.htdwps.grateful.TagPostActivity;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.SpacingItemDecoration;
import com.htdwps.grateful.Viewholder.TagNameLayoutViewHolder;

public class TagsCounterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final int OFFSET = 16;

    DatabaseReference tagListReference;
    RecyclerView tagListRecyclerView;
    FirebaseRecyclerAdapter<TagName, TagNameLayoutViewHolder> tagListAdapter;

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
        View view = inflater.inflate(R.layout.fragment_tags_list_layout, container, false);

        tagListRecyclerView = view.findViewById(R.id.rv_tag_counter_list);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            tagListReference = FirebaseUtil.getTagsBeanDirectoryReference().child(firebaseUser.getUid());

            tagListAdapter = new FirebaseRecyclerAdapter<TagName, TagNameLayoutViewHolder>(
                    TagName.class,
                    R.layout.item_tag_single_label,
                    TagNameLayoutViewHolder.class,
                    tagListReference
            ) {
                @Override
                protected void populateViewHolder(final TagNameLayoutViewHolder viewHolder, final TagName model, int position) {

                    viewHolder.setTagName(model.getTagName());

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent tagPostActivityIntent = new Intent(getActivity(), TagPostActivity.class);
                            tagPostActivityIntent.putExtra(TagPostActivity.TAG_WORD_KEY_PARAM, model.getTagName());
                            startActivity(tagPostActivityIntent);
                        }
                    });

                }

                //                @Override
//                public TagNameLayoutViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
//                    TagNameLayoutViewHolder tagListViewHolder = super.onCreateViewHolder(parent, viewType);
//
//                    tagListViewHolder.setOnClickListener(new TagNameLayoutViewHolder.ClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int position) {
//                            TagName tagName = (TagName) adapterView.getAdapter().getItem(position);
//                            Intent tagPostActivityIntent = new Intent(getActivity(), TagPostActivity.class);
//                            tagPostActivityIntent.putExtra(TagPostActivity.TAG_WORD_KEY_PARAM, tagName.getTagName());
//                            startActivity(tagPostActivityIntent);
//
//                        }
//                    });
//
////                        @Override
////                        public void onTagClick(TagName tagName) {
////                            Intent tagPostActivityIntent = new Intent(getActivity(), TagPostActivity.class);
////                            tagPostActivityIntent.putExtra(TagPostActivity.TAG_WORD_KEY_PARAM, tagName.getTagName());
////                            startActivity(tagPostActivityIntent);
////                        }
////                });
//
//                    return tagListViewHolder;
//                }
            };

            tagListRecyclerView.setLayoutManager(ChipLayoutManager.createLayoutManager(getActivity()));
            tagListRecyclerView.addItemDecoration(new SpacingItemDecoration(OFFSET, OFFSET));
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
