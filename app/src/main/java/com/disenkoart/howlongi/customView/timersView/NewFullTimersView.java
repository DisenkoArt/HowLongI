package com.disenkoart.howlongi.customView.timersView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.database.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Артём on 11.09.2016.
 */
public class NewFullTimersView extends CardView {

    @BindView(R.id.timers_views)
    TimersView mTimersView;

    @BindView(R.id.left_panel)
    LeftPanelTimerView mLeftPanel;

    public boolean UpdateOn = false;

    OnChangeOpenStatusListener mOnChangeOpenStatusListener;

    final String TAG = getClass().getSimpleName();
    /* -- START CONSTRUCTORS -- */

    public NewFullTimersView(Context context) {
        super(context);
        init();
    }

    public NewFullTimersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewFullTimersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        mLeftPanel.setGradientColor(data.getGradient());
        invalidate();
    }

    public void updateData(Timer data) {
        mTimersView.setTimerData(data);
        mLeftPanel.setGradientColor(data.getGradient());
        invalidate();
    }

    public View getLeftPanel() {
        return mLeftPanel;
    }

    public View getTimersView() {
        return mTimersView;
    }

    public void invalidateProgressBar() {
        if (!mLeftPanel.isOpen) {
            mTimersView.updateSecondsProgressBar();
        }
    }

    public void setOpenedLeftPanel(boolean isOpen, boolean isAnimation){
        mLeftPanel.setOpening(isOpen, isAnimation);
        mTimersView.setEnabledView(!isOpen);
    }

    /* -- END PUBLIC METHODS -- */

    /* -- START PRIVATE METHODS -- */

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.full_timers_view, this);
        ButterKnife.bind(this);

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

        mLeftPanel.setClickable(true);

        mLeftPanel.setChangeOpenStatusListener(new LeftPanelTimerView.OnChangeOpenStatusListener() {
            @Override
            public void onChangeOpenStatus(boolean isOpen) {
                mTimersView.setEnabledView(!isOpen);
                mOnChangeOpenStatusListener.onChangeOpenStatus(mLeftPanel, isOpen);
            }
        });
        mLeftPanel.setOpeningListener(new LeftPanelTimerView.OnOpeningListener() {
            @Override
            public void onOpening(float percentOpening) {
                mTimersView.setAlpha(1f - percentOpening * 0.8f);
            }
        });
        setBackground(getResources().getDrawable(R.drawable.background_button));
    }


    private void hideLeftPanel() {
        if (!mTimersView.isEnabledView()) {
            mLeftPanel.close();
            mTimersView.setAlpha(1f);
            mTimersView.setEnabledView(true);
            mTimersView.update();
        }
    }
    /* -- END PRIVATE METHODS -- */

    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
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
