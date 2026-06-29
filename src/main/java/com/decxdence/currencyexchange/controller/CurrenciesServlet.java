package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.request.CurrencyRequestDto;
import com.decxdence.currencyexchange.dto.response.ErrorResponseDto;
import com.decxdence.currencyexchange.exception.ApiException;
import com.decxdence.currencyexchange.exception.DatabaseException;
import com.decxdence.currencyexchange.exception.MissingRequiredFieldException;
import com.decxdence.currencyexchange.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            sendResponse(resp, 200, currencyService.findAllCurrencies());
        } catch (DatabaseException e) {
            sendError(resp, e, new ErrorResponseDto(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


           try {
               sendResponse(resp, 201, currencyService.save(validateParameters(req)));
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

    private static CurrencyRequestDto validateParameters(HttpServletRequest req) throws ApiException {

        var code =  req.getParameter("code");
        var name = req.getParameter("name");
        var sign = req.getParameter("sign");

        if (code != null &&  name != null &&  sign != null) {
            return new CurrencyRequestDto(
                    req.getParameter("code"),
                    req.getParameter("name"),
                    req.getParameter("sign")
            );
        }
        throw new MissingRequiredFieldException();
    }
}
