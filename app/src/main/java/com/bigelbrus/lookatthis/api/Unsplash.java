package com.bigelbrus.lookatthis.api;

import android.util.Log;

import com.bigelbrus.lookatthis.api.endpoints.CollectionEndpoints;
import com.bigelbrus.lookatthis.api.endpoints.PhotoEndpoints;
import com.bigelbrus.lookatthis.models.Collection;
import com.bigelbrus.lookatthis.models.Photo;
import com.bigelbrus.lookatthis.models.SearchResults;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Unsplash {
    private static final String BASE_URL = "https://api.unsplash.com/";
    private static final String CLIENT_ID = "5bf61d94923089996e743459063eb478370731d7280a703d6a9952870a4a85f3";
    private static final Integer PER_PAGE = 30;
//    private static final String Secret_ID = "30d598382eb4cecc875f8d4400860170a671317350a6f399fb644d429270f98e";
    private static Unsplash mInstance;

    private PhotoEndpoints photoApi;
    private CollectionEndpoints collectionApi;

    private final String TAG = "TAG";

    public static Unsplash getInstance() {
        if (mInstance == null) {
            mInstance = new Unsplash(CLIENT_ID);
        }
        return mInstance;
    }

    private Unsplash (String clientId) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(clientId))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        photoApi = retrofit.create(PhotoEndpoints.class);
        collectionApi = retrofit.create(CollectionEndpoints.class);
    }

    public void getPhotos(Integer page, Integer perPage, Order order,final OnPhotosLoadedListener listener) {
        Call<List<Photo>> call = photoApi.getPhotos(page,perPage,order.getOrder());
        call.enqueue(getMultiplePhotoCallback(listener));
    }

    public void getPhoto(String id,final OnPhotoLoadedListener listener) {
        Call<Photo> call = photoApi.getPhoto(id);
        Log.d(TAG,"getPhoto" + call.toString());
        call.enqueue(getSinglePhotoCallback(listener));
    }

    public void getRandomPortraitPhoto (OnPhotoLoadedListener listener) {
        getRandomPhoto(Orientation.PORTRAIT,listener);
    }

    public void getRandomPhoto(Orientation orientation, OnPhotoLoadedListener listener) {
        Call<Photo> call = photoApi.getRandomPhoto(orientation.getOrientation());
        call.enqueue(getSinglePhotoCallback(listener));
    }

    public void searchPhotos(String query,Integer page, Integer perPage, Orientation orientation,OnSearchCompleteListener listener) {
        Call<SearchResults> call = photoApi.searchPhotos(query,page,perPage,orientation.getOrientation());
        call.enqueue(getSearchResultsCallback(listener));
    }

    public void searchPhotos(String query,OnSearchCompleteListener listener) {
        searchPhotos(query,1,PER_PAGE, Orientation.PORTRAIT,listener);
    }

    public void searchPhotos(String query, Integer page, OnSearchCompleteListener listener) {
        searchPhotos(query,page,PER_PAGE, Orientation.PORTRAIT,listener);
    }

    public interface OnPhotoLoadedListener {
        void onComplete(Photo photo);

        void onError(String error);
    }

    public interface OnPhotosLoadedListener {
        void onComplete(List<Photo> photos);

        void onError(String error);
    }

    public interface OnCollectionsLoadedListener {
        void onComplete(List<Collection> collections);

        void onError(String error);
    }

    public interface OnCollectionLoadedListener {
        void onComplete(Collection photos);

        void onError(String error);
    }

    public interface OnSearchCompleteListener {
        void onComplete(SearchResults results,String next);

        void onError(String error);
    }

    private Callback<Photo> getSinglePhotoCallback (final OnPhotoLoadedListener listener) {
        return new UnsplashCallback<Photo>() {
            @Override
            void onComplete(Photo response,String link) {
                listener.onComplete(response);
                Log.d(TAG,"onComplete");
            }

            @Override
            void onError(Call<Photo> call, String message) {
                listener.onError(message);
                Log.d(TAG,"onError " + message);

            }
        };
    }

    private Callback<List<Photo>> getMultiplePhotoCallback(final OnPhotosLoadedListener listener) {
        return new UnsplashCallback<List<Photo>>() {
            @Override
            void onComplete(List<Photo> response,String link) {
                listener.onComplete(response);
            }

            @Override
            void onError(Call<List<Photo>> call, String message) {
                listener.onError(message);
            }
        };
    }

    private Callback<SearchResults> getSearchResultsCallback(final OnSearchCompleteListener listener) {
        return new UnsplashCallback<SearchResults>() {
            @Override
            void onComplete(SearchResults response, String link) {
                listener.onComplete(response,link);
            }

            @Override
            void onError(Call<SearchResults> call, String message) {
                listener.onError(message);
            }
        };
    }

    private abstract class UnsplashCallback<T> implements Callback<T> {
        abstract void onComplete(T response,String next);

        abstract void onError(Call<T> call, String message);

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            int statusCode = response.code();
            String header = response.headers().get("link");
            Log.d("tag",(header == null)?"":header);
            if (statusCode == 200) {
                onComplete(response.body(),header);
            } else if (statusCode >= 400) {
                onError(call,String.valueOf(statusCode));
                if (statusCode == 401) {
                    Log.d(TAG,"Unauthorized, Check your client Id");
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            onError(call, t.getMessage());
        }

    }
}
