package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.request.ExchangeRateCreateRequestDto;
import com.decxdence.currencyexchange.dto.response.ErrorResponseDto;
import com.decxdence.currencyexchange.exception.ApiException;
import com.decxdence.currencyexchange.exception.InvalidRateFormatException;
import com.decxdence.currencyexchange.exception.MissingRequiredFieldException;
import com.decxdence.currencyexchange.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            sendResponse(resp, 200, exchangeRateService.findAllExchangeRates());
        } catch (ApiException e) {
            sendError(resp, e, new ErrorResponseDto(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            sendResponse(resp, 200, exchangeRateService.save(validateParameters(req)));
        } catch (ApiException e) {
            sendError(resp, e, new ErrorResponseDto(e.getMessage()));
        }

    }

    private static void sendResponse(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), body);
    }

    private static void sendError(HttpServletResponse resp, ApiException e, ErrorResponseDto err) throws IOException {
        resp.setStatus(e.getStatus());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), err);
    }

    private static ExchangeRateCreateRequestDto validateParameters(HttpServletRequest req) throws ApiException {

        var baseCode =  req.getParameter("baseCurrencyCode");
        var targetCode = req.getParameter("targetCurrencyCode");
        var rate = req.getParameter("rate");

        if (baseCode != null || targetCode != null ||  rate != null) {
            try {
                var bigdecRate = new BigDecimal(baseCode);
                return new ExchangeRateCreateRequestDto(
                        baseCode,
                        targetCode,
                        bigdecRate
                );
            } catch (NumberFormatException e) {
                throw new InvalidRateFormatException();
            }
        }
        throw new MissingRequiredFieldException();
    }
}
