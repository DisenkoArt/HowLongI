package com.disenkoart.howlongi.customView.gradientsViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.disenkoart.howlongi.MainApplication;
import com.disenkoart.howlongi.database.Gradient;

import java.util.ArrayList;

/**
 * Created by Артём on 21.09.2016.
 */
public class GradientViewGroup extends ViewGroup {

    /**
     * Количество строк и столбцов для отбражения gradientColorView.
     */
    private static final int COLUMNS_COUNT = 7, ROWS_COUNT = 2;

    /**
     * Индекс выбранного view.
     */
    private int mIndexCurrentGradientView = 0;

    /* -- START CONSTRUCTORS -- */

    public GradientViewGroup(Context context) {
        super(context);
        setGradientViewGroup();
    }

    public GradientViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGradientViewGroup();
    }

    public GradientViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGradientViewGroup();
    }

    /* -- END CONSTRUCTORS -- */


    /* START PUBLIC METHODS --*/

    public Gradient getCurrentGradient() {
        return ((GradientView) getChildAt(mIndexCurrentGradientView)).getGradientColors();
    }

    public void setCurrentGradient(Long currentGradientId) {
        if (currentGradientId < 0) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            GradientView gradientView = (GradientView) getChildAt(i);
            if (gradientView.getGradientColors().getId().equals(currentGradientId)) {
                gradientView.setCheckedViewFlag(true);
                mIndexCurrentGradientView = i;
            } else {
                gradientView.setCheckedViewFlag(false);
            }
        }
    }

    /* END PUBLIC METHODS --*/


    /* -- START PRIVATE METHODS -- */

    private void setGradientViewGroup() {
        ArrayList<Gradient> gradientColors = (ArrayList<Gradient>) MainApplication.getInstance().getDbSession().getGradientDao().loadAll();
        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < COLUMNS_COUNT; j++) {
                GradientView gradientColorView = new GradientView(getContext());
                gradientColorView.setGradientColors(gradientColors.get(i * COLUMNS_COUNT + j));
                gradientColorView.setTag(i * COLUMNS_COUNT + j);
                gradientColorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIndexCurrentGradientView = (int) v.getTag();
                        changeCurrentGradientColor();
                    }
                });
                addView(gradientColorView);
            }
        }
        ((GradientView) getChildAt(0)).setCheckedViewFlag(true);
    }

    private void changeCurrentGradientColor() {
        for (int i = 0; i < getChildCount(); i++) {
            ((GradientView) getChildAt(i)).setCheckedViewFlag(i == mIndexCurrentGradientView);
        }
    }

    /* -- END PRIVATE METHODS -- */


    /* -- START OVERRIDE METHODS -- */

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }
        int width = getWidth() / COLUMNS_COUNT, height = getHeight() / ROWS_COUNT;
        float minSqrSize = width > height ? height : width;
        float rowsMargin = (getHeight() - minSqrSize * ROWS_COUNT) / (ROWS_COUNT - 1);
        float columnsMargin = (getWidth() - minSqrSize * COLUMNS_COUNT) / (COLUMNS_COUNT - 1);
        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < COLUMNS_COUNT; j++) {
                GradientView gradientColorView = ((GradientView) getChildAt(i * COLUMNS_COUNT + j));
                int left = (int) (minSqrSize * j + columnsMargin * j);
                int top = (int) (minSqrSize * i);
                int right = (int) (left + minSqrSize);
                int bottom = (int) (top + minSqrSize);
                gradientColorView.layout(left, top, right, bottom);
            }
        }
    }

    /* -- END OVERRIDE METHODS -- */
}
