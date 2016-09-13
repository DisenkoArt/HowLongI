package com.disenkoart.howlongi.customView.timersView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.database.Timer;

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

    public FullTimersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        if (UpdateOn) {
            mTimersView.updateTimerData(data);
        } else {
            mTimersView.setTimerData(data);
            UpdateOn = true;
        }
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
                mOnChangeOpenStatusListener.onChangeOpenStatus(mLeftPanelTimerView, isOpen);
            }
        });
        mLeftPanelTimerView.setOpeningListener(new LeftPanelTimerView.OnOpeningListener() {
            @Override
            public void onOpening(float percentOpening) {
                mTimersView.setAlpha(1f - percentOpening * 0.8f);
            }
        });
        addView(mLeftPanelTimerView);
        setBackground(getResources().getDrawable(R.drawable.background_button));
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
        int width = getWidth(), height = getHeight();
        mLeftPanelTimerView.layout(-1, 0, (int) (width * 0.345), height - 5);
        mTimersView.layout((int) (mLeftPanelTimerView.getWidth() * LeftPanelTimerView.LEFT_PANEL_CLOSE_TRANSLATION) - 2, 0,
                width - 3, height - 5);
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
