package com.disenkoart.howlongi.customView;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.R;

/**
 * Created by Артём on 01.10.2016.
 */
public class StyleSnackBar {
    public static Snackbar createSnackBar(View view, CharSequence text, int duration){
        Snackbar snackbar = Snackbar.make(view, text, duration);
        TextView titleText = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        TextView actionText = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);
//            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/font_file.ttf");
        titleText.setTypeface(FontManager.UNI_SANS, Typeface.BOLD);
        actionText.setTypeface(FontManager.UNI_SANS, Typeface.BOLD);
        return snackbar;
    }
}
