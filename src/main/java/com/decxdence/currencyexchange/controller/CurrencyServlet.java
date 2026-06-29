package com.decxdence.currencyexchange.controller;

import com.decxdence.currencyexchange.dto.response.ErrorResponseDto;
import com.decxdence.currencyexchange.exception.ApiException;
import com.decxdence.currencyexchange.exception.InvalidPathException;
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

        try {
            sendResponse(resp, 200, currencyService.findByCode(extractCurrencyCode(req, resp)));
        } catch (ApiException e) {
            sendError(resp, e, new ErrorResponseDto(e.getMessage()));
        }

    }

    private static String extractCurrencyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidPathException();
        }
        return pathInfo = pathInfo.substring(1);
    }

    private static void sendResponse(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), body);
    }

    private static void sendError(HttpServletResponse resp, ApiException e , ErrorResponseDto err) throws IOException {
        resp.setStatus(e.getStatus());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), err);
    }
}
