package com.disenkoart.howlongi.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.disenkoart.howlongi.FontManager;

/**
 * Created by Артём on 11.09.2016.
 */
public class CustomTextView extends View {
    final String TAG = getClass().getSimpleName();

    /**
     * Текст TextView
     */
    private String mText = "1";

    /**
     * Кисть, отрисовывающая текст.
     */
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Поля, определяющие размеры TextView.
     */
    private int mWidth, mHeight, mMaxTextHeight, mMaxTextSize = Integer.MAX_VALUE, mMinTextSize = 12;

    /**
     * Прямоугольник, определяющий границы текста.
     */
    private Rect mTextRect = new Rect();

    /**
     * Положение текста.
     */
    private int mTextGravity = Gravity.CENTER;

    /**
     * Флаг выполнения анимации.
     */
    private boolean isRunAnimation = false;

    /**
     * Флаг движения строки влево.
     */
    private boolean isMoveToLeft = true;

    private Handler mUpdateTimeHandler;

    /**
     * Интервал обновления анимации TextView.
     */
    private static final long INTERVAL_UPDATE_VIEW = 70;

    /**
     * Начальное положение текста.
     */
    private float mTextStartPositionX;

    /**
     * Величина шага при движении текста.
     */
    private static final float STEP_X_POSITION = 2;

    /* -- START IMPLEMENTS CONSTRUCTORS -- */

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, int color) {
        super(context);
        mTextPaint.setColor(color);
        mTextPaint.setTypeface(FontManager.UNI_SANS);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* -- END IMPLEMENTS CONSTRUCTORS -- */


    /* -- START PUBLIC METHODS -- */

    /**
     * Устаналивает текст.
     * @param text Текст.
     */
    public void setText(String text) {
        if (mText.length() != text.length()) {
            resetText(text);
        }
        else {
            mText = text;
            if (!isRunAnimation){
                invalidate();
            }
        }
    }

    /**
     * Обновляет весь TextView.
     * @param text Текст.
     */
    public void resetText(String text) {
        mText = text;
        if (mHeight == 0 || mWidth == 0){
            return;
        }
        setSizeTextPaint();
        isRunAnimation = mTextRect.width() >= mWidth;
        if (isRunAnimation) {
            startMoveAnimation();
        } else {
            mUpdateTimeHandler = null;
            invalidate();
        }
    }

    /**
     * Возвращает текст установленный в TextView.
     * @return Текст из TextView.
     */
    public String getText() {
        return mText;
    }

    /**
     * Устанавливает расположение текста в TextView.
     * @param textGravity Параметр расположения текста.
     */
    public void setTextGravity(int textGravity) {
        mTextGravity = textGravity;
    }

    /**
     * Устанавливает максимальный размер текста для TextView.
     * @param size VЗначение максимального размера текста для TextView.
     */
    public void setMaxTextSize(int size) {
        mMaxTextSize = size;
    }

    /**
     * Устанавливает минимальный размер текста для TextView.
     */
    public void setMinTextSize(){
        mMinTextSize = (int) (mHeight * 0.8f);
    }

    public void onStopUpdate(){
        isRunAnimation = false;
        mUpdateTimeHandler = null;
    }

    /* -- END PUBLIC METHODS -- */


    /* -- START PRIVATE METHODS -- */

    /**
     * Устанавливает размер текста, так чтобы он вместился в прямоугольник для текста.
     */
    private void setSizeTextPaint() {
        mTextPaint.setTextSize(mMinTextSize);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        if (mHeight != 0 && mWidth != 0) {
            while (mTextRect.height() <= mMaxTextHeight && mTextRect.width() <= mWidth && mTextPaint.getTextSize() <= mMaxTextSize) {
                mTextPaint.setTextSize(mTextPaint.getTextSize() + 1);
                mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
            }
            mTextPaint.setTextSize(mTextPaint.getTextSize() - 2);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        }
    }

    /**
     * Запускает анимацию бегущей строки.
     */
    private void startMoveAnimation() {
        mUpdateTimeHandler = new Handler();
        mUpdateTimeHandler.post(updateTimerViewRunnable);
        mTextStartPositionX = 1;
        isMoveToLeft = true;
    }

    final Runnable updateTimerViewRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            if (isRunAnimation && mUpdateTimeHandler != null) {
                mUpdateTimeHandler.postDelayed(updateTimerViewRunnable, INTERVAL_UPDATE_VIEW);
            }
        }
    };

    /**
     * Возвращает позицию начала отрисовки текста, в зависимости от флага положения текста.
     * @return Позиция начала текста.
     */
    private PointF getPositionOfText(){
        PointF textStartPosition = new PointF();
        switch (mTextGravity) {
            case Gravity.CENTER:
                textStartPosition.set((mWidth - mTextRect.width()) / 2, mHeight - (mHeight - mTextRect.height()) / 2);
                break;
            case Gravity.LEFT | Gravity.CENTER_VERTICAL:
                textStartPosition.set(0, mHeight - (mHeight - mTextRect.height()) / 2);
                break;
            case Gravity.LEFT | Gravity.TOP:
                textStartPosition.set(0, mTextRect.height());
                break;
            case Gravity.LEFT | Gravity.BOTTOM:
                textStartPosition.set(0, mHeight);
                break;
        }
        return textStartPosition;
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mMaxTextHeight = (int) (mHeight * 0.8f);
        setMinTextSize();
        resetText(mText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PointF textStartPosition = new PointF();

        if (isRunAnimation){
            if (isMoveToLeft){
                mTextStartPositionX -= STEP_X_POSITION;
                float showerTextWidth = mTextRect.width() + mTextStartPositionX;
                isMoveToLeft = showerTextWidth >= mWidth * 0.95f;
            }
            else {
                mTextStartPositionX += STEP_X_POSITION;
                isMoveToLeft = mTextStartPositionX >= mWidth * 0.05f;
            }
            textStartPosition.set(mTextStartPositionX, mHeight - (mHeight - mTextRect.height()) / 2);
        } else {
            textStartPosition = getPositionOfText();
        }
        canvas.drawText(mText, textStartPosition.x, textStartPosition.y, mTextPaint);
    }

    /* -- END OVERRIDE METHODS -- */

}
