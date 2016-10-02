package com.disenkoart.howlongi.customView.timersView;

import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.disenkoart.howlongi.AnimationUtils;
import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.database.Gradient;

/**
 * Created by Артём on 11.09.2016.
 */
public class LeftPanelTimerView extends ViewGroup {
    public static final float LEFT_PANEL_CLOSE_TRANSLATION = 0.1f;

    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mBackgroundRect;
    private LinearGradient mBackgroundLinearGradient;
    private Gradient mBackgroundGradient;
    private ImageView mDeleteTimerButton, mSendTimerButton, mChangeButton;

    private float mStartX = -1, mPrevTranslationX = Float.NaN;
    private boolean mIsMoving = false;
    boolean isOpen;
    private float minTranslationX = 0;
    private final float maxTranslationX = 0;

    private OnChangeOpenStatusListener mChangeOpenStatusListener;
    private OnOpeningListener mOnOpeningListener;
    /* -- START CONSTRUCTORS -- */

    public LeftPanelTimerView(Context context) {
        super(context);
        init();
    }

    public LeftPanelTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeftPanelTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeftPanelTimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

//    public ImageView getDeleteButton(){
//        return_ico mDeleteTimerButton;
//    }
//
//    public ImageView getSendButton(){
//        return_ico mSendTimerButton;
//    }

    public void setDeleteButtonOnClickListener(OnClickListener listener){
        mDeleteTimerButton.setOnClickListener(listener);
    }

    public void setSendButtonOnClickListener(OnClickListener listener){
        mSendTimerButton.setOnClickListener(listener);
    }

    public void setChangeButtonClickListener(OnClickListener listener){
        mChangeButton.setOnClickListener(listener);
    }

    /* -- END CONSTRUCTORS -- */


    /* START PUBLIC METHODS --*/

    public Gradient getColor(){
        return mBackgroundGradient;
    }

    public void setGradientColor(Gradient gradientColors) {
        mBackgroundGradient = gradientColors;
        updatePaints();
    }

    public void setOpening(boolean opened, boolean isAnimation){
        isOpen = opened;
        if (isAnimation){
            if (isOpen){
                animationOpenPanel();
            } else {
                animationClosePanel();
            }
        } else {
            if (isOpen){
                setTranslationX(maxTranslationX);
            } else {
                setTranslationX(minTranslationX);
            }
        }
    }

    public void close() {
        if(isOpen){
            animationClosePanel();
        }
        if (mChangeOpenStatusListener != null) {
            mChangeOpenStatusListener.onChangeOpenStatus(isOpen);
        }
    }


    /* END PUBLIC METHODS --*/


    /* -- START PRIVATE METHODS -- */

    private void init() {
        setWillNotDraw(false);
        mBackgroundGradient = new Gradient(-1L, Color.argb(255, 241, 239, 165), Color.argb(255, 96, 185, 154));

        mChangeButton = new ImageView(getContext());
        mChangeButton.setImageResource(R.drawable.ic_pen_button);
        addView(mChangeButton);

        mSendTimerButton = new ImageView(getContext());
        mSendTimerButton.setImageResource(R.drawable.ic_share_button);
        addView(mSendTimerButton);

        mDeleteTimerButton = new ImageView(getContext());
        mDeleteTimerButton.setImageResource(R.drawable.ic_bucket_button);
        addView(mDeleteTimerButton);
    }

    private void panelMoving(float currentX) {
        if (mIsMoving) {
            if (Float.isNaN(mPrevTranslationX)) {
                mPrevTranslationX = getTranslationX();
            }

            final float newTranslationX = (int) (mPrevTranslationX + currentX - mStartX);

            if (newTranslationX < minTranslationX) {
                setTranslationX(minTranslationX);
            } else if (newTranslationX > maxTranslationX) {
                setTranslationX(maxTranslationX);
            } else {
                setTranslationX(newTranslationX);
            }

        } else {
            final float deltaX = Math.abs(currentX - mStartX);
            if (deltaX >= 5) {
                mIsMoving = true;
            } else {
                mPrevTranslationX = Float.NaN;
            }
        }
    }

    private void panelTouchUp(float rawX) {
        if (rawX > mStartX  || (mStartX == rawX) && !isOpen) {
            animationOpenPanel();
        } else {
            animationClosePanel();
        }
        if (mChangeOpenStatusListener != null) {
            mChangeOpenStatusListener.onChangeOpenStatus(isOpen);
        }
    }

    private void animationOpenPanel() {
        float mTranslationX = getTranslationX();
        if (mTranslationX < maxTranslationX) {
            long time = (long) (Math.abs(maxTranslationX - mTranslationX) * 4);
            AnimatorSet xTranslation = AnimationUtils.createXAnimation(mTranslationX, maxTranslationX, time, this);
            xTranslation.start();
        }
        isOpen = true;
    }

    private void animationClosePanel() {
        final float mTranslationX = getTranslationX();
        Log.d("TRANSLATION", String.valueOf(mTranslationX));
        if (mTranslationX > minTranslationX) {
            long time = (long) (Math.abs(minTranslationX - mTranslationX) * 4);
            AnimatorSet xTranslation = AnimationUtils.createXAnimation(mTranslationX, minTranslationX, time, this);
            xTranslation.start();
        }
        isOpen = false;
    }

    private void updatePaints(){
        mBackgroundLinearGradient = new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(), new int[]{mBackgroundGradient.getStartColor(),
                mBackgroundGradient.getEndColor()}, null, Shader.TileMode.CLAMP);
        mBackgroundPaint.setShader(mBackgroundLinearGradient);
        invalidate();
    }

    private void endTouchEvent(float rawX){
        if (mStartX < 0) {
            return;
        }
        panelTouchUp(rawX);
        mPrevTranslationX = Float.NaN;
        mIsMoving = false;
        mStartX = -1;
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        if (mOnOpeningListener != null) {
            float percentOpening = (translationX - minTranslationX)/(maxTranslationX-minTranslationX);
            mOnOpeningListener.onOpening(percentOpening);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        minTranslationX =  getWidth() * (-(1 - LEFT_PANEL_CLOSE_TRANSLATION) + 0.15f);
        setOpening(isOpen, false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int l = getLeft(), t = getTop(), b = getBottom();
        mBackgroundRect = new Rect(l, t, (int) (l + w * 0.85f), b);
        int leftMargin = (int) (l + mBackgroundRect.width() * 0.15);
        int rightMargin = (int) (l + mBackgroundRect.width() * 0.85f);
        int topMargin = (int) (h * 0.08f);
        int buttonMargin = (int) (h * 0.06f);
        int buttonHeight = (h - 2 * (buttonMargin + topMargin))/getChildCount();
        for (int i = 0; i < getChildCount(); i++){
            View button = getChildAt(i);
            button.layout(leftMargin, t + topMargin + buttonMargin * i + buttonHeight * i,
                    rightMargin, t + topMargin + buttonMargin * i + buttonHeight * (i + 1));
        }
        mBackgroundLinearGradient = new LinearGradient(1, t, 1, b,
                new int[]{mBackgroundGradient.getStartColor(),
                mBackgroundGradient.getEndColor()}, null, Shader.TileMode.CLAMP);
        mBackgroundPaint.setShader(mBackgroundLinearGradient);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                mStartX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
//                float dy = Math.abs(startY - event.getRawY());
//                float dx = Math.abs(mStartX - event.getRawX());
//                if (dy > dx && dy > mHeight * 0.05)
//                    return false;
                if (mStartX >= 0) {
                    panelMoving(event.getRawX());
                }
                break;
            case MotionEvent.ACTION_UP:
                endTouchEvent(event.getRawX());
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                endTouchEvent(event.getRawX());
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mBackgroundRect, mBackgroundPaint);
    }

    /* -- END OVERRIDE METHODS -- */

    /* -- START INTERFACES -- */

    public static interface OnChangeOpenStatusListener {
        public void onChangeOpenStatus(boolean isOpen);
    }

    public void setChangeOpenStatusListener(OnChangeOpenStatusListener listener) {
        mChangeOpenStatusListener = listener;
    }

    public static interface OnOpeningListener {
        public void onOpening(float percentOpening);
    }

    public void setOpeningListener(OnOpeningListener listener) {
        mOnOpeningListener = listener;
    }


    /* -- FINISH INTERFACES -- */
}
