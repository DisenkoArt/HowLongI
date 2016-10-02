package com.disenkoart.howlongi.customView.gradientsViews;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.disenkoart.howlongi.R;
import com.disenkoart.howlongi.database.Gradient;

/**
 * Created by Артём on 21.09.2016.
 */
public class GradientView extends View {
    /**
     * Определяет цвета градиента.
     */
    private Gradient mGradientColors;

    /**
     * Paint для отрисовки градиента и рамки, если view является выбранным.
     */
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG), mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            mBmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Прямоугольник определяющий размеры view.
     */
    private Rect mBackgroundRect, mCheckViewRect;

    /**
     * Выбрана view или нет
     */
    private boolean isCheckView = false;

    /* -- START CONSTRUCTORS -- */

    public GradientView(Context context) {
        super(context);
        init();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* -- END CONSTRUCTORS -- */


    /* START PUBLIC METHODS --*/


    public void setCheckedViewFlag(boolean check){
        isCheckView = check;
        invalidate();
    }

    public void setGradientColors(Gradient gradientColors){
        mGradientColors = gradientColors;
        setPaints();
        invalidate();
    }

    public Gradient getGradientColors(){
        return mGradientColors;
    }

    /* END PUBLIC METHODS --*/


    /* -- START PRIVATE METHODS -- */

    private void init() {
        setClickable(true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow_of_gradient_view));
        mGradientColors =  new Gradient(-1L, Color.argb(255, 241, 239, 165), Color.argb(255, 96, 185, 154));
    }

    private void setPaints() {
         /*
          Определяет положение, направление и т.д. градиента.
         */
        if (mBackgroundRect == null){
            return;
        }
        LinearGradient mLinearGradient = new LinearGradient(mBackgroundRect.centerX(), mBackgroundRect.top,
                mBackgroundRect.centerX(),
                mBackgroundRect.bottom, new int[]{mGradientColors.getStartColor(), mGradientColors.getEndColor()},
                new float[]{0, 1f}, Shader.TileMode.MIRROR);
        mBackgroundPaint.setShader(mLinearGradient);

        RadialGradient borderGradient = new RadialGradient(mBackgroundRect.centerX(),
                mBackgroundRect.centerY(), mBackgroundRect.width()/2,
                new int[]{Color.argb(1, 255,255,255), Color.argb(1, 255,255,255), Color.argb(40, 255, 255, 255)},
                new float[]{0, 0.65f, 1f}, Shader.TileMode.CLAMP);
        mBorderPaint.setShader(borderGradient);

        mBmpPaint.setFilterBitmap(true);
        mBmpPaint.setDither(true);
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int sqrSize = w > h ? h : w;
        int margin = (int) (sqrSize * 0.075f);
        mBackgroundRect = new Rect((w - sqrSize) / 2 + margin, (h - sqrSize) / 2, (w + sqrSize) / 2 - margin,
                (h + sqrSize) / 2 - margin);
        int checkIcoWidth = (int) (sqrSize * 0.45f);
        int checkIcoHeight = (int) (sqrSize * 0.45f);
        mCheckViewRect = new Rect((w - checkIcoWidth) / 2, (h - checkIcoHeight) / 2 - margin/2, (w + checkIcoWidth) / 2,
                (h + checkIcoHeight) / 2 - margin/2);
        setPaints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mBackgroundRect.centerX(), mBackgroundRect.centerY(), mBackgroundRect.width() / 2, mBackgroundPaint);
        canvas.drawCircle(mBackgroundRect.centerX(), mBackgroundRect.centerY(),
                mBackgroundRect.width()/2, mBorderPaint);
        if (isCheckView) {
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_checked_gradient), null,
                    mCheckViewRect, mBmpPaint);
        }
    }

    /* -- END OVERRIDE METHODS -- */
}
