package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.request.ExchangeRequestDto;
import com.decxdence.currencyexchange.service.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private static final ExchangeService exchangeService = ExchangeService.getInstance();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var from = req.getParameter("from");
        var to = req.getParameter("to");
        var amount = new BigDecimal(req.getParameter("amount"));

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        mapper.writeValue(resp.getWriter(), exchangeService.convert(new ExchangeRequestDto(
                from,
                to,
                amount
        )));

    }
}
