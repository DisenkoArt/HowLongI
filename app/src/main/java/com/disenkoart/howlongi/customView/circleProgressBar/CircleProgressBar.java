package com.disenkoart.howlongi.customView.circleProgressBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.data.DateItem;
import com.disenkoart.howlongi.data.DateTimeUnits;
import com.disenkoart.howlongi.database.Gradient;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by Артём on 10.09.2016.
 */
public class CircleProgressBar extends View {

    /**
     * Paint для тени, заполнителя прогресса, текста внутри круга, нижнего заголовка.
     */
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            mCenterTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            mBottomTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Прямоугольник, определяющий положение и размеры кольца прогресс-бара
     */
    private RectF mRingRectangle;

    /**
     * Прямоугольник, определяющий положение текста
     */
    private Rect mTextRectangle;

    /**
     * Внутрений цвет тени (серого кольца) прогресс-бара.
     */
    @BindColor(R.color.pbInternalShadowColor)
    int mInternalShadowColor;

    /**
     * внешний цвет тени (серого кольца) прогресс-бара.
     */
    @BindColor(R.color.pbExternalShadowColor)
    int mExternalShadowColor;

    /**
     * Флаг, отвечающий за обновление прогресс-бара, когда тот завершил свой цикл и пошёл на новый круг.
     */
    private boolean mUpdatingViewsFlag = true;

    /**
     * Значение определяющее выбран текщий ПБ или нет
     */
    private SelectEnum mSelectPB = SelectEnum.NOT_SELECT;

    /**
     * Относительная ширина линий прогресса и тени.
     */
    private static final float PROGRESS_BAR_ARC_RELATIVE_SIZE = 0.05f, PROGRESS_BAR_ARC_BACKGROUND_RELATIVE_SIZE = 0.07f;

    /**
     * Максимальное допустимое значение ПБ.
     */
    private float mMaxValue = 100;

    /**
     * Размер квадрата прогресс-бара.
     */
    private float mSquareSize;

    /**
     * текущее значение прогресса.
     */
    private float mCurrentProgress = 0;

    /**
     * Количество завершенных циклов прогресс-бара (текст внутри ПБ)
     */
    private int mCurrentValue = 0;

    /**
     * Высота прогресс-бара вместе с заголовком.
     */
    private int mHeight;

    /**
     * Градиентный цвет заполнителя прогресс-бара.
     */
    private Gradient mGradientColors = new Gradient(-1, Color.argb(255, 241, 239, 165), Color.argb(255, 96, 185, 154));

    /**
     * Строка заголовка.
     */
    private String mBottomTitle;

    /**
     * Единицы измерения прогресс-бара (секнд в минуте и т.п.), тип измеряемой величины.
     */
    private int mTimerDateUnit, mDataType;

    /* -- START CONSTRUCTORS -- */

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* -- END CONSTRUCTORS -- */


    /* START PUBLIC METHODS --*/

    /**
     * Установливает прогресс, без анимирования.
     * @param progress Величина прогресса.
     */
    public void setProgressValue(float progress) {
        if (mUpdatingViewsFlag){
            mCurrentProgress = progress;
            invalidate();
        }
    }

    public float getProgress(){
        return mCurrentProgress;
    }

    public float getMaxProgressValue(){
        return mMaxValue;
    }

    /**
     * Устанавливает количество завершенных циклов PB.
     * @param value Количество завершенных циклов.
     */
    public void setValue(int value) {
        if (mCurrentValue != value && value >= 0){
            mCurrentValue = value;
        }
    }

    /**
     * Устанавливает цвет градиента PB.
     * @param gradientColors Градиент.
     */
    public void setGradientColor(Gradient gradientColors) {
        mGradientColors = gradientColors;
        initForegroundPaint();
        invalidate();
    }

    /**
     * Возвращает измеряемую прогресс-баром величину.
     * @return Измеряемая величина.
     */
    public int getDataType() {
        return mDataType;
    }

    /**
     * Устанавливает флаг, отвечающий за выбор прогресс-бара.
     * @param selectFlag Выбран прогресс бар или нет.
     */
    public void setSelectionView(SelectEnum selectFlag) {
        mSelectPB = selectFlag;
        setBottomTitleColor();
        invalidate();
    }

    /**
     * Возвращает единицы измерения PB.
     * @return Единицы измерения PB.
     */
    public int getTimerDateUnit() {
        return mTimerDateUnit;
    }

    /**
     * Устанавливает параметры измеряемой прогресс-баром величины.
     * @param item Параметры измеряемой прогресс-баром величины.
     */
    public void setDateTypeParameters(DateItem item){
        mTimerDateUnit = item.getDateUnit();
        mDataType = item.getDateType();

        mMaxValue = item.getInnerItemsCount();
        mBottomTitle = DateTimeUnits.getInstance().toString(mDataType);
    }

    public void setUpdatingView(boolean flag){
        mUpdatingViewsFlag = flag;
    }

    /* -- END PUBLIC METHODS --*/


    /* -- START PRIVATE METHODS -- */

    private void init() {
        ButterKnife.bind(this);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mDataType = DateTimeUnits.MINUTES;
    }

    /**
     * Инициализирует кисти для отрисовки прогресс-бара.
     */
    private void initPaint() {
        mCenterTextPaint.setColor(getResources().getColor(R.color.pbCenterTextColor));
        mCenterTextPaint.setTypeface(FontManager.UNI_SANS);
        mCenterTextPaint.setTextSize(2);
        setSizeCenterTextPaint();

        setBottomTitleColor();
        mBottomTextPaint.setTextSize(mCenterTextPaint.getTextSize() / 2);
        mBottomTextPaint.setTypeface(FontManager.UNI_SANS);

        initBackgroundPaint();
        initForegroundPaint();
    }

    /**
     * Устанавливает цвет тени прогресс-бара.
     */
    private void initBackgroundPaint(){
        float strokeWidth = mSquareSize * PROGRESS_BAR_ARC_RELATIVE_SIZE;
        float externalRadius = mSquareSize / 2;
        float position = (externalRadius - (strokeWidth) * 1f) / externalRadius;
        RadialGradient mBackgroundGradient = new RadialGradient(mRingRectangle.centerX(), mRingRectangle.centerY(), externalRadius,
                new int[]{mExternalShadowColor, mExternalShadowColor, mInternalShadowColor},
                new float[]{0, position, 1}, Shader.TileMode.MIRROR);
        mBackgroundPaint.setShader(mBackgroundGradient);
        mBackgroundPaint.setStrokeWidth(mSquareSize * PROGRESS_BAR_ARC_BACKGROUND_RELATIVE_SIZE);
    }

    /**
     * Устанавливает цвет заполнителя прогресс-бара.
     */
    private void initForegroundPaint() {
        if (mRingRectangle == null)
            return;
        LinearGradient linearGradient = new LinearGradient(mRingRectangle.centerX(), mRingRectangle.top, mRingRectangle.centerX(),
                mRingRectangle.bottom, new int[]{mGradientColors.getStartColor(), mGradientColors.getEndColor()},
                new float[]{0, 1f}, Shader.TileMode.MIRROR);
        mForegroundPaint.setShader(linearGradient);
        mForegroundPaint.setStrokeWidth(mSquareSize * PROGRESS_BAR_ARC_RELATIVE_SIZE);
    }

    /**
     * Устанавливает цвет нижнего заголовка
     */
    private void setBottomTitleColor() {
        switch (mSelectPB) {
            case SELECT_OTHER:
                mBottomTextPaint.setColor(getResources().getColor(R.color.pbBottomTextColor));
                mCenterTextPaint.setColor(getResources().getColor(R.color.pbBottomTextColor));
                break;
            default:
                mBottomTextPaint.setColor(getResources().getColor(R.color.pbCenterTextColor));
                mCenterTextPaint.setColor(getResources().getColor(R.color.pbCenterTextColor));
                break;
        }
    }

    /**
     * Устанавливает размер текста центрального заголовка (счетчика циклов).
     */
    private void setSizeCenterTextPaint() {
        int height = (int) (mSquareSize * 0.25f);
        mTextRectangle = new Rect();
        mCenterTextPaint.getTextBounds(String.valueOf(mCurrentValue), 0, String.valueOf(mCurrentValue).length(), mTextRectangle);
        if (height != 0) {
            while (mTextRectangle.height() <= height) {
                mCenterTextPaint.setTextSize(mCenterTextPaint.getTextSize() + 1);
                mCenterTextPaint.getTextBounds(String.valueOf(mCurrentValue), 0, String.valueOf(mCurrentValue).length(), mTextRectangle);
            }
        }
    }

    /**
     * Инициализирует размер кольца прогресс-бара.
     */
    private void initRingRectangleSize(){
        int squareWidth, width = getWidth(), height = getHeight();
        /*Чтобы прогресс бар смотрелся одинаково на всех экранах, определяем его размеры
            зависимость высоты от ширины 70/30
        */
        if (height > width * 1.25) {
            squareWidth = width;
            mHeight = (int) (width / 0.7f);
        } else {
            mHeight = height;
            squareWidth = (int) (height * 0.7f);
        }
        mSquareSize = squareWidth * (1 - PROGRESS_BAR_ARC_BACKGROUND_RELATIVE_SIZE);
        //0.22f - относительный размер высоты нижнего заголовка (22% от высоты всей view)
        mRingRectangle = new RectF((width - mSquareSize) / 2, (height - mSquareSize - mHeight * 0.22f) / 2,
                (width + mSquareSize) / 2, (height + mSquareSize - mHeight * 0.22f) / 2);
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initRingRectangleSize();
        //градиенты задаются после определения размеров для их правильной отрисовки
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRingRectangle, 0, 360, false, mBackgroundPaint);
        if (mCurrentProgress != 0) {
            float currentSweepAngle = 360 * mCurrentProgress / mMaxValue;
            canvas.drawArc(mRingRectangle, -90, currentSweepAngle, false, mForegroundPaint);
        }
        mCenterTextPaint.getTextBounds(String.valueOf(mCurrentValue), 0, String.valueOf(mCurrentValue).length(), mTextRectangle);
        //Смещает значения 0 и 1 влево при отрисовки, чтобы они визуально были в центре
        float alignCenterSlash = 2;
        if (mCurrentValue < 2) {
            alignCenterSlash = 1.5f;
        }
        canvas.drawText(String.valueOf(mCurrentValue), (int) (mRingRectangle.centerX() - mTextRectangle.width() / alignCenterSlash),
                (int) (mRingRectangle.centerY() + mTextRectangle.height() / 2), mCenterTextPaint);

        mBottomTextPaint.getTextBounds(mBottomTitle, 0, mBottomTitle.length(), mTextRectangle);
        canvas.drawText(mBottomTitle, (int) (mRingRectangle.centerX() - mTextRectangle.width() / 2),
                mHeight * 0.95f, mBottomTextPaint);
    }

    /* -- END OVERRIDE METHODS -- */
}
