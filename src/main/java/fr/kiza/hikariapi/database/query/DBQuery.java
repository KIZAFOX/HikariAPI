package fr.kiza.hikariapi.database.query;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DBQuery {

    private final DataSource source;
    private final ExecutorService executorService;

    private final List<Connection> connections;
    private final List<PreparedStatement> statements;

    public DBQuery(final DataSource source) {
        this.source = source;
        this.executorService = Executors.newFixedThreadPool(10);

        this.connections = new ArrayList<>();
        this.statements = new ArrayList<>();
    }


    /**
     * Initializes a database table with the specified name and columns if it doesn't exist.
     *
     * @param table   The name of the table to create.
     * @param columns A map representing the columns, where the key is the column name, and the value is the column type.
     */
    public void initializeDB(final String table, final Map<String, String> columns) {
        final String createTableQuery = this.createTableQuery(table, columns);
        this.update(createTableQuery);
    }

    /**
     * Executes an update SQL query (e.g., CREATE, UPDATE, DELETE).
     *
     * @param query The SQL query to execute.
     */
    public void update(final String query) {
        try (Connection connection = this.source.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void query(final BiConsumer<ResultSet, SQLException> resultConsumer, final String query, final String... params) {
        try (final Connection connection = this.source.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                resultConsumer.accept(resultSet, null);
            }

            this.connections.add(connection);
            this.statements.add(statement);
        } catch (final SQLException e) {
            resultConsumer.accept(null, e);
        } finally {
            if (this.connections.getFirst() != null) {
                try {
                    this.connections.getFirst().close();
                    this.statements.getFirst().close();
                    this.connections.removeFirst();
                    this.statements.removeFirst();

                    executorService.shutdown();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Object query(Function<ResultSet, Object> consumer, String query) {
        try (final Connection connection = this.source.getConnection();
             final PreparedStatement statement = connection.prepareStatement(query);
             final ResultSet resultSet = statement.executeQuery()) {

            final Future<Object> resultFuture = executorService.submit(() -> consumer.apply(resultSet));

            return resultFuture.get();
        } catch (final SQLException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Constructs a SQL query string to create a table with the specified columns.
     *
     * @param table   The name of the table.
     * @param columns A map where the key is the column name, and the value is the column type.
     * @return A SQL string for creating the table.
     */
    private String createTableQuery(final String table, final Map<String, String> columns) {
        final StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        query.append(table).append(" (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, ");

        columns.forEach((name, type) -> query.append(name).append(" ").append(type).append(", "));
        query.setLength(query.length() - 2);
        query.append(")");

        return query.toString();
    }
}
