package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        mapper.writeValue(resp.getWriter(), currencyService.findByCode(extractCurrencyCode(req, resp)));
    }

    private static String extractCurrencyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isBlank() || !pathInfo.startsWith("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Currency Not Found");
        }
        return pathInfo = pathInfo.substring(1);
    }
}
