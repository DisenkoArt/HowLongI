package com.disenkoart.howlongi.customView.timersView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.disenkoart.howlongi.AnimationUtils;
import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.customView.CustomTextView;
import com.disenkoart.howlongi.customView.circleProgressBar.CircleProgressBar;
import com.disenkoart.howlongi.customView.circleProgressBar.SelectEnum;
import com.disenkoart.howlongi.data.DateItem;
import com.disenkoart.howlongi.data.DateTimeUnits;
import com.disenkoart.howlongi.data.DateUtils;
import com.disenkoart.howlongi.database.Timer;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Артём on 11.09.2016.
 */
public class TimersView extends ViewGroup {
    /**
     * Список, содержащий информацию о каждой единице измерения времени.
     */
    private ArrayList<DateItem> mDateItems;

    /**
     * Строка "время старта".
     */
    @BindString(R.string.startDatetimeString)
    String mStartDateTimeString;

    /**
     * Локация запуска приложения, используется для определения формата представления времени
     */
    private Locale mCurrentLocale;

    /**
     * Индекс выбранного таймера.
     */
    private int mSelectedTimerIndex = -1;

    /**
     * Верхний заголовок, где пишется "HOW LONG I ...".
     */
    private CustomTextView mTopTitleTextView;

    /**
     * Нижний заголовок, где пишется время старта таймера.
     */
    private TextView mBottomTitleTextView;

    /**
     * Информация о таймере.
     */
    private Timer mTimerData;

    /**
     * Определяет активность View.
     */
    private boolean isEnabled = true;

    private OnChangeSelectedTimerListener mOnChangeSelectedTimerListener;
    private OnClickProgressBarWhenDisabled mOnClickProgressBarWhenDisabled;

    /**
     * Таймер, используемый для анимирования пройденного времени в верхнем заголовке.
     */
    private java.util.Timer mTimer;
    private Handler mUpdateUITopTitleHandler = new Handler();
    private TimerTask mUpdateTitleTask;

    /* -- START IMPLEMENTS CONSTRUCTORS -- */

    public TimersView(Context context) {
        super(context);
        init();
    }

    public TimersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* -- END IMPLEMENTS CONSTRUCTORS -- */

    /* -- START PUBLIC METHODS -- */

    public void onStopUpdate(){
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        if (mUpdateTitleTask != null){
            mUpdateTitleTask.cancel();
        }
        mTopTitleTextView.onStopUpdate();
    }

    public void onStartUpdate(){
        if (mSelectedTimerIndex > 0){
            scheduleTopTitleUpdate();
        }
        mTopTitleTextView.resetText(getTopTitle());
    }

    /**
     * Определяет доступности View.
     * @param enabled Флаг доступности view.
     */
    public void setEnabledView(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * Возвращает флаг доступности View.
     * @return Флаг доступности View.
     */
    public boolean isEnabledView() {
        return isEnabled;
    }

    /**
     * Задает информацию о таймере.
     * @param timerData Информация о таймере.
     */
    public void setTimerData(Timer timerData) {
        mTimerData = timerData;
        mTopTitleTextView.resetText(getTopTitle());
        update();
    }

    /**
     * Обновляет информацию о таймере.
     * @param timerData Новая информация о таймере.
     */
    public void updateTimerData(Timer timerData) {
        mTimerData = timerData;
        update();
    }

    /**
     * Полностью обновляет View.
     */
    public void update() {
        updateTopTitle();
        updateBottomTitle();
        updateAllProgressBars();
        for (int i = 0; i < mDateItems.size(); i++) {
            CircleProgressBar circleProgressBar = (CircleProgressBar) getChildAt(i);
            circleProgressBar.setGradientColor(mTimerData.getGradient());
        }
    }

    /**
     * Устанавливает индекс выбранной View.
     * @param index Индкс выбранного таймера.
     */
    public void setSelectedTimerIndex(int index) {
        mSelectedTimerIndex = index;
        for (int i = 0; i < mDateItems.size(); i++) {
            ((CircleProgressBar) getChildAt(i)).setSelectionView(mSelectedTimerIndex == -1 ? SelectEnum.NOT_SELECT :
                    mSelectedTimerIndex == i ? SelectEnum.SELECT : SelectEnum.SELECT_OTHER);
        }
        if (mSelectedTimerIndex > 0){
            scheduleTopTitleUpdate();
        } else {
            onStopUpdate();
        }
    }

    /**
     * Обновляет значение прогресс-бара, отвечающий за подсчет минут.
     */
    public void updateSecondsProgressBar() {
        double percent = DateUtils.getSecondsPeriod(mTimerData.getStartDateTime());
        if (percent >= 0) {
            percent++;
            updateProgressBar((CircleProgressBar) getChildAt(4), (float) percent);
        }
    }


    /* -- END PUBLIC METHODS -- */

    /* -- START PRIVATE METHODS -- */

    /**
     * Инициализирует View.
     */
    private void init() {
        ButterKnife.bind(this);
        mDateItems = DateTimeUnits.getDateTypeList();
        setWillNotDraw(false);
        initProgressBars();
        setBackgroundColor(getResources().getColor(R.color.colorTopViews));
        mTopTitleTextView = new CustomTextView(getContext(), getResources().getColor(R.color.pbCenterTextColor));
        mBottomTitleTextView = new TextView(getContext());
        mBottomTitleTextView.setTextColor(getResources().getColor(R.color.colorTimersViewBottomTitle));
        mBottomTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.bottom_title_text_size));
        mBottomTitleTextView.setGravity(Gravity.CENTER);
        mBottomTitleTextView.setLines(1);
        mBottomTitleTextView.setTypeface(FontManager.UNI_SANS);
        addView(mTopTitleTextView);
        addView(mBottomTitleTextView);
        mCurrentLocale = getResources().getConfiguration().locale;
    }

    private void initProgressBars(){
        for (int i = 0; i < mDateItems.size(); i++) {
            CircleProgressBar circleProgressBar = new CircleProgressBar(getContext());
            circleProgressBar.setDateTypeParameters(mDateItems.get(i));
            circleProgressBar.setTag(i);
            circleProgressBar.setOnClickListener(progressBarOnClickListener);
            addView(circleProgressBar);
        }
    }

    /**
     * Обновляет все прогресс-бары.
     */
    private void updateAllProgressBars() {
        int[] valuesProgressBar = DateUtils.getWholeTimeValues(mTimerData.getStartDateTime());
        float[] completionProgressBar = DateUtils.getCompletionDateValues(mTimerData.getStartDateTime());
        for (int i = 0; i < mDateItems.size(); i++) {
            CircleProgressBar circleProgressBar = (CircleProgressBar) getChildAt(i);
            circleProgressBar.setValue(valuesProgressBar[i]);
            circleProgressBar.setProgressValue(completionProgressBar[i]);
        }
    }

    //TODO: оптимизировать неявную рекурсию в двух методах, выполняется несколько раз расчет прогресса

    /**
     * Обновляет значение прогресс-бара.
     * @param progressBar Прогресс-бар.
     * @param progressValue Новое значение прогресс-бара.
     */
    private void updateProgressBar(CircleProgressBar progressBar, float progressValue){
        if (progressValue >= 0 && progressValue <= 1 && (int) progressBar.getProgress() + 1 >= progressBar.getMaxProgressValue()) {
            updateNextProgressBars(progressBar);
            progressBar.setValue(DateUtils.getWholeTimeValue(mTimerData.getStartDateTime(), progressBar.getDataType()));
            AnimationUtils.createProgressBarAnimation(progressBar, progressValue, -progressBar.getMaxProgressValue(), "progressValue").start();
        } else {
            if (progressValue < progressBar.getProgress()) {
                progressBar.setProgressValue(progressValue);
                return;
            }
            AnimationUtils.createProgressBarAnimation(progressBar, progressValue, progressBar.getProgress(), "progressValue").start();
        }
    }

    private void updateNextProgressBars(CircleProgressBar progressBar){
        int dateUnit = progressBar.getDataType();
        for (int i = 0; i < mDateItems.size(); i++) {
            CircleProgressBar nextProgressBar = (CircleProgressBar) getChildAt(i);
            if (nextProgressBar.getTimerDateUnit() == dateUnit){
                progressBar.setUpdatingView(false);
                updateProgressBar(nextProgressBar, DateUtils.getCompletionDateValue(mTimerData.getStartDateTime(),
                        nextProgressBar.getDataType()));
                progressBar.setUpdatingView(true);
            }
        }
    }

    /**
     * Обновляет текст нижнего заголовка.
     */
    private void updateBottomTitle() {
        String bottomTitle = String.format("%s: %s", mStartDateTimeString, DateUtils.convertDateToLocaleString(mCurrentLocale, mTimerData.getStartDateTime()));
        if (!bottomTitle.equals(mBottomTitleTextView.getText())) {
            mBottomTitleTextView.setText(bottomTitle);
        }
    }

    /**
     * Запускает непрерывный цуикл, который обновляет верхний заголовок.
     */
    private void scheduleTopTitleUpdate() {
        if (mUpdateTitleTask != null) {
            mUpdateTitleTask.cancel();
        }
        long interval = DateTimeUnits.getIntervalUpdateByUnit(((CircleProgressBar) getChildAt(mSelectedTimerIndex)).getDataType());
        mUpdateTitleTask = new TimerTask() {
            @Override
            public void run() {
                updateTopTitle();
            }
        };
        mTimer = new java.util.Timer();
        mTimer.schedule(mUpdateTitleTask, 0, interval);
    }

    /**
     * Обновляет текст верхнего заголовка.
     */
    private synchronized void updateTopTitle() {
        if (isEnabled) {
            mUpdateUITopTitleHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mUpdateUITopTitleHandler != null){
                        mTopTitleTextView.setText(getTopTitle());
                    }
                }
            });
        }
    }

    /**
     * Возвращает текст для верхнего заголовка.
     * @return Новый текст для верхнего заголовка.
     */
    private String getTopTitle(){
        String topTitle = getResources().getString(R.string.timersViewTitle);
        if (mSelectedTimerIndex >= 0) {
            int type = ((CircleProgressBar) getChildAt(mSelectedTimerIndex)).getDataType();
            double value = DateUtils.getPeriodByUnit(mTimerData.getStartDateTime(), type);
            topTitle += String.format(" %s: %.3f %s", mTimerData.getHliString(), value >= 0 ? value : 0,
                    DateTimeUnits.getInstance().toString(type, value));
        } else {
            topTitle += " " + mTimerData.getHliString();
        }
        return topTitle;
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        float leftRightSpace = width * 0.028f;
        float betweenTimersSpace = width * 0.0135f;
        final float timerWidth = (width - leftRightSpace * 2 - betweenTimersSpace * (mDateItems.size() - 1)) / mDateItems.size();
        final int timerHeight = (int) (timerWidth * 1.5);
        final int timerTop = (int) (height * 0.23f);
        final int timerBottom = timerTop + timerHeight;
        for (int i = 0; i < mDateItems.size(); i++) {
            final int left = (int) (leftRightSpace + (betweenTimersSpace + timerWidth) * i);
            final int right = (int) (left + timerWidth);
            getChildAt(i).layout(left, timerTop, right, timerBottom);
        }
        mTopTitleTextView.layout((int) (width * 0.05), (int) (height * 0.06), (int) (width * 0.95), (int) (height * 0.18));
        mBottomTitleTextView.layout((int) (width * 0.07), (int) (timerBottom + (height - timerBottom) * 0.2f), (int) (width * 0.93), height);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < mDateItems.size(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
    }

    /* -- END OVERRIDE METHODS -- */


    /* -- START EVENTS -- */

    OnClickListener progressBarOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isEnabled) {
                if (mOnClickProgressBarWhenDisabled != null) {
                    mOnClickProgressBarWhenDisabled.OnClick();
                }
                return;
            }
            int index = (int) v.getTag();
            if (mSelectedTimerIndex == index){
                mSelectedTimerIndex = -1;
                mTimer.cancel();
                for (int i = 0; i < mDateItems.size(); i++) {
                    ((CircleProgressBar) getChildAt(i)).setSelectionView(SelectEnum.NOT_SELECT);
                }
                updateTopTitle();
            } else {
                mSelectedTimerIndex = index;
                for (int i = 0; i < mDateItems.size(); i++) {
                    if (i == mSelectedTimerIndex) {
                        ((CircleProgressBar) getChildAt(i)).setSelectionView(SelectEnum.SELECT);
                    } else {
                        ((CircleProgressBar) getChildAt(i)).setSelectionView(SelectEnum.SELECT_OTHER);
                    }
                }
                scheduleTopTitleUpdate();
            }
            if (mOnChangeSelectedTimerListener != null) {
                mOnChangeSelectedTimerListener.onChangeSelectedTimerIndex(v, mSelectedTimerIndex);
            }
        }
    };

    /* -- END EVENTS -- */


    /* -- START INTERFACES -- */

    public static interface OnChangeSelectedTimerListener {
        public void onChangeSelectedTimerIndex(View v, int selectedTimerIndex);
    }

    public void setChangeSelectedTimerIndex(OnChangeSelectedTimerListener listener) {
        mOnChangeSelectedTimerListener = listener;
    }

    public static interface OnClickProgressBarWhenDisabled {
        public void OnClick();
    }

    public void setOnClickProgressBarWhenDisabled(OnClickProgressBarWhenDisabled listener) {
        mOnClickProgressBarWhenDisabled = listener;
    }

    /* -- FINISH INTERFACES -- */
}
