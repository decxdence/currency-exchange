package com.decxdence.currencyexchange;

import com.decxdence.currencyexchange.dao.CurrencyDao;
import com.decxdence.currencyexchange.dao.ExchangeRateDao;
import com.decxdence.currencyexchange.model.Currency;
import com.decxdence.currencyexchange.service.CurrencyService;
import com.decxdence.currencyexchange.service.ExchangeRateService;

public class Main {
    public static void main(String[] args) {
        ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
        ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();


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
