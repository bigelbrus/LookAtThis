package com.bigelbrus.lookatthis;

import android.app.Application;

import com.bigelbrus.lookatthis.api.Unsplash;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class App extends Application {
    @Override
    public void onCreate() {
        Unsplash.getInstance();
        AndroidThreeTen.init(this);
        super.onCreate();
    }
}
