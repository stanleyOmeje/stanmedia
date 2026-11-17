package com.stan.account.utils;

import java.util.Random;

public class CoreBankingUtil {
    public static Long generateId(){
        Random r = new Random();
        Long id = r.nextLong(5);
        return id;
    }
}
