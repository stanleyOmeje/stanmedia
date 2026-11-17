package com.stan.account.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

public class RandomeNumberUtils {

    private static String accountNumber = "0031362467";

    // generate unique random number
    public static String generateUniqueRandomNumber(int length) {
        Long suffix = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(Integer.toString((int) (Math.random() * 10)));
        }
        return getNextTransactionRef() + sb.toString() + suffix;
    }

    private static Long getNextTransactionRef() {
        String checkDigit = StringUtils.EMPTY;
        Long transactionRef = Long.valueOf(getRandomNumberString());
        Long l = Long.sum(transactionRef, 1);
        return l;
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%012d", number);
    }

    public static void main(String[] args) {
        System.out.println(generateUniqueRandomNumber(11));
    }
}
