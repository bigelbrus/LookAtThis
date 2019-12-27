package com.bigelbrus.lookatthis;


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

import com.bigelbrus.lookatthis.api.Orientation;
import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.models.SearchResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static final String KEY_SEARCH_TAG = "SEARCH_TAG";
    private RecyclerView searchRecyclerView;
    private EditText searchField;
    private ImageButton makeSearchButton;
    private TextView messageText;
    private ProgressBar loadingProgressBar;
    private SearchPhotosAdapter adapter;
    private SharedPreferences preferences;

    private static final int SPAN_COUNT = 3;


    public static SearchFragment getInstance() {
        return new SearchFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchRecyclerView = view.findViewById(R.id.rv_search);
        searchField = view.findViewById(R.id.search_field_edit_text);
        makeSearchButton = view.findViewById(R.id.make_search_button);
        messageText = view.findViewById(R.id.search_message_text);
        loadingProgressBar = view.findViewById(R.id.search_progress_bar);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        adapter = new SearchPhotosAdapter(getFragmentManager());
        searchRecyclerView.setAdapter(adapter);
        searchRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        showLoading();
        searchField.setText(preferences.getString(KEY_SEARCH_TAG, ""));
        makeSearch();
        makeSearchButton.setOnClickListener(v -> {
            makeSearch();
        });

        return view;
    }

    private void makeSearch() {
        if (searchField.getText().toString().equals("")) {
            showMessage();
            messageText.setText(getString(R.string.empty_search));
        } else {
            preferences.edit().putString(KEY_SEARCH_TAG, searchField.getText().toString()).apply();
            Unsplash.getInstance().searchPhotos(searchField.getText().toString(), new Unsplash.OnSearchCompleteListener() {
                @Override
                public void onComplete(SearchResults results, String link) {
                    if (results.getResults().isEmpty()) {
                        messageText.setText(getActivity().getText(R.string.no_result));
                        showMessage();
                    } else {
                        adapter.setAdapter(results.getResults());
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
