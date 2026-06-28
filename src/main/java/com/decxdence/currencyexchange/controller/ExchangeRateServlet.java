package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.request.ExchangeRateReadRequestDto;
import com.decxdence.currencyexchange.dto.request.ExchangeRateUpdateRequestDto;
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

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        var code1 = extractCurrencyCode(req, resp)[0];
        var code2 = extractCurrencyCode(req, resp)[1];

        mapper.writeValue(resp.getWriter(), exchangeRateService.findByBaseAndTargetCurrency(
                new ExchangeRateReadRequestDto(
                        code1,
                        code2
        )));
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var rate = req.getParameter("rate");
        var code1 = extractCurrencyCode(req, resp)[0];
        var code2 = extractCurrencyCode(req, resp)[1];


        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        mapper.writeValue(resp.getWriter(), exchangeRateService.update(code1, code2, new ExchangeRateUpdateRequestDto(
                new BigDecimal(rate)
        )));
    }

    private String[] extractCurrencyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isBlank()  || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Currency not found");
        }

        String firstCurrencyCode = pathInfo.substring(1, 4);
        String secondCurrencyCode = pathInfo.substring(4);

        return new String[]{firstCurrencyCode, secondCurrencyCode};
    }
}
