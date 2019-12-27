package com.bigelbrus.lookatthis;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigelbrus.lookatthis.api.Quality;
import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.data.DataRepository;
import com.bigelbrus.lookatthis.models.Photo;
import com.bigelbrus.lookatthis.util.AppUtils;
import com.bigelbrus.lookatthis.util.DateUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PhotoOfADayFragment extends Fragment {
    private static final String KEY_PHOTO_DATE = "PHOTO_DATE";
    private static final String KEY_PHOTO_WIDTH = "PHOTO_WIDTH";
    private static final String KEY_PHOTO_HEIGHT = "PHOTO_HEIGHT";
    private static final String KEY_PHOTO_DESCRIPTION = "PHOTO_DESCRIPTION";
    private static final String KEY_PHOTO_LINK = "PHOTO_LINK";
    private static final String KEY_PHOTO_COLOR = "PHOTO_COLOR";
    private static final String DEFAULT_COLOR = "#6E633A";
    private static final String DEFAULT_LINK = "https://unsplash.com";
    private static final int REGULAR_WIDTH = 1440;
    private static final ZoneId ZONE_ID = ZoneId.of("+5");

    private ProgressBar photoLoading;
    private ImageView photoOfTheDay;
    private TextView loadingError;
    private int width;
    private int height;
    private String description;
    private String color;
    private String link;
    private LocalDateTime photoDownloadDate;
    private SharedPreferences preferences;

    public static PhotoOfADayFragment newInstance() {
        return new PhotoOfADayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_of_a_day, container, false);
        photoOfTheDay = view.findViewById(R.id.iv_photo_of_a_day);
        photoLoading = view.findViewById(R.id.photo_of_the_day_loading);
        loadingError = view.findViewById(R.id.error);
        showLoading();

        photoOfTheDay.setOnClickListener(v -> {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, PhotoDetailsFragment.getInstance(
//                            currentPhoto
                            width, height, description, link, color
                    ))
                    .addToBackStack(null)
                    .commit();

        });
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            preferences = activity.getPreferences(Context.MODE_PRIVATE);
//            preferences.edit().remove(KEY_PHOTO_DATE).apply();
            AppUtils.setActionBar(activity, view, R.string.photo_of_the_day, true);
        }
        if (preferences.contains(KEY_PHOTO_DATE)) {
            Log.d("tag", "Pref contains key");
            photoDownloadDate = DateUtils.parseDate(preferences.getString(KEY_PHOTO_DATE, ""));
            Log.d("tag", "Date of photo from pref " + DateUtils.formatDate(photoDownloadDate));
            if (photoDownloadDate.plusDays(1).isBefore(LocalDateTime.now(ZONE_ID))) {
                Log.d("tag", "Photo is old, will take new from net");
                loadPhoto();
            } else {
                Log.d("tag", "Photo is fresh, will take from pref");
                loadFromPref();
                prepareImageView();
                Picasso.get().load(link).into(photoOfTheDay);
                showData();
            }
        } else {
            Log.d("tag", "else");

            loadPhoto();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.photo_of_the_day_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            loadPhoto();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadPhoto() {
        Log.d("tag", "Load photo");
        Unsplash.getInstance().getRandomPortraitPhoto(new Unsplash.OnPhotoLoadedListener() {
            @Override
            public void onComplete(Photo photo) {
                String linkToScreenWidthPhoto = getLinkToPhoto(photo,Quality.TO_SCREEN_WIDTH,getActivity());
                Picasso.get().load(linkToScreenWidthPhoto).into(photoOfTheDay);
                photoDownloadDate = LocalDateTime.now(ZONE_ID);
                ZoneId.systemDefault();
                Log.d("tag", "Load photo time " + DateUtils.formatDate(photoDownloadDate));
                preferences.edit()
                        .putString(KEY_PHOTO_DATE, (DateUtils.formatDate(photoDownloadDate)))
                        .putInt(KEY_PHOTO_HEIGHT, photo.getHeight())
                        .putInt(KEY_PHOTO_WIDTH, photo.getWidth())
                        .putString(KEY_PHOTO_DESCRIPTION, photo.getDescription())
                        .putString(KEY_PHOTO_LINK, linkToScreenWidthPhoto)
                        .putString(KEY_PHOTO_COLOR, photo.getColor())
                        .apply();
                Log.d("tag", "Save data to pref");
                savePhoto(photo);
                prepareImageView();
                showData();
            }

            @Override
            public void onError(String error) {
                showError();
                loadingError.append("\n" + error);
            }
        });
    }

    private void savePhoto(Photo photo) {
        color = photo.getColor();
        width = photo.getWidth();
        height = photo.getHeight();
        description = photo.getDescription();
        link = photo.getUrls().getRegular();
    }

    private void loadFromPref() {
        Log.d("tag", "Take data from pref");
        width = preferences.getInt(KEY_PHOTO_WIDTH, 0);
        height = preferences.getInt(KEY_PHOTO_HEIGHT, 0);
        description = preferences.getString(KEY_PHOTO_DESCRIPTION, "");
        link = preferences.getString(KEY_PHOTO_LINK, DEFAULT_LINK);
        color = preferences.getString(KEY_PHOTO_COLOR, DEFAULT_COLOR);
    }

    private void showLoading() {
        photoLoading.setVisibility(View.VISIBLE);
        photoOfTheDay.setVisibility(View.GONE);
        loadingError.setVisibility(View.GONE);
    }

    private void showData() {
        photoLoading.setVisibility(View.GONE);
        photoOfTheDay.setVisibility(View.VISIBLE);
        loadingError.setVisibility(View.GONE);
    }

    private void showError() {
        photoLoading.setVisibility(View.GONE);
        photoOfTheDay.setVisibility(View.GONE);
        loadingError.setVisibility(View.VISIBLE);
    }

    private void prepareImageView() {
        photoOfTheDay.getLayoutParams().height = (height * REGULAR_WIDTH) / width;
        photoOfTheDay.getLayoutParams().width = REGULAR_WIDTH;
        photoOfTheDay.setBackgroundColor(Color.parseColor(color));
    }

    private String getLinkToPhoto(Photo photo, Quality quality, Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        String photoQualityAppend = "&fm=jpg&fit=crop&w=" + metrics.widthPixels + "&q=80&fit=max";
        switch (quality) {
            case RAW:
                return photo.getUrls().getRaw();
            case FULL:
                return photo.getUrls().getFull();
            case SMALL:
                return photo.getUrls().getSmall();
            case THUMB:
                return photo.getUrls().getThumb();
            case REGULAR:
                return photo.getUrls().getRegular();
            case TO_SCREEN_WIDTH:
            default:
                StringBuilder stringBuilder = new StringBuilder(photo.getUrls().getRaw()).append(photoQualityAppend);
                return stringBuilder.toString();
        }
    }

}
