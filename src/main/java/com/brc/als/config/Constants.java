package com.brc.als.config;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM_ACCOUNT = "system";

    public static LocalDate START_DATE = LocalDate.now().minus(2,ChronoUnit.YEARS);
    public static LocalDate END_DATE = LocalDate.now().plus(1,ChronoUnit.DAYS);
    
    public static DateTime START_TIME = new DateTime(START_DATE.getYear(), START_DATE.getMonthValue(), START_DATE.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
    public static DateTime END_TIME = new DateTime(END_DATE.getYear(), END_DATE.getMonthValue(), END_DATE.getDayOfMonth(), 0, 0, 0, DateTimeZone.UTC);
    
    private Constants() {
    }
}
