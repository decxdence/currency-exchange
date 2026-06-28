package com.decxdence.currencyexchange.dto.response;

import com.decxdence.currencyexchange.model.Currency;

import java.math.BigDecimal;

public class ExchangeRateResponseDto {
    private CurrencyResponseDto baseCurrencyDto;
    private CurrencyResponseDto targetCurrencyDto;
    private BigDecimal rate;

    public ExchangeRateResponseDto(CurrencyResponseDto baseCurrencyDto, CurrencyResponseDto targetCurrencyDto, BigDecimal rate) {
        this.baseCurrencyDto = baseCurrencyDto;
        this.targetCurrencyDto = targetCurrencyDto;
        this.rate = rate;
    }

    public CurrencyResponseDto getBaseCurrencyDto() {
        return baseCurrencyDto;
    }

    public void setBaseCurrencyDto(CurrencyResponseDto baseCurrencyDto) {
        this.baseCurrencyDto = baseCurrencyDto;
    }

    public CurrencyResponseDto getTargetCurrencyDto() {
        return targetCurrencyDto;
    }

    public void setTargetCurrencyDto(CurrencyResponseDto targetCurrencyDto) {
        this.targetCurrencyDto = targetCurrencyDto;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
