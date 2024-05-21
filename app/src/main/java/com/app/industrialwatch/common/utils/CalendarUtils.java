package com.app.industrialwatch.common.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarUtils {

    public static String getCurrentMonthShort() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        return monthFormat.format(calendar.getTime());
    }

    public static String[] getMonthNames() {
        return new DateFormatSymbols().getShortMonths();
    }
}