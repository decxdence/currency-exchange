package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.request.ExchangeRequestDto;
import com.decxdence.currencyexchange.dto.response.ErrorResponseDto;
import com.decxdence.currencyexchange.exception.ApiException;
import com.decxdence.currencyexchange.exception.InvalidRequestException;
import com.decxdence.currencyexchange.service.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private static final ExchangeService exchangeService = ExchangeService.getInstance();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            sendResponse(resp, 200, exchangeService.convert(validateParameters(req)));
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

    private ExchangeRequestDto validateParameters(HttpServletRequest req) {

        var from = req.getParameter("from");
        var to = req.getParameter("to");
        var amount = (req.getParameter("amount"));


        if (from != null
                && to != null) {
            try {
                BigDecimal bigDecimalAmount = new BigDecimal(amount);

                return new ExchangeRequestDto(
                        from,
                        to,
                        bigDecimalAmount
                );
            } catch (NumberFormatException e) {
                throw new InvalidRequestException();
            }
        } else {
            throw new InvalidRequestException();
        }
    }
}
