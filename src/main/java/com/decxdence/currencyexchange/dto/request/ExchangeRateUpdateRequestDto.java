package com.decxdence.currencyexchange.dto.request;

import java.math.BigDecimal;

public class ExchangeRateUpdateRequestDto {
    private BigDecimal rate;

    public ExchangeRateUpdateRequestDto(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
