package com.disenkoart.howlongi.data;

/**
 * Created by Артём on 11.09.2016.
 */
public class DateItem {

    /**
     * Тип измеряемой величины.
     */
    private int mDateType;

    /**
     * Единицы измерения измеряемой величины.
     */
    private int mDateUnit;

    /**
     * Максимальная возможное значение для единицы измерения (напр. секнда в минуте 60).
     */
    private int mInnerItemsCount;

    /* -- START CONSTRUCTORS -- */

    public DateItem(int dateType, int dateUnit, int innerItemsCount){
        mDateType = dateType;
        mDateUnit = dateUnit;
        mInnerItemsCount = innerItemsCount;
    }

    /* -- END CONSTRUCTORS -- */


    /* START PUBLIC METHODS --*/

    public int getDateType(){
        return mDateType;
    }

    public int getInnerItemsCount(){
        return mInnerItemsCount;
    }

    public int getDateUnit(){ return mDateUnit;}

    /* END PUBLIC METHODS --*/


    /* -- START PRIVATE METHODS -- */

    /* -- END PRIVATE METHODS -- */


    /* -- START EVENTS -- */

    /* -- END EVENTS -- */

}
