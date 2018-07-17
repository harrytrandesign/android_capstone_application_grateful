package com.htdwps.grateful.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.htdwps.grateful.Util.StringConstantsUtil;
import com.htdwps.grateful.Viewholder.TagNameLayoutViewHolder;

public class TagsCounterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DatabaseReference tagNameChipsDirectoryReference;

    private FirebaseRecyclerAdapter<TagName, TagNameLayoutViewHolder> adapterTagNameList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerViewChipsTagNameList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tags_list_layout, container, false);

        setupLayoutView(view);
        setupInitializers();

        if (firebaseUser != null) {

            tagNameChipsDirectoryReference = FirebaseUtil.getTagsPostsWithTagDirectoryReference().child(firebaseUser.getUid());

            recyclerViewChipsTagNameList.setAdapter(createAdapterTagNameList(tagNameChipsDirectoryReference));

        }

        return view;
    }

    private void setupLayoutView(View view) {

        recyclerViewChipsTagNameList = view.findViewById(R.id.rv_tag_counter_list);
        recyclerViewChipsTagNameList.setLayoutManager(ChipLayoutManager.createLayoutManager(getActivity()));
        recyclerViewChipsTagNameList.addItemDecoration(new SpacingItemDecoration(StringConstantsUtil.OFFSET, StringConstantsUtil.OFFSET));

    }

    private void setupInitializers() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    public FirebaseRecyclerAdapter<TagName, TagNameLayoutViewHolder> createAdapterTagNameList(DatabaseReference databaseReference) {

        adapterTagNameList = new FirebaseRecyclerAdapter<TagName, TagNameLayoutViewHolder>(
                TagName.class,
                R.layout.item_tag_single_label,
                TagNameLayoutViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final TagNameLayoutViewHolder viewHolder, final TagName model, int position) {

                viewHolder.setTagName(model.getTagName());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent tagPostActivityIntent = new Intent(getActivity(), TagPostActivity.class);
                        tagPostActivityIntent.putExtra(StringConstantsUtil.TAG_WORD_KEY_PARAM, model.getTagName());
                        startActivity(tagPostActivityIntent);

                    }
                });

            }

        };

        return adapterTagNameList;
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
