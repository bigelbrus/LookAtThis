package com.bigelbrus.lookatthis;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bigelbrus.lookatthis.data.DataRepository;
import com.bigelbrus.lookatthis.models.Photo;
import com.bigelbrus.lookatthis.util.AppUtils;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoDetailsFragment extends Fragment {
    private static final String KEY_COLOR = "COLOR";
    private static final String KEY_WIDTH = "WIDTH";
    private static final String KEY_HEIGHT = "HEIGHT";
    private static final String KEY_DESCRIPTION = "DESCRIPTION";
    private static final String KEY_LINK = "LINK";
    private static final int REGULAR_WIDTH = 1080;

    private String color;
    private int width;
    private int height;
    private String description;
    private String link;


    public static PhotoDetailsFragment getInstance(
            Integer width, Integer height, String description, String link, String color
//            Photo photo
    ) {
        Bundle args = new Bundle();
//        args.putParcelable(KEY_PHOTO,photo);
        args.putString(KEY_COLOR, color);
        args.putInt(KEY_WIDTH, width);
        args.putInt(KEY_HEIGHT, height);
        args.putString(KEY_DESCRIPTION, description);
        args.putString(KEY_LINK, link);
        PhotoDetailsFragment fragment = new PhotoDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_details, container, false);
        TextView linkToGoodQualityImage = view.findViewById(R.id.tv_link);
        getActivity().findViewById(R.id.bottom_panel).setVisibility(View.GONE);
        if (getArguments() != null) {
            Bundle args = getArguments();
            color = args.getString(KEY_COLOR);
            width = args.getInt(KEY_WIDTH);
            height = args.getInt(KEY_HEIGHT);
            description = args.getString(KEY_DESCRIPTION);
            link = args.getString(KEY_LINK);
        }
        ImageView detailsPhoto = view.findViewById(R.id.iv_photo_details);
        TextView widthTextView = view.findViewById(R.id.iv_width);
        TextView heightTextView = view.findViewById(R.id.iv_height);
        TextView descriptionTextView = view.findViewById(R.id.iv_description);
        detailsPhoto.getLayoutParams().width = REGULAR_WIDTH;
        detailsPhoto.getLayoutParams().height = (REGULAR_WIDTH * height) / width;
        detailsPhoto.setBackgroundColor(Color.parseColor(color));

        widthTextView.setText(String.valueOf(width));
        heightTextView.setText(String.valueOf(height));
        descriptionTextView.setText(description);
        Picasso.get().load(link).into(detailsPhoto);
        linkToGoodQualityImage.setText(getUnderlineSpan(R.string.link_to_good_quality));
        linkToGoodQualityImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
        });
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        AppUtils.setActionBar(activity, view, R.string.details, true);
        return view;
    }

    private Spannable getUnderlineSpan(int textRes) {
        Spannable text = new SpannableString(getString(textRes));
        text.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_light)),
                0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_refresh);
        if (item != null) {
            item.setVisible(false);
        }
    }
}
