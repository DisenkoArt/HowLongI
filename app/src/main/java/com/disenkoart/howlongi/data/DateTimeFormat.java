package com.disenkoart.howlongi.data;

import java.text.SimpleDateFormat;

/**
 * Created by Артём on 11.09.2016.
 */
public class DateTimeFormat {
    /**
     * Формат даты день.месяц.год.
     */
    public static final SimpleDateFormat DATE_FORMAT_DEFAULT = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Формат даты для USA месяц-день-год.
     */
    public static final SimpleDateFormat DATE_FORMAT_USA = new SimpleDateFormat("MM-dd-yyyy");

    /**
     * Английский формат даты день-месяц-год.
     */
    public static final SimpleDateFormat DATE_FORMAT_EN = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * 24-часовой формат времени.
     */
    public static final SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");

    /**
     * 12-часовой формат времени.
     */
    public static final SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm a");

    /**
     * Формат даты и времени для USA.
     */
    public static final SimpleDateFormat DATETIME_FORMAT_USA = new SimpleDateFormat("MM-dd-yyyy HH:mm a");

    /**
     * Стандартный (основной) формат даты и времени.
     */
    public static final SimpleDateFormat DATETIME_FORMAT_DEFAULT = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    /**
     * Английский формат даты и времени.
     */
    public static final SimpleDateFormat DATETIME_FORMAT_EN = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    /**
     * Кандский формат даты и времени.
     */
    public static final SimpleDateFormat DATETIME_FORMAT_CANADA = new SimpleDateFormat("dd.MM.yyyy HH:mm a");
}
