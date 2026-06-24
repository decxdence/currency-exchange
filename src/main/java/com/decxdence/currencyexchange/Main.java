package com.decxdence.currencyexchange;

import com.decxdence.currencyexchange.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (var connection = ConnectionManager.open()) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
