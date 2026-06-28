package com.decxdence.currencyexchange.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public final class DatabaseInitializator {

    private DatabaseInitializator() {
    }

    public static void initialize() {
        try (var connection = ConnectionManager.get();
             var inputStream = Objects.requireNonNull(
                     DatabaseInitializator
                             .class
                             .getClassLoader()
                             .getResourceAsStream("schema.sql"),
                     "schema.sql is not found"
             );
             var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String[] sqlQueries = bufferedReader.readAllAsString().split(";");

            for (String query : sqlQueries) {

                query = query.trim();

                if (query.isBlank()) {
                    continue;
                }

                try (var statement = connection.prepareStatement(query)) {
                    statement.execute();
                }
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
