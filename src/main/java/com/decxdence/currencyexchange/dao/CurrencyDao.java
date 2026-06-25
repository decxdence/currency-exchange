package com.decxdence.currencyexchange.dao;

import com.decxdence.currencyexchange.model.Currency;
import com.decxdence.currencyexchange.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private static final CurrencyDao INSTANCE = new CurrencyDao();
    private static final String DELETE_SQL = """
            DELETE FROM currencies
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
                    INSERT INTO currencies(code, full_name, sign)
                    VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE currencies
            SET code = ?,
                full_name = ?,
                sign = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
                    SELECT id,
                           code,
                           full_name,
                           sign
                    FROM currencies
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                    WHERE id = ?
            """;


    private CurrencyDao() {
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    private static Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }

    public Optional<Currency> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Currency currency = buildCurrency(resultSet);

                return Optional.of(currency);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> findAll() {
        try (var connection = ConnectionManager.get();
            var  statement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = statement.executeQuery();

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Currency currency) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setLong(4, currency.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency save(Currency currency) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());

            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                currency.setId(generatedKeys.getLong(1));
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
