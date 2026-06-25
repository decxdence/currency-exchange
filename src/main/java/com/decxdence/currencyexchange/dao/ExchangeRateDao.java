package com.decxdence.currencyexchange.dao;

import com.decxdence.currencyexchange.model.ExchangeRate;
import com.decxdence.currencyexchange.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {
    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private static final String DELETE_SQL = """
            DELETE FROM exchange_rates
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
                    INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
                    VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
                    UPDATE exchange_rates
                    SET base_currency_id = ?,
                        target_currency_id = ?,
                        rate = ?
                    WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   base_currency_id,
                   target_currency_id,
                   rate
            FROM exchange_rates
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;


    private ExchangeRateDao() {
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),
                resultSet.getLong("base_currency_id"),
                resultSet.getLong("target_currency_id"),
                resultSet.getBigDecimal("rate")
        );
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

    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(2, exchangeRate.getBaseCurrencyId());
            statement.setLong(3, exchangeRate.getTargetCurrencyId());
            statement.setBigDecimal(4, exchangeRate.getRate());

            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                exchangeRate.setId(resultSet.getLong(1));
            }
            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(ExchangeRate exchangeRate) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setLong(1, exchangeRate.getBaseCurrencyId());
            statement.setLong(2, exchangeRate.getTargetCurrencyId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.setLong(4, exchangeRate.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ExchangeRate> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = statement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> findById(Long id) {
        try (var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildExchangeRate(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
