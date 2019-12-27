package com.bigelbrus.lookatthis.data;

import android.widget.ImageView;

import com.bigelbrus.lookatthis.api.Unsplash;
import com.bigelbrus.lookatthis.models.Photo;
import com.squareup.picasso.Picasso;

public class DataRepository {
    private static DataRepository mInstance;
    private static Unsplash unsplash;
    private ImageView randomPhotoImage;
    private Photo randomPhoto;

    public static DataRepository getInstance() {
        if (mInstance == null) {
            mInstance = new DataRepository();
        }
        return mInstance;
    }

    private DataRepository() {
        unsplash = Unsplash.getInstance();
    }

    public ImageView getRandomPhoto() {
        if (randomPhotoImage == null) {

            unsplash.getRandomPortraitPhoto(new Unsplash.OnPhotoLoadedListener() {
                @Override
                public void onComplete(Photo photo) {
                    Picasso.get().load(photo.getUrls().getRegular()).into(randomPhotoImage);
                    randomPhoto = photo;
                }

                @Override
                public void onError(String error) {

                }
            });
        }
        return randomPhotoImage;
    }

    public Photo getRandomPhotoDetails() {
        if (randomPhoto == null) {
            getRandomPhoto();
        }
        return randomPhoto;
    }

}
