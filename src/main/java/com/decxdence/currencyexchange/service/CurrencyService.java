package com.decxdence.currencyexchange.service;

import com.decxdence.currencyexchange.dao.CurrencyDao;
import com.decxdence.currencyexchange.dto.request.CurrencyRequestDto;
import com.decxdence.currencyexchange.dto.response.CurrencyResponseDto;
import com.decxdence.currencyexchange.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public CurrencyService() {
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private static CurrencyResponseDto buildCurrencyDto(Currency currency) {
        return new CurrencyResponseDto(
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }

    public List<CurrencyResponseDto> findAllCurrencies() {
        List<Currency> currencies = currencyDao.findAll();
        List<CurrencyResponseDto> currencyDtos = new ArrayList<>();

        for (Currency currency : currencies) {
            currencyDtos.add(buildCurrencyDto(currency));
        }

        return currencyDtos;
    }

    public CurrencyResponseDto findByCode(String code) {

        var currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        return buildCurrencyDto(currency);

    }

    public CurrencyResponseDto save(CurrencyRequestDto currencyDto) {
        if (dtoIsValid(currencyDto)) {
            var savedCurrency = currencyDao.save(new Currency(
                    currencyDto.getCode(),
                    currencyDto.getFullName(),
                    currencyDto.getSign())
            );

            return buildCurrencyDto(savedCurrency);
        }
        throw new RuntimeException("Invalid currency request");
    }

    private boolean dtoIsValid(CurrencyRequestDto currencyDto) {
        return currencyDto.getCode() != null && currencyDto.getFullName() != null && currencyDto.getSign() != null;
    }


}
