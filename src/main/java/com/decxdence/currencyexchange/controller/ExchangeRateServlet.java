package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.request.ExchangeRateReadRequestDto;
import com.decxdence.currencyexchange.dto.request.ExchangeRateUpdateRequestDto;
import com.decxdence.currencyexchange.dto.response.ErrorResponseDto;
import com.decxdence.currencyexchange.exception.ApiException;
import com.decxdence.currencyexchange.exception.InvalidPathException;
import com.decxdence.currencyexchange.exception.InvalidRateFormatException;
import com.decxdence.currencyexchange.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        var code1 = extractCurrencyCode(req, resp)[0];
        var code2 = extractCurrencyCode(req, resp)[1];

        try {
            sendResponse(resp, 200, exchangeRateService.findByBaseAndTargetCurrency(
                    new ExchangeRateReadRequestDto(
                            code1,
                            code2
                    )));
        } catch (ApiException e) {
            sendError(resp, e, new  ErrorResponseDto(e.getMessage()));
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            var rate = req.getParameter("rate");
            var code1 = extractCurrencyCode(req, resp)[0];
            var code2 = extractCurrencyCode(req, resp)[1];
            sendResponse(resp, 200, exchangeRateService.update(code1, code2, new ExchangeRateUpdateRequestDto(
                    parseRate(rate)
            )));
        } catch (ApiException e) {
            sendError(resp, e, new ErrorResponseDto(e.getMessage()));
        }

    }

    private String[] extractCurrencyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() != 7) {
            throw new InvalidPathException();
        }

        String firstCurrencyCode = pathInfo.substring(1, 4);
        String secondCurrencyCode = pathInfo.substring(4);

        return new String[]{firstCurrencyCode, secondCurrencyCode};
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

    private BigDecimal parseRate(String rate) {

        if (rate == null || rate.isEmpty()) {
            throw new InvalidRateFormatException();
        }

        try {
            return new BigDecimal(rate);
        } catch (NumberFormatException e) {
            throw new InvalidRateFormatException();
        }
    }
}
