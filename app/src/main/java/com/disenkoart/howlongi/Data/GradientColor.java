package com.disenkoart.howlongi.data;

import android.graphics.Color;

/**
 * Created by Артём on 16.08.2016.
 */
public class GradientColor {
    /**
     * Начальный цвет градиента.
     */
    private int mStartColor;

    /**
     * Конечный цвет градиента.
     */
    private int mEndColor;

    /**
     * id градиента.
     */
    private int mId;

    /* -- START CONSTRUCTORS -- */

    public GradientColor(int id, int startColor, int finishColor){
        mId = id;
        mStartColor = startColor;
        mEndColor = finishColor;
    }

    /* -- END CONSTRUCTORS -- */


    /* -- START PUBLIC METHODS --*/

    /**
     * Возвращает стандартный цвет градиента.
     * @return Стандартный цвет градиента.
     */
    public static GradientColor getDefaultGradient(){
        return new GradientColor(1, Color.argb(255, 241, 239, 165), Color.argb(255, 96, 185, 154));
    }

    public int getId(){
        return mId;
    }

    public int getStartColor(){
        return  mStartColor;
    }

    public int getEndColor(){
        return mEndColor;
    }

    /* -- END PUBLIC METHODS -- */
}
