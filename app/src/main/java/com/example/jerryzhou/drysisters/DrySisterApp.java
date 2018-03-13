package com.example.jerryzhou.drysisters;

import android.app.Application;

public class DrySisterApp extends Application{

    private static DrySisterApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

    public static DrySisterApp getContext() {
        return context;
    }
}
