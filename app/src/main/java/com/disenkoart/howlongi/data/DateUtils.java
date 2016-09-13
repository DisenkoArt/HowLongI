package com.disenkoart.howlongi.data;

import org.joda.time.*;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Артём on 11.09.2016.
 */
public class DateUtils {
    /**
     * Возвращает от начальной даты количество прошедших: лет, месяцев, дней, часов, минут
     * @param startDateTime - начало отсчета
     * @return - количество прошедшых единиц времени с начала отсчета времени
     */
    public static int[] getWholeTimeValues(long startDateTime) {
        Period currentPeriod = new Period(startDateTime, DateTime.now().getMillis());
        if (currentPeriod.getMillis() < 0) {
            return new int[5];
        }
        int[] valuesProgressBar = new int[5];
        valuesProgressBar[0] = currentPeriod.getYears();
        valuesProgressBar[1] = currentPeriod.getMonths();
        valuesProgressBar[2] = currentPeriod.getWeeks() * 7 + currentPeriod.getDays();
        valuesProgressBar[3] = currentPeriod.getHours();
        valuesProgressBar[4] = currentPeriod.getMinutes();
        return valuesProgressBar;
    }

    /**
     * Возвращает количество неполных прошедших единиц измерений времени:
     дней в году, дней в месяце, минут в дне, минут в часе, секунд в часе
     * @return количество неполных прошедших единиц измерений времени
     */
    public static float[] getCompletionDateValues(long startDateTime) {
        Period period = new Period(startDateTime, DateTime.now().getMillis());
        if (period.getMillis() < 0) {
            return new float[5];
        }
        float[] completionProgressBar = new float[]{0, 0, 0, 0, 0};
        completionProgressBar[4] = period.getSeconds();
        completionProgressBar[3] = period.getMinutes() + completionProgressBar[4] / 60;
        completionProgressBar[2] = period.getHours() * 60 + period.getMinutes();
        completionProgressBar[1] = period.getWeeks() * 7 + period.getDays() +
                completionProgressBar[2] / 1440;
        DateTime yearDateTime = new DateTime(startDateTime).plusYears(period.getYears());
        completionProgressBar[0] = Days.daysBetween(yearDateTime, DateTime.now()).getDays() +
                completionProgressBar[2] / (getMaximumDaysOfYear() * 1440);
        return completionProgressBar;
    }

    public static float getCompletionDateValue(long startDateTime, int dateType){
        Period period = new Period(startDateTime, DateTime.now().getMillis());
        if (period.getMillis() > 0) {
            switch (dateType) {
                case DateTimeUnits.MINUTES:
                    return period.getSeconds();
                case DateTimeUnits.HOURS:
                    return period.getMinutes() + period.getSeconds() / 60;
                case DateTimeUnits.DAYS:
                    return period.getHours() * 60 + period.getMinutes();
                case DateTimeUnits.MONTHS:
                    return period.getWeeks() * 7 + period.getDays() +
                            (period.getHours() * 60 + period.getMinutes()) / 1440;
                case DateTimeUnits.YEARS:
                    DateTime yearDateTime = new DateTime(startDateTime).plusYears(period.getYears());
                    return Days.daysBetween(yearDateTime, DateTime.now()).getDays() +
                            (period.getHours() * 60 + period.getMinutes()) / (getMaximumDaysOfYear() * 1440);
            }
        }
        return -1;
    }

    public static int getWholeTimeValue(long startDateTime, int dateType){
        Period currentPeriod = new Period(startDateTime, DateTime.now().getMillis());
        if (currentPeriod.getMillis() > 0) {
            switch (dateType) {
                case DateTimeUnits.MINUTES:
                    return currentPeriod.getMinutes();
                case DateTimeUnits.HOURS:
                    return currentPeriod.getHours();
                case DateTimeUnits.DAYS:
                    return currentPeriod.getWeeks() * 7 + currentPeriod.getDays();
                case DateTimeUnits.MONTHS:
                    return currentPeriod.getMonths();
                case DateTimeUnits.YEARS:
                    return currentPeriod.getYears();
            }
        }
        return -1;
    }

    /**
     * Определяет разницу между текущим времененем  и стартовым для заданной единицы измерения времени.
     * Учитывается только текущее время (т.е. секунды в текущей минуте, минуты в текущем часе и т.д.)
     * @param dateType Еденица измерения.
     * @return Разница между текущим временем и мременем старта в заданной ед. изм.
     */
    public static double getPeriodByUnit(long startDateTime, int dateType) {
        DateTime firstDateTime = new DateTime(startDateTime);
        Period period = new Period(firstDateTime, DateTime.now());
        double periodValue = 0;
        switch (dateType) {
            case DateTimeUnits.SECONDS:
                periodValue = period.getSeconds();
                break;
            case DateTimeUnits.MINUTES:
                periodValue = Minutes.minutesBetween(firstDateTime, DateTime.now()).getMinutes() +
                        period.getSeconds() / 60d + period.getMillis() / 60000d;
                break;
            case DateTimeUnits.YEARS:
                periodValue = Years.yearsBetween(firstDateTime, DateTime.now()).getYears() +
                        (getDaysOfCurrentYear(period, firstDateTime) * 24 + period.getHours()) /
                                (getMaximumDaysOfYear() * 24);
                break;
            case DateTimeUnits.MONTHS:
                periodValue = Months.monthsBetween(firstDateTime, DateTime.now()).getMonths() +
                        (getDaysOfCurrentMonth(period) * 24 + period.getHours())
                                / ((float) Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) * 24);
                break;
            case DateTimeUnits.DAYS:
                periodValue = Days.daysBetween(firstDateTime, DateTime.now()).getDays() +
                        (period.getHours() + period.getMinutes() / 60f) / 24f;
                break;
            case DateTimeUnits.HOURS:
                periodValue = Hours.hoursBetween(firstDateTime, DateTime.now()).getHours() +
                        period.getMinutes() / 60f + period.getSeconds() / 3600f;
                break;
        }
        return periodValue < 0 ? -1 : periodValue;
    }

    /**
     * Возвращает количество пройденных секунд на данный момент (учитывая только секнды текущей минуты)
     * @param startTime Точка отсчета времени.
     * @return Количество прошедших секунд в текущей минуте.
     */
    public static double getSecondsPeriod(long startTime){
        Period period = new Period(startTime, DateTime.now().getMillis());
        double periodValue = period.getSeconds();
        return periodValue < 0 ? -1 : periodValue;
    }

    /**
     * Возвращает количество пройденных дней в текущем месяце.
     * @param period Период между текущей датой и стартовой.
     * @return Количесвто пройденных дней в текщем месяце.
     */
    private static int getDaysOfCurrentMonth(Period period) {
        return (period.getWeeks() * 7 + period.getDays());
    }

    /**
     * Возвращает количество пройденных дней в текущем году.
     * @param period Период между текущей датой и стартовой.
     * @param startDateTime Точка отсчета времени.
     * @return Количество пройденных дней в текущем году.
     */
    private static int getDaysOfCurrentYear(Period period, DateTime startDateTime) {
        DateTime yearDateTime = new DateTime(startDateTime).plusYears(period.getYears());
        return Days.daysBetween(yearDateTime, DateTime.now()).getDays();
    }

    private static float getMaximumDaysOfYear(){
        return (float) Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    public static String convertDateToLocaleString(Locale locale, long dateTime) {
        if (locale.equals(Locale.US)) {
            return DateTimeFormat.DATETIME_FORMAT_USA.format(dateTime);
        } else if (locale.equals(Locale.CANADA)) {
            return DateTimeFormat.DATETIME_FORMAT_CANADA.format(dateTime);
        } else if (locale.equals(Locale.ENGLISH)) {
            return DateTimeFormat.DATETIME_FORMAT_EN.format(dateTime);
        } else {
            return DateTimeFormat.DATETIME_FORMAT_DEFAULT.format(dateTime);
        }
    }
}
