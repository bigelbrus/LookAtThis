package com.bigelbrus.lookatthis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.models.Photo;
import com.squareup.picasso.Picasso;
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker;
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto;
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_ACTIVE_BUTTON = "ACTIVE_BUTTON";

    private Button activeButton;
    private Button searchButton;
    private Button photoOfTheDayButton;
    private Button collectionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = findViewById(R.id.search_button);
        photoOfTheDayButton = findViewById(R.id.picture_of_a_day_button);
        collectionsButton = findViewById(R.id.collections_button);

        if (savedInstanceState != null) {
            activeButton = findViewById(savedInstanceState.getInt(KEY_ACTIVE_BUTTON));
        } else {
            activeButton = photoOfTheDayButton;
        }
        activeButton.setActivated(true);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, PhotoOfADayFragment.newInstance()).commit();
        }
    }

    public void onBottomButtonPress(View view) {
        switch (view.getId()) {
            case (R.id.search_button):
                if (activeButton != searchButton) {
                    activateButton(searchButton);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SearchFragment.getInstance()).commit();
                }
                break;
            case (R.id.picture_of_a_day_button):
                if (activeButton != photoOfTheDayButton) {
                    activateButton(photoOfTheDayButton);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PhotoOfADayFragment.newInstance()).commit();
                }
                break;
            case (R.id.collections_button):
                if (activeButton != collectionsButton) {
                    activateButton(collectionsButton);
//                transaction.replace(R.id.fragment_container,CollectionsFragment.getInstance());
                }
                break;
        }
    }

    private void activateButton(Button b) {
        activeButton.setActivated(false);
        b.setActivated(true);
        activeButton = b;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            findViewById(R.id.bottom_panel).setVisibility(View.VISIBLE);
            onBackPressed();
        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_ACTIVE_BUTTON, activeButton.getId());
        super.onSaveInstanceState(outState);
    }
}
