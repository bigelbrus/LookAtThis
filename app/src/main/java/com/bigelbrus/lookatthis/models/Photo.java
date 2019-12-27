package com.bigelbrus.lookatthis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("downloads")
    @Expose
    private Integer downloads;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("liked_by_user")
    @Expose
    private Boolean likedByUser;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("urls")
    @Expose
    private Urls urls;
    public final static Parcelable.Creator<Photo> CREATOR = new Creator<Photo>() {


        public Photo createFromParcel(Parcel in) {
            Photo instance = new Photo();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.color = ((String) in.readValue((String.class.getClassLoader())));
            instance.downloads = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.likes = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.likedByUser = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.description = ((String) in.readValue(String.class.getClassLoader()));
            instance.urls = ((Urls) in.readValue((Urls.class.getClassLoader())));
            return instance;
        }

        public Photo[] newArray(int size) {
            return (new Photo[size]);
        }

    };

    /**
     * No args constructor for use in serialization
     */
    public Photo() {
    }

    /**
     * @param currentUserCollections
     * @param urls
     * @param width
     * @param downloads
     * @param id
     * @param updatedAt
     * @param height
     * @param color
     * @param createdAt
     * @param likes
     * @param description
     * @param likedByUser
     */
    public Photo(String id, String createdAt, String updatedAt, Integer width, Integer height, String color, Integer downloads, Integer likes, Boolean likedByUser, String description, List<Collection> currentUserCollections, Urls urls) {
        super();
        this.id = id;
        this.width = width;
        this.height = height;
        this.color = color;
        this.downloads = downloads;
        this.likes = likes;
        this.likedByUser = likedByUser;
        this.description = description;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Photo withId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Photo withDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Photo withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Photo withHeight(Integer height) {
        this.height = height;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Photo withColor(String color) {
        this.color = color;
        return this;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public Photo withDownloads(Integer downloads) {
        this.downloads = downloads;
        return this;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Photo withLikes(Integer likes) {
        this.likes = likes;
        return this;
    }

    public Boolean getLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(Boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public Photo withLikedByUser(Boolean likedByUser) {
        this.likedByUser = likedByUser;
        return this;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Photo withUrls(Urls urls) {
        this.urls = urls;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(color);
        dest.writeValue(downloads);
        dest.writeValue(likes);
        dest.writeValue(likedByUser);
        dest.writeValue(description);
        dest.writeValue(urls);
    }

    public int describeContents() {
        return 0;
    }
}
