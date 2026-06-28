package com.decxdence.currencyexchange.service;

import com.decxdence.currencyexchange.dao.CurrencyDao;
import com.decxdence.currencyexchange.dao.ExchangeRateDao;
import com.decxdence.currencyexchange.dto.request.ExchangeRateCreateRequestDto;
import com.decxdence.currencyexchange.dto.request.ExchangeRateReadRequestDto;
import com.decxdence.currencyexchange.dto.request.ExchangeRateUpdateRequestDto;
import com.decxdence.currencyexchange.dto.response.CurrencyResponseDto;
import com.decxdence.currencyexchange.dto.response.ExchangeRateResponseDto;
import com.decxdence.currencyexchange.exception.*;
import com.decxdence.currencyexchange.model.Currency;
import com.decxdence.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private static final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public ExchangeRateService() {
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    public List<ExchangeRateResponseDto> findAllExchangeRates() {

        List<ExchangeRateResponseDto> exchangeRates = new ArrayList<>();
        List<ExchangeRate> exchangeRatesList = exchangeRateDao.findAll();

        for (ExchangeRate exchangeRate : exchangeRatesList) {
            var baseCurrency = currencyDao.findById(exchangeRate.getBaseCurrencyId())
                    .orElseThrow(() -> new RuntimeException("Currency Not Found"));
            var targetCurrency = currencyDao.findById(exchangeRate.getTargetCurrencyId())
                    .orElseThrow(() -> new RuntimeException("Currency Not Found"));

            var baseCurrencyDto = buildCurrencyDto(baseCurrency);
            var targetCurrencyDto = buildCurrencyDto(targetCurrency);

            exchangeRates.add(new ExchangeRateResponseDto(baseCurrencyDto, targetCurrencyDto, exchangeRate.getRate()));
        }

        return exchangeRates;
    }

    public ExchangeRateResponseDto findByBaseAndTargetCurrency(ExchangeRateReadRequestDto exchangeRateReadRequestDto) {
        if (exchangeRateReadRequestDtoIsValid(exchangeRateReadRequestDto)) {

            var baseCurrencyId = currencyDao.findByCode(exchangeRateReadRequestDto.getBaseCurrencyCode())
                    .orElseThrow(() -> new ExchangeRateNotFoundException())
                    .getId();
            var targetCurrencyId = currencyDao.findByCode(exchangeRateReadRequestDto.getTargetCurrencyCode())
                    .orElseThrow(() -> new ExchangeRateNotFoundException())
                    .getId();

            var exchangeRate = exchangeRateDao.findByBaseAndTargetId(baseCurrencyId, targetCurrencyId);

            if (exchangeRate.isPresent()) {

                var baseCurrency = currencyDao.findById(baseCurrencyId).get();
                var targetCurrency = currencyDao.findById(targetCurrencyId).get();

                var baseCurrencyDto = buildCurrencyDto(baseCurrency);
                var targetCurrencyDto = buildCurrencyDto(targetCurrency);

                return new ExchangeRateResponseDto(baseCurrencyDto, targetCurrencyDto,
                        exchangeRate.get().getRate());

            } else {
                throw new ExchangeRateNotFoundException();
            }
        }
        throw new RuntimeException("Invalid exchangeRate Request");
    }

    public ExchangeRateResponseDto save(ExchangeRateCreateRequestDto exchangeRateCreateRequestDto) {

        if (exchangeRateCreateRequestDtoIsValid(exchangeRateCreateRequestDto)) {

            var baseCurrencyId = currencyDao.findByCode(exchangeRateCreateRequestDto.getBaseCurrencyCode())
                    .orElseThrow(() -> new CurrencyNotFoundException())
                    .getId();
            var targetCurrencyId = currencyDao.findByCode(exchangeRateCreateRequestDto.getTargetCurrencyCode())
                    .orElseThrow(() -> new CurrencyNotFoundException())
                    .getId();

            if (exchangeRateExistsByBaseAndTargetId(baseCurrencyId, targetCurrencyId)) {
                throw new ExchangeRateAlreadyExists();
            }

            var savedExchangeRate = exchangeRateDao.save(new ExchangeRate(baseCurrencyId, targetCurrencyId, exchangeRateCreateRequestDto.getRate()));

            return buildExchangeRateResponseDto(savedExchangeRate);
        }
        throw new MissingRequiredFieldException();
    }

    private boolean exchangeRateExistsByBaseAndTargetId(Long baseCurrencyId, Long targetCurrencyId) {
        var exchangeRate = exchangeRateDao.findByBaseAndTargetId(baseCurrencyId, targetCurrencyId);

        return exchangeRate.isPresent();
    }

    public ExchangeRateResponseDto update(String code1, String code2, ExchangeRateUpdateRequestDto exchangeRateUpdateRequestDto) {

        if (exchangeRateUpdateRequestDtoIsValid(exchangeRateUpdateRequestDto)) {
            var baseCurrencyId = currencyDao.findByCode(code1)
                    .orElseThrow(() -> new ExchangeRateNotFoundException())
                    .getId();
            var targetCurrencyId = currencyDao.findByCode(code2)
                    .orElseThrow(() -> new ExchangeRateNotFoundException())
                    .getId();

            var exchangeRate = exchangeRateDao.findByBaseAndTargetId(baseCurrencyId, targetCurrencyId)
                    .orElseThrow(() -> new ExchangeRateNotFoundException());

            exchangeRate.setRate(exchangeRateUpdateRequestDto.getRate());

            var updatedExchangeRate = exchangeRateDao.update(exchangeRate);

            return buildExchangeRateResponseDto(updatedExchangeRate);

        }

        throw new MissingRequiredFieldException();
    }

    private ExchangeRateResponseDto buildExchangeRateResponseDto(ExchangeRate exchangeRate) {

        var baseCurrency = currencyDao.findById(exchangeRate.getBaseCurrencyId())
                .orElseThrow(() -> new RuntimeException("Currency Not Found"));
        var targetCurrency = currencyDao.findById(exchangeRate.getTargetCurrencyId())
                .orElseThrow(() -> new RuntimeException("Currency Not Found"));

        var baseCurrencyDto = buildCurrencyDto(baseCurrency);
        var targetCurrencyDto = buildCurrencyDto(targetCurrency);

        return new ExchangeRateResponseDto(baseCurrencyDto, targetCurrencyDto, exchangeRate.getRate());
    }

    private boolean exchangeRateCreateRequestDtoIsValid(ExchangeRateCreateRequestDto exchangeRateCreateRequestDto) {

        var baseCode = exchangeRateCreateRequestDto.getBaseCurrencyCode();
        var targetCode = exchangeRateCreateRequestDto.getTargetCurrencyCode();
        var rate = exchangeRateCreateRequestDto.getRate();

        baseCode = baseCode.toUpperCase();
        targetCode = targetCode.toUpperCase();

        if (baseCode.equals("^[A-Z]{3}")
        && targetCode.equals("^[A-Z]{3}")
        && rate.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }
        throw new InvalidRequestException();
    }

    private boolean exchangeRateReadRequestDtoIsValid(ExchangeRateReadRequestDto exchangeRateReadRequestDto) {
        var baseCode = exchangeRateReadRequestDto.getBaseCurrencyCode();
        var targetCode = exchangeRateReadRequestDto.getTargetCurrencyCode();

        baseCode = baseCode.toUpperCase();
        targetCode = targetCode.toUpperCase();

        if (baseCode.matches("^[A-Z]{3}$]")
                && targetCode.matches("^[A-Z]{3}$")) {
            return true;
        }
        throw new InvalidPathException();
    }

    private boolean exchangeRateUpdateRequestDtoIsValid(ExchangeRateUpdateRequestDto exchangeRateUpdateRequestDto) {
        return exchangeRateUpdateRequestDto.getRate() != null
                && exchangeRateUpdateRequestDto.getRate().compareTo(BigDecimal.ZERO) > 0;
    }

    private static CurrencyResponseDto buildCurrencyDto(Currency currency) {
        return new CurrencyResponseDto(
                currency.getCode(),
                currency.getFullName(),
                currency.getSign());
    }
}
