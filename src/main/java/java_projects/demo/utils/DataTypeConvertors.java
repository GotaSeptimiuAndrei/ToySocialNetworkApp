package java_projects.demo.utils;

import java.time.LocalDateTime;

public class DataTypeConvertors {
    /**
     * Converts the date from LocalDateTome to a string of form "YYYY-MM-DD"
     *
     * @param dateTime - LocalDateTime
     * @return String
     */
    public static String getDateStringFromDateTime(LocalDateTime dateTime) {
        String stringDate = String.valueOf(dateTime.getYear());
        stringDate = stringDate + "-" + String.valueOf(dateTime.getMonth());
        stringDate = stringDate + "-" + String.valueOf(dateTime.getDayOfMonth());
        return stringDate;
    }

    /**
     * Converts the time from LocalDateTome to a string of form "HH:MM"
     *
     * @param dateTime - LocalDateTime
     * @return String
     */
    public static String getCompressedTimeStringFromDateTime(LocalDateTime dateTime) {
        String stringTime = String.valueOf(dateTime.getHour());
        stringTime = stringTime + ":";
        if (dateTime.getMinute() < 10)
            stringTime = stringTime + "0";
        stringTime = stringTime + String.valueOf(dateTime.getMinute());
        return stringTime;
    }

    /**
     * Converts the time from LocalDateTome to a string of form "HH:MM:SS"
     *
     * @param dateTime - LocalDateTime
     * @return String
     */
    public static String getTimeStringFromDateTime(LocalDateTime dateTime) {
        String stringTime = String.valueOf(dateTime.getHour());
        stringTime = stringTime + ":" + String.valueOf(dateTime.getMinute());
        stringTime = stringTime + ":" + String.valueOf(dateTime.getSecond());
        return stringTime;
    }

    private static String getMonthAndDay(LocalDateTime dateTime) {
        String stringDate = String.valueOf(dateTime.getMonth()).substring(0, 3);
        stringDate = stringDate + " " + String.valueOf(dateTime.getDayOfMonth());
        return stringDate;
    }

    private static String getFullDate(LocalDateTime dateTime) {
        String stringDate = String.valueOf(dateTime.getYear());
        stringDate = stringDate + " " + String.valueOf(dateTime.getMonth()).substring(0, 3);
        stringDate = stringDate + " " + String.valueOf(dateTime.getDayOfMonth());
        return stringDate;
    }

    /**
     * Converts the date from LocalDateTome to a string of form "YYYY-MM-DD"
     *
     * @param dateTime - LocalDateTime
     * @return String
     */
    public static String getDateStringFromDateTimeCompressed(LocalDateTime dateTime) {
        if (dateTime.getYear() == LocalDateTime.now().getYear())
            return getMonthAndDay(dateTime);
        return getFullDate(dateTime);
    }
}
