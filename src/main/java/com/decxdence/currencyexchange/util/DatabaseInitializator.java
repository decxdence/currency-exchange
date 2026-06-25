package com.decxdence.currencyexchange.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public final class DatabaseInitializator {

    private DatabaseInitializator() {
    }

    public static void initialize() {
        try (var connection = ConnectionManager.get();
             var inputStream = DatabaseInitializator.class.getClassLoader().getResourceAsStream("schema.sql");
             var sql = new BufferedReader(new InputStreamReader(inputStream));
             var statement = connection.prepareStatement(sql.readAllAsString())) {

            statement.execute();

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
