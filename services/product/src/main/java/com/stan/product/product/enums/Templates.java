package com.stan.product.product.enums;

public enum Templates {
    PRODUCT("product.csv");

    public final String path;

    private Templates(String path){
        this.path = path;
    }
}
