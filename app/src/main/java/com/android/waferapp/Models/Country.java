package com.android.waferapp.Models;

public class Country {

    private String name;
    private String language;
    private String currency;

    public Country(String name, String language, String currency) {
        this.name = name;
        this.language = language;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getCurrency() {
        return currency;
    }

}
