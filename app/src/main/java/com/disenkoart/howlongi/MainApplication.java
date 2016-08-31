package com.disenkoart.howlongi;

import android.app.Application;

/**
 * Created by Артём on 16.08.2016.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontManager.init(this);
    }
}
