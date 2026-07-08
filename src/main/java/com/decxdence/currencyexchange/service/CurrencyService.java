package com.decxdence.currencyexchange.service;

import com.decxdence.currencyexchange.dao.CurrencyDao;
import com.decxdence.currencyexchange.dto.request.CurrencyRequestDto;
import com.decxdence.currencyexchange.dto.response.CurrencyResponseDto;
import com.decxdence.currencyexchange.exception.CurrencyAlreadyExistsException;
import com.decxdence.currencyexchange.exception.CurrencyNotFoundException;
import com.decxdence.currencyexchange.exception.DatabaseException;
import com.decxdence.currencyexchange.exception.MissingRequiredFieldException;
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

    public List<CurrencyResponseDto> findAllCurrencies() throws DatabaseException {
        List<Currency> currencies = currencyDao.findAll();
        List<CurrencyResponseDto> currencyDtos = new ArrayList<>();

        for (Currency currency : currencies) {
            currencyDtos.add(buildCurrencyDto(currency));
        }

        return currencyDtos;
    }

    public CurrencyResponseDto findByCode(String code) {

        code = code.toUpperCase();
        if (!code.matches("^[A-Z]{3}$")) {
            throw new CurrencyNotFoundException();
        }

        var currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException());

        return buildCurrencyDto(currency);

    }

    public CurrencyResponseDto save(CurrencyRequestDto currencyDto) {

        if (currencyRequestDtoIsValid(currencyDto)) {

            if (currencyDao.findByCode(currencyDto.getCode()).isPresent()) {
                throw new CurrencyAlreadyExistsException();
            }


            var savedCurrency = currencyDao.save(new Currency(
                    currencyDto.getCode(),
                    currencyDto.getFullName(),
                    currencyDto.getSign())
            );

            return buildCurrencyDto(savedCurrency);

        } else {
            throw new MissingRequiredFieldException();
        }
    }

    private boolean currencyRequestDtoIsValid(CurrencyRequestDto currencyDto) {

        return currencyDto.getCode().matches("^[A-Z]{3}$");

    }


}
