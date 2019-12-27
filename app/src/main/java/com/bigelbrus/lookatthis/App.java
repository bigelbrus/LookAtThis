package com.bigelbrus.lookatthis;

import android.app.Application;

import com.bigelbrus.lookatthis.api.Unsplash;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class App extends Application {
    private static final String ACCESS_KEY = "5bf61d94923089996e743459063eb478370731d7280a703d6a9952870a4a85f3";
    private static final String SECRET_KEY = "30d598382eb4cecc875f8d4400860170a671317350a6f399fb644d429270f98e";
    @Override
    public void onCreate() {
        Unsplash.getInstance();
        AndroidThreeTen.init(this);
        super.onCreate();
    }
}
