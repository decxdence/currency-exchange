package com.decxdence.currencyexchange;

import com.decxdence.currencyexchange.dao.CurrencyDao;
import com.decxdence.currencyexchange.model.Currency;

public class Main {
    public static void main(String[] args) {
        // DatabaseInitializator.initialize();
        saveEUR();

        var currencyDao = CurrencyDao.getInstance();
        var currency4 = currencyDao.findAll();
        System.out.println(currency4);
    }

    private static void saveUSD() {
        var currencyDao = CurrencyDao.getInstance();
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setFullName("Dollar");
        currency.setSign("$");
        var savedCurrency = currencyDao.save(currency);
        System.out.println(savedCurrency);
    }

    private static void saveEUR() {
        var currencyDao = CurrencyDao.getInstance();
        Currency currency = new Currency();
        currency.setCode("EUR");
        currency.setFullName("Euro");
        currency.setSign("&");
        var savedCurrency = currencyDao.save(currency);
        System.out.println(savedCurrency);
    }
}
