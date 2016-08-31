package com.disenkoart.howlongi.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.disenkoart.howlongi.Data.GradientColor;
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

    private GradientColor mRectangleColor = GradientColor.getDefaultGradient();

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ShapeDrawable rectangle = (ShapeDrawable) getResources().getDrawable(R.drawable.designer_rectangle);
        rectangle.getPaint().setShader(new LinearGradient(0, 0, 0, rectangleImage.getHeight(),
                new int[]{mRectangleColor.getStartColor(), mRectangleColor.getEndColor()},
                new float[]{0, 1f}, Shader.TileMode.REPEAT));
        rectangleImage.setImageDrawable(rectangle);
    }

    public void setRectangleColor(GradientColor color) {
        mRectangleColor = color;
    }

    /* -- END PUBLIC METHODS -- */

}
