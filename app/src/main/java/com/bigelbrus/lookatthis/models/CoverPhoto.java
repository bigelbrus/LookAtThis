package com.bigelbrus.lookatthis.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CoverPhoto implements Parcelable {

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
    @SerializedName("urls")
    @Expose
    private Urls urls;
    public final static Parcelable.Creator<CoverPhoto> CREATOR = new Creator<CoverPhoto>() {


        public CoverPhoto createFromParcel(Parcel in) {
            CoverPhoto instance = new CoverPhoto();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.color = ((String) in.readValue((String.class.getClassLoader())));
            instance.urls = ((Urls) in.readValue((Urls.class.getClassLoader())));
            return instance;
        }

        public CoverPhoto[] newArray(int size) {
            return (new CoverPhoto[size]);
        }

    }
            ;

    /**
     * No args constructor for use in serialization
     *
     */
    public CoverPhoto() {
    }

    /**
     *
     * @param id
     * @param height
     * @param color
     * @param likes
     * @param width
     * @param likedByUser
     */
    public CoverPhoto(String id, Integer width, Integer height, String color, Integer likes, Boolean likedByUser) {
        super();
        this.id = id;
        this.width = width;
        this.height = height;
        this.color = color;
        this.urls = urls;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CoverPhoto withId(String id) {
        this.id = id;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public CoverPhoto withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public CoverPhoto withHeight(Integer height) {
        this.height = height;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CoverPhoto withColor(String color) {
        this.color = color;
        return this;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public CoverPhoto withUrls(Urls urls) {
        this.urls = urls;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(color);
        dest.writeValue(urls);
    }

    public int describeContents() {
        return  0;
    }

}
