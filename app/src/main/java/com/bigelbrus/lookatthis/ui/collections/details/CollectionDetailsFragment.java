package com.bigelbrus.lookatthis.ui.collections.details;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigelbrus.lookatthis.R;
import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.models.Photo;
import com.bigelbrus.lookatthis.util.AppUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionDetailsFragment extends Fragment {
    private static final String KEY_COLLECTION_ID = "COLLECTION_ID";
    private static final String KEY_COLLECTION_NAME = "COLLECTION_NAME";

    private RecyclerView collectionDetailsRecyclerView;
    private CollectionDetailsAdapter adapter;
    private String collectionId;
    private String collectionName;
    private TextView messageText;
    private ProgressBar loadingProgressBar;


    public static CollectionDetailsFragment newInstance(String id,String name) {
        Bundle args = new Bundle();
        args.putString(KEY_COLLECTION_ID,id);
        args.putString(KEY_COLLECTION_NAME,name);
        CollectionDetailsFragment collectionDetailsFragment = new CollectionDetailsFragment();
        collectionDetailsFragment.setArguments(args);
        return collectionDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection_details, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        AppUtils.setActionBar(activity,view,R.string.collections,true);
        loadingProgressBar = view.findViewById(R.id.collection_details_progress_bar);
        messageText = view.findViewById(R.id.collection_details_message_text);

        collectionDetailsRecyclerView = view.findViewById(R.id.collection_details_recycler_view);
        adapter = new CollectionDetailsAdapter(getFragmentManager());
        collectionDetailsRecyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        collectionDetailsRecyclerView.setLayoutManager(layoutManager);
        showLoading();
        if (getArguments() != null) {
            collectionId = getArguments().getString(KEY_COLLECTION_ID);
            collectionName = getArguments().getString(KEY_COLLECTION_NAME);
        }
        ((TextView)view.findViewById(R.id.toolbar_title)).setText(collectionName);

        Unsplash.getInstance().getCollectionsPhoto(collectionId, 1, 30, new Unsplash.OnPhotosLoadedListener() {
            @Override
            public void onComplete(List<Photo> photos) {
                adapter.setAdapter(photos);
                showData();
            }

            @Override
            public void onError(String error) {
                showMessage();
                messageText.setText(error);
            }
        });

        return view;
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.GONE);
        collectionDetailsRecyclerView.setVisibility(View.GONE);
    }

    private void showMessage() {
        loadingProgressBar.setVisibility(View.GONE);
        messageText.setVisibility(View.VISIBLE);
        collectionDetailsRecyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        loadingProgressBar.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);
        collectionDetailsRecyclerView.setVisibility(View.VISIBLE);
    }

}
