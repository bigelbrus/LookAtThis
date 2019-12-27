package com.bigelbrus.lookatthis.ui.search;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigelbrus.lookatthis.PaginationScrollListener;
import com.bigelbrus.lookatthis.R;
import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.models.SearchResults;


public class SearchFragment extends Fragment {
    private static final String KEY_SEARCH_TAG = "SEARCH_TAG";
    private RecyclerView searchRecyclerView;
    private EditText searchField;
    private ImageButton makeSearchButton;
    private TextView messageText;
    private ProgressBar loadingProgressBar;
    private SearchPhotosAdapter adapter;
    private SharedPreferences preferences;
    private StaggeredGridLayoutManager layoutManager;

    private static final int PAGE_START = 1;
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPages;
    private int currentPage = PAGE_START;

    public static final int SPAN_COUNT = 3;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchRecyclerView = view.findViewById(R.id.rv_search);
        searchField = view.findViewById(R.id.search_field_edit_text);
        makeSearchButton = view.findViewById(R.id.make_search_button);
        messageText = view.findViewById(R.id.search_message_text);
        loadingProgressBar = view.findViewById(R.id.search_progress_bar);
        if (getActivity() != null) {
            preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        }
        layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT,StaggeredGridLayoutManager.VERTICAL);
        adapter = new SearchPhotosAdapter(getFragmentManager());
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(layoutManager);

        showLoading();
        searchField.setText(preferences.getString(KEY_SEARCH_TAG, ""));
        makeSearch();
        makeSearchButton.setOnClickListener(v -> {
            makeSearch();
        });

        return view;
    }

    private void loadFirstPage() {
        if (searchField.getText().toString().equals("")) {
            showMessage();
            messageText.setText(getString(R.string.empty_search));
        } else {
            preferences.edit().putString(KEY_SEARCH_TAG, searchField.getText().toString()).apply();
            Unsplash.getInstance().searchPhotos(searchField.getText().toString(),currentPage, new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results) {
                    if (results.getResults().isEmpty()) {
                        messageText.setText(getActivity().getText(R.string.no_result));
                        showMessage();
                    } else {
                        adapter.clear();
                        adapter.addAll(results.getResults());
                        totalPages = results.getTotalPages();
                        currentPage = PAGE_START;
                        if (currentPage <= totalPages) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                        showData();
                    }
                }

                @Override
                public void onError(String error) {
                    showMessage();
                    messageText.setText(error);
                }
            });
        }
    }

    private void loadNextPage() {
        Unsplash.getInstance().searchPhotos(searchField.getText().toString(), currentPage, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                if (results.getResults().isEmpty()) {
                    messageText.setText(getActivity().getText(R.string.no_result));
                    showMessage();
                    totalPages = results.getTotalPages();
                } else {
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    adapter.addAll(results.getResults());
                    if (currentPage != totalPages) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                    showData();
                }

            }

            @Override
            public void onError(String error) {
                showMessage();
                messageText.setText(error);
            }
        });
    }

    private void makeSearch() {
        loadFirstPage();
        searchRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return totalPages;
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

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.GONE);
    }

    private void showMessage() {
        loadingProgressBar.setVisibility(View.GONE);
        messageText.setVisibility(View.VISIBLE);
        searchRecyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        loadingProgressBar.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.VISIBLE);
    }

}
