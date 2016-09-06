package com.disenkoart.howlongi.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Артём on 16.08.2016.
 */
public class AboutView extends LinearLayout {

    @BindView(R.id.profession_title_about_view)
    TextView professionTitle;

    @BindView(R.id.name_title_about_view)
    TextView nameTitle;

    @BindView(R.id.image_about_view)
    ImageView developerImage;

    @BindView(R.id.rectangle_image_about_view)
    ImageView rectangleImage;

    /* -- START IMPLEMENTS CONSTRUCTORS -- */

    public AboutView(Context context) {
        super(context);
        init();
    }

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AboutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AboutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /* -- END IMPLEMENTS CONSTRUCTORS -- */

    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /* -- END OVERRIDE METHODS -- */

    /* -- START PRIVATE METHODS -- */

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.about_view, this);
        ButterKnife.bind(this);
        initTextViews();
    }

    private void initTextViews() {
        professionTitle.setTypeface(FontManager.UNI_SANS);
        nameTitle.setTypeface(FontManager.UNI_SANS);
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START PUBLIC METHODS -- */

    public void setDeveloperImage(int resId) {
        developerImage.setImageResource(resId);
    }

    public void setProfessionTitle(String professionTitle) {
        this.professionTitle.setText(professionTitle);
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle.setText(nameTitle);
    }

    public void setRectangle(int shape) {
        GradientDrawable rectangle = (GradientDrawable) getResources().getDrawable(shape);
        rectangleImage.setImageDrawable(rectangle);
    }

        /* -- END PUBLIC METHODS -- */


}
