package com.disenkoart.howlongi.customView.timersView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.Tools;
import com.disenkoart.howlongi.adapters.TimersAdapter;
import com.disenkoart.howlongi.database.Timer;

import java.io.File;

/**
 * Created by Артём on 11.09.2016.
 */
public class FullTimersView extends ViewGroup {
    private LeftPanelTimerView mLeftPanelTimerView;
    private TimersView mTimersView;
    public boolean UpdateOn = false;

    OnChangeOpenStatusListener mOnChangeOpenStatusListener;

    final String TAG = getClass().getSimpleName();

    /* -- START CONSTRUCTORS -- */

    public FullTimersView(Context context) {
        super(context);
        init();
    }

    public FullTimersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullTimersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullTimersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /* -- END CONSTRUCTORS -- */


    /* START PUBLIC METHODS --*/

    public void onStopUpdate(){
        mTimersView.onStopUpdate();
    }

    public void onStartUpdate(){
        mTimersView.onStartUpdate();
    }

    public void setData(Timer data) {
        mTimersView.setTimerData(data);
        mLeftPanelTimerView.setGradientColor(data.getGradient());
        invalidate();
    }

    public void updateData(Timer data) {
        mTimersView.setTimerData(data);
        mLeftPanelTimerView.setGradientColor(data.getGradient());
        invalidate();
    }

    public View getLeftPanel() {
        return mLeftPanelTimerView;
    }

    public View getTimersView() {
        return mTimersView;
    }

    public void invalidateProgressBar() {
        if (!mLeftPanelTimerView.isOpen) {
            mTimersView.updateSecondsProgressBar();
        }
    }

    public void setOpenedLeftPanel(boolean isOpen, boolean isAnimation){
        mLeftPanelTimerView.setOpening(isOpen, isAnimation);
        mTimersView.setEnabledView(!isOpen);
    }

    public void setOnChangeButtonClickListener(OnClickListener listener){
        mLeftPanelTimerView.setChangeButtonClickListener(listener);
    }

    public void setOnDeleteButtonClickListener(OnClickListener listener){
        mLeftPanelTimerView.setDeleteButtonOnClickListener(listener);
    }

    public void setChangeSelectedTimerIndex(TimersView.OnChangeSelectedTimerListener listener){
        mTimersView.setChangeSelectedTimerIndex(listener);
    }

    public void setSelectedTimerIndex(int position){
        mTimersView.setSelectedTimerIndex(position);
    }

    OnClickListener onSendButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            setOpenedLeftPanel(false, false);
            mTimersView.updateSecondsProgressBar();
            File screenShot = Tools.saveScreenShotOfView(getContext(), FullTimersView.this);
            setOpenedLeftPanel(true, false);
            Uri uri = Uri.fromFile(screenShot);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    (getResources().getString(R.string.timers_view_title) + " " + mTimersView.getTimerHLIString() + "\n" +
                            getResources().getString(R.string.timer_share_url)).toUpperCase());
            getContext().startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.title_share_intent)));
        }
    };

    /* -- END PUBLIC METHODS -- */


    /* -- START PRIVATE METHODS -- */

    private void init() {
        mTimersView = new TimersView(getContext());
        mTimersView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLeftPanel();
            }
        });
        mTimersView.setOnClickProgressBarWhenDisabled(new TimersView.OnClickProgressBarWhenDisabled() {
            @Override
            public void OnClick() {
                hideLeftPanel();
            }
        });

        addView(mTimersView);
        mLeftPanelTimerView = new LeftPanelTimerView(getContext());
        mLeftPanelTimerView.setClickable(true);

        mLeftPanelTimerView.setChangeOpenStatusListener(new LeftPanelTimerView.OnChangeOpenStatusListener() {
            @Override
            public void onChangeOpenStatus(boolean isOpen) {
                mTimersView.setEnabledView(!isOpen);
                if (mOnChangeOpenStatusListener != null) {
                    mOnChangeOpenStatusListener.onChangeOpenStatus(mLeftPanelTimerView, isOpen);
                }
            }
        });
        mLeftPanelTimerView.setOpeningListener(new LeftPanelTimerView.OnOpeningListener() {
            @Override
            public void onOpening(float percentOpening) {
                mTimersView.setAlpha(1f - percentOpening * 0.8f);
            }
        });
        mLeftPanelTimerView.setSendButtonOnClickListener(onSendButtonClickListener);
        addView(mLeftPanelTimerView);
        setBackgroundColor(getResources().getColor(R.color.colorTopViews));
    }

    private void hideLeftPanel() {
        if (!mTimersView.isEnabledView()) {
            mLeftPanelTimerView.close();
            mTimersView.setAlpha(1f);
            mTimersView.setEnabledView(true);
            mTimersView.update();
        }
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        //todo размеры
        mLeftPanelTimerView.layout(0, 0, (int) (width * 0.345), b);
        mTimersView.layout((int) (mLeftPanelTimerView.getWidth() * LeftPanelTimerView.LEFT_PANEL_CLOSE_TRANSLATION) - 2, 0,
                width - 3, b);
    }

    /* -- END OVERRIDE METHODS -- */


    /* -- START INTERFACES -- */

    public static interface OnChangeOpenStatusListener {
        public void onChangeOpenStatus(View v, boolean isOpen);
    }

    public void setChangeOpenStatusListener(OnChangeOpenStatusListener listener) {
        mOnChangeOpenStatusListener = listener;
    }

    /* -- FINISH INTERFACES -- */
}
