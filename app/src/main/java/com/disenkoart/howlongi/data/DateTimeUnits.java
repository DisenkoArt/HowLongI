package com.disenkoart.howlongi.data;

import android.content.res.Resources;

import com.disenkoart.howlongi.R;

import org.joda.time.MonthDay;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Артём on 11.09.2016.
 */
public class DateTimeUnits {
    private static DateTimeUnits ourInstance = new DateTimeUnits();
    private static Resources mResources;

    /**
     * Значение {"code SECONDS} поля определяющее секунды.
     */
    public static final int SECONDS = 0;

    /**
     * Значение {"code MINUTES} поля определяющее минуты.
     */
    public static final int MINUTES = 1;

    /**
     * Значение {"code HOURS} поля определяющее часы.
     */
    public static final int HOURS = 2;

    /**
     * Значение {"code DAYS} поля определяющее дни.
     */
    public static final int DAYS = 3;

    /**
     * Значение {"code MONTHS} поля определяющее месяца.
     */
    public static final int MONTHS = 4;

    /**
     * Значение {"code YEARS} поля определяющее года.
     */
    public static final int YEARS = 5;

    private static final DateItem YEARS_UNITS = new DateItem(YEARS, DAYS, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR));
    private static final DateItem MONTHS_UNITS = new DateItem(MONTHS, DAYS, MonthDay.now().minusMonths(1).dayOfMonth().getMaximumValue());
    private static final DateItem DAYS_UNITS = new DateItem(DAYS, MINUTES, 1440); // 1 день = 1440 минут
    private static final DateItem HOURS_UNITS = new DateItem(HOURS, MINUTES, 60);
    private static final DateItem MINUTES_UNITS = new DateItem(MINUTES, SECONDS, 60);

    public static DateTimeUnits getInstance() {
        return ourInstance;
    }

    private DateTimeUnits() {
    }

    public static void setResources(Resources resources){
        mResources = resources;
    }

    /**
     * Возвращает единицу измерения в строковом формате.
     * @param unit удиница измерения времени.
     * @return Единица измерения времени в строковом формате.
     */
    public String toString(int unit){
        switch (unit){
            case SECONDS:
                return mResources.getString(R.string.seconds);
            case MINUTES:
                return mResources.getString(R.string.minutes);
            case HOURS:
                return mResources.getString(R.string.hours);
            case DAYS:
                return mResources.getString(R.string.days);
            case MONTHS:
                return mResources.getString(R.string.months);
            case YEARS:
                return mResources.getString(R.string.years);
        }
        return null;
    }

    /**
     * Возвращает единицу измерения времени в строковом формате с учётом её величины (для русского языка).
     * @param unit Единица измерения времени.
     * @param value Величина единицы измерения.
     * @return Единица измерения в строковом формате.
     */
    public String toString(int unit, double value){
        switch (unit){
            case SECONDS:
                return mResources.getString(R.string.seconds);
            case MINUTES:
                return mResources.getString(R.string.minutes_value);
            case HOURS:
                return mResources.getString(R.string.hours_value);
            case DAYS:
                return mResources.getString(R.string.days_value);
            case MONTHS:
                return mResources.getString(R.string.months_value);
            case YEARS:
                return mResources.getString(R.string.years_value);
        }
        return null;
    }

    /**
     * Возвращает интервал обновления таймера для заданной ед.изм.
     * @param type Единица измерения.
     * @return Интервал обновления таймера.
     */
    public static long getIntervalUpdateByUnit(int type){
        long interval = 1800000;
        switch (type){
            case DateTimeUnits.MINUTES:
                interval = 200;
                break;
            case DateTimeUnits.HOURS:
                interval = 2000;
                break;
            case DateTimeUnits.DAYS:
                interval = 60000;
                break;
        }
        return interval;
    }

    /**
     * Возвращает список, элементы которого представляют собой тип, в котором хранятся значение единцы измерения,
     * в чём она измеряется и её максимальная допустимая величина.
     * @return Список
     */
    public static ArrayList<DateItem> getDateTypeList(){
        ArrayList<DateItem> items = new ArrayList<>();
        items.add(YEARS_UNITS);
        items.add(MONTHS_UNITS);
        items.add(DAYS_UNITS);
        items.add(HOURS_UNITS);
        items.add(MINUTES_UNITS);
        return items;
    }
}
