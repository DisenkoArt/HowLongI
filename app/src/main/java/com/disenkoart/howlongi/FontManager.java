package com.disenkoart.howlongi;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Артём on 16.08.2016.
 */
public class FontManager {
    public static Typeface UNI_SANS;

    public static void init(){
        UNI_SANS = Typeface.createFromAsset(MainApplication.getInstance().getApplicationContext().getAssets(), "Uni Sans Thin.otf");
        //UNI_SANS = Typeface.createFromAsset(context.getAssets(), "BEBAS.ttf");
    }
}
