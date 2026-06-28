package com.decxdence.currencyexchange.service;

import com.decxdence.currencyexchange.dao.CurrencyDao;
import com.decxdence.currencyexchange.dao.ExchangeRateDao;
import com.decxdence.currencyexchange.dto.request.ExchangeRequestDto;
import com.decxdence.currencyexchange.dto.response.CurrencyResponseDto;
import com.decxdence.currencyexchange.dto.response.ExchangeResponseDto;
import com.decxdence.currencyexchange.exception.CurrencyNotFoundException;
import com.decxdence.currencyexchange.exception.InvalidRequestException;
import com.decxdence.currencyexchange.model.Currency;


import java.math.BigDecimal;
import java.math.RoundingMode;


public class ExchangeService {
    private static final ExchangeService INSTANCE = new ExchangeService();
    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private static final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    private ExchangeService() {
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    public ExchangeResponseDto convert(ExchangeRequestDto exchangeRequestDto) {

        if (validate(exchangeRequestDto)) {

            var fromCurrency = currencyDao.findByCode(exchangeRequestDto.getFrom())
                    .orElseThrow(() -> new CurrencyNotFoundException());
            var fromCurrencyId = fromCurrency.getId();
            var baseCurrencyResponseDto = buildCurrencyResponseDto(fromCurrency);


            var toCurrency = currencyDao.findByCode(exchangeRequestDto.getTo())
                    .orElseThrow(() -> new CurrencyNotFoundException());
            var toCurrencyId = toCurrency.getId();
            var targetCurrencyResponseDto = buildCurrencyResponseDto(toCurrency);

            var rate = findRate(fromCurrencyId, toCurrencyId);

            var convertedAmount = rate.multiply(exchangeRequestDto.getAmount());

            return new ExchangeResponseDto(
                    baseCurrencyResponseDto,
                    targetCurrencyResponseDto,
                    rate,
                    exchangeRequestDto.getAmount(),
                    convertedAmount
            );
        }
        throw new InvalidRequestException();
    }

    private boolean validate(ExchangeRequestDto exchangeRequestDto) {
        var from =  exchangeRequestDto.getFrom();
        var to = exchangeRequestDto.getTo();
        var amount = exchangeRequestDto.getAmount();

        from = from.toUpperCase();
        to = to.toUpperCase();

        return from.matches("^[A-Z]{3}$")
                && to.matches("^[A-Z]{3}$")
                && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private static BigDecimal findRate(Long fromCurrencyId, Long toCurrencyId) {

        var directRate = exchangeRateDao.findByBaseAndTargetId(fromCurrencyId, toCurrencyId);
        if (directRate.isPresent()) {
            return directRate.get().getRate();
        }

        var reverseRate = exchangeRateDao.findByBaseAndTargetId(toCurrencyId, fromCurrencyId);
        if (reverseRate.isPresent()) {
            return BigDecimal.ONE.divide(reverseRate.get().getRate(), 6, RoundingMode.HALF_EVEN);
        }

        var USDId = currencyDao.findByCode("USD")
                .orElseThrow(() -> new RuntimeException("Currency not found"))
                .getId();
        var rateFromUsd = exchangeRateDao.findByBaseAndTargetId(USDId, fromCurrencyId)
                .orElseThrow(() -> new RuntimeException("Currency not found"))
                .getRate();
        var rateToUsd = exchangeRateDao.findByBaseAndTargetId(USDId, toCurrencyId)
                .orElseThrow(() -> new RuntimeException("Currency not found"))
                .getRate();

        return BigDecimal.ONE.divide(rateFromUsd, 6, RoundingMode.HALF_EVEN).multiply(rateToUsd);


    }

    private CurrencyResponseDto buildCurrencyResponseDto(Currency currency) {
        return new CurrencyResponseDto(
                currency.getCode(),
                currency.getFullName(),
                currency.getSign()
        );
    }
}
