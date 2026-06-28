package com.decxdence.currencyexchange.dto.response;

import java.math.BigDecimal;

public class ExchangeResponseDto {
    private CurrencyResponseDto from;
    private CurrencyResponseDto to;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;


    public CurrencyResponseDto getFrom() {
        return from;
    }

    public void setFrom(CurrencyResponseDto from) {
        this.from = from;
    }

    public CurrencyResponseDto getTo() {
        return to;
    }

    public void setTo(CurrencyResponseDto to) {
        this.to = to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public ExchangeResponseDto(CurrencyResponseDto from, CurrencyResponseDto to, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;


    }
}
