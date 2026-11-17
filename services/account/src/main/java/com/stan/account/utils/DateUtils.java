package com.stan.account.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class DateUtils {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final int WAT_OFFSET = 1;

    public static String dateAdder() {
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1); // number of days to add
        //       c.add(Calendar.YEAR, 1);
        //       c.add(Calendar.MONTH, 1);
        //       c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        //       c.add(Calendar.HOUR, 1);
        //       c.add(Calendar.MINUTE, 1);
        //       c.add(Calendar.SECOND, 1);
        Date currentDatePlusOne = c.getTime();

        return dateFormat.format(currentDatePlusOne);
    }

    public static String getExpiryDate(int additionalMinutesToCurrentMinute) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+1"));
        String expiryDateAndTime = now
            .plus(additionalMinutesToCurrentMinute, ChronoUnit.MINUTES)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return expiryDateAndTime;
    }

    public static String getExpiryDateWoven(int additionalMinutesToCurrentMinute) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT+1"));
        String expiryDateAndTime = now
            .plus(additionalMinutesToCurrentMinute, ChronoUnit.MINUTES)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return expiryDateAndTime + " GMT+0100";
    }

    //                public static void main(String[] args) {
    //                    //System.out.println(dateAdder());
    //                    System.out.println(getExpiryDateWoven(30));
    //                }

    public static Supplier<LocalDate> getFormattedDate = LocalDate::now;

    public static Supplier<String> getFormattedTime = () -> {
        LocalTime localDate = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        return localDate.format(formatter);
    };
    public static Function<String, LocalDate> formatDateRequest = date -> {
        String[] dateSplit = date.split("-");
        List<Integer> dateString = Arrays.stream(dateSplit).map(Integer::valueOf).collect(Collectors.toList());
        return LocalDate.of(dateString.get(2), dateString.get(1), dateString.get(0));
    };

    public static Function<String, String> formatDateRequestForKyc = date -> {
        String[] dateSplit = date.split("-");
        List<Integer> dateString = Arrays.stream(dateSplit).map(Integer::valueOf).collect(Collectors.toList());
        return LocalDate.of(dateString.get(2), dateString.get(1), dateString.get(0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    };

    public static Supplier<String> getTodaysDate = () -> {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(dateTimeFormatter);
    };

    public static Supplier<Instant> transactionDate = () -> {
        Clock offsetClock = Clock.offset(Clock.systemUTC(), Duration.ofHours(WAT_OFFSET));
        Instant instant = Instant.ofEpochSecond(Instant.now(offsetClock).getEpochSecond());
        log.info("DateStamp => {}", instant);
        return instant;
    };

    public static Supplier<String> getDate = () -> {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH-mm-ss");
        return localDateTime.format(dateTimeFormatter);
    };

    public static boolean isStartDateGreaterThanEndDate(String fromDate, String toDate) {
        LocalDate startDate = formatDateRequest.apply(fromDate);
        LocalDate endDate = formatDateRequest.apply(toDate);
        return startDate.isAfter(endDate);
    }

    // convert date format of 09-Sep-2022 to 09-09-2022
    public static String convertDateFormat(String dateInString) {
        // check if dateInString is in dd-MM-yyyy format
        if (dateInString.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return dateInString;
        }
        LocalDate parsedDate = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH));
        return parsedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static final Function<Instant, String> formatDate = date ->
        LocalDateTime.ofInstant(date, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    public static void main(String[] args) {
        System.out.println(convertDateFormat("09-09-2022"));
        System.out.println("Oscar: " + DateUtils.getTodaysDate.get());
        System.out.println("Jide: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}
