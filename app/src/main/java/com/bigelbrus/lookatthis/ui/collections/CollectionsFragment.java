package com.bigelbrus.lookatthis.ui.collections;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigelbrus.lookatthis.PaginationScrollListener;
import com.bigelbrus.lookatthis.R;
import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.models.Collection;
import com.bigelbrus.lookatthis.util.AppUtils;

import java.util.List;

public class CollectionsFragment extends Fragment {
    private static final int PAGE_START = 1;
    private static final int PER_PAGE = 10;
    private Unsplash unsplash;
    private int currentPage = PAGE_START;
    private boolean hasNextPage;
    private TextView messageText;
    private ProgressBar loadingProgressBar;

    private CollectionsAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    private RecyclerView collectionsRecyclerView;
    private boolean isLastPage;
    private boolean isLoading;

    public static final int SPAN_COUNT = 2;


    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        unsplash = Unsplash.getInstance();
        collectionsRecyclerView = view.findViewById(R.id.collections_recycler_view);
        adapter = new CollectionsAdapter(getFragmentManager());
        collectionsRecyclerView.setAdapter(adapter);
        layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        collectionsRecyclerView.setLayoutManager(layoutManager);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        AppUtils.setActionBar(activity, view, R.string.collections, false);
        loadingProgressBar = view.findViewById(R.id.collection_progress_bar);
        messageText = view.findViewById(R.id.collection_message_text);
        showLoading();
        loadFirstPage();
        return view;
    }

    public void loadFirstPage() {
        unsplash.getCollections(PAGE_START, PER_PAGE, new Unsplash.OnCollectionsLoadedListener() {
            @Override
            public void onComplete(List<Collection> collections, boolean hasNext) {
                adapter.addAll(collections);
                hasNextPage = hasNext;
                if (hasNextPage) {
                    adapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
                showData();
            }

            @Override
            public void onError(String error) {
                showMessage();
                messageText.setText(error);
            }
        });
        collectionsRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadNextPage() {
        Unsplash.getInstance().getCollections(currentPage, PER_PAGE, new Unsplash.OnCollectionsLoadedListener() {
            @Override
            public void onComplete(List<Collection> collections, boolean hasNext) {
                adapter.removeLoadingFooter();
                isLoading = false;
                adapter.addAll(collections);
                hasNextPage = hasNext;
                if (hasNextPage) {
                    adapter.addLoadingFooter();
                } else {
                    isLastPage = true;
                }
                showData();
            }

            @Override
            public void onError(String error) {
                showMessage();
                messageText.setText(error);
            }
        });
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.GONE);
        collectionsRecyclerView.setVisibility(View.GONE);
    }

    private void showMessage() {
        loadingProgressBar.setVisibility(View.GONE);
        messageText.setVisibility(View.VISIBLE);
        collectionsRecyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        loadingProgressBar.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);
        collectionsRecyclerView.setVisibility(View.VISIBLE);
    }

}
