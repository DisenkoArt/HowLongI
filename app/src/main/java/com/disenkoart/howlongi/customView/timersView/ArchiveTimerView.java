package com.disenkoart.howlongi.customView.timersView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.customView.CustomTextView;
import com.disenkoart.howlongi.data.DateUtils;
import com.disenkoart.howlongi.database.Timer;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 30.09.2016.
 */
public class ArchiveTimerView extends LinearLayout{

    @BindView(R.id.topTitleArchiveView)
    CustomTextView mTopTitle;

    @BindView(R.id.bottomTitleArchiveView)
    CustomTextView mBottomTitle;

    @BindView(R.id.separatorArchiveView)
    ImageView mSeparatorImageView;

    @BindView(R.id.resetButtonArchiveView)
    ImageView mResetButton;

    @BindView(R.id.deleteButtonArchiveView)
    ImageView mDeleteButton;

    private Timer mTimerData;
    private String mStartDateTimeString;
    private Locale mCurrentLocale;

    public ArchiveTimerView(Context context) {
        super(context);
        init();
    }

    public ArchiveTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArchiveTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArchiveTimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.archive_timer_layout, this);
        ButterKnife.bind(this);
        mStartDateTimeString = getResources().getString(R.string.start_datetime_string);
        mCurrentLocale = getResources().getConfiguration().locale;
        mTopTitle.setTextGravity(Gravity.LEFT|Gravity.TOP);
        mTopTitle.setTextColor(getResources().getColor(R.color.colorText));
        mBottomTitle.setTextGravity(Gravity.LEFT|Gravity.TOP);
        mBottomTitle.setTextColor(getResources().getColor(R.color.colorText));
    }

    private void changeSeparatorColor(){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{mTimerData.getGradient().getStartColor(), mTimerData.getGradient().getEndColor()});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mSeparatorImageView.setImageDrawable(gradientDrawable);
    }

    public void setTimerData(Timer timerData) {
        mTimerData = timerData;
        changeSeparatorColor();
        mTopTitle.resetText(getResources().getString(R.string.timers_view_title) + " " + mTimerData.getHliString());
        mBottomTitle.resetText(String.format("%s: %s", mStartDateTimeString, String.format("%s: %s",
                mStartDateTimeString, DateUtils.convertDateToLocaleString(mCurrentLocale, mTimerData.getStartDateTime()))));
    }

    public void setDeleteButtonOnClickListener(OnClickListener listener) {
        mDeleteButton.setOnClickListener(listener);
    }

    public void setResetButtonClickListener(OnClickListener listener) {
        mResetButton.setOnClickListener(listener);
    }


}
