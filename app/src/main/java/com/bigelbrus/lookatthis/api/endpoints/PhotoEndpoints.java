package com.bigelbrus.lookatthis.api.endpoints;

import com.bigelbrus.lookatthis.models.Photo;
import com.bigelbrus.lookatthis.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PhotoEndpoints {

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id") String id);

    @GET("photos/")
    Call<List<Photo>> getPhotos(@Query("page") Integer page, @Query("per_page") Integer perPage,
                                @Query("order_by") String orderBy);

    @GET("search/photos")
    Call<SearchResults> searchPhotos(@Query("query") String query,
                                     @Query("page") Integer page,
                                     @Query("per_page") Integer perPage,
                                     @Query("orientation") String orientation);

    @GET("photos/random")
    Call<Photo> getRandomPhoto(@Query("orientation") String orientation);

}
