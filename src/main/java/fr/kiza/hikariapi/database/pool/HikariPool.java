package fr.kiza.hikariapi.database.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.kiza.hikariapi.HikariAPI;
import org.slf4j.Marker;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.Instant;

public class HikariPool implements AutoCloseable {

    private final HikariConfig config;
    private final HikariDataSource dataSource;

    /**
     * Initializes the HikariCP connection pool with the specified configuration.
     *
     * @param config HikariConfig configuration for the database connection pool.
     */
    public HikariPool(final HikariConfig config) {
        this.config = config;
        configureDefaults(this.config);
        this.dataSource = new HikariDataSource(this.config);
    }

    /**
     * Applies default settings to the HikariConfig.
     *
     * @param config HikariConfig instance to apply defaults.
     */
    private void configureDefaults(HikariConfig config) {
        config.setMaximumPoolSize(4);
        config.setMaxLifetime(600_000L);
        config.setIdleTimeout(300_000L);
        config.setLeakDetectionThreshold(300_000L);
        config.setConnectionTimeout(10_000L);

        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    }

    /**
     * Closes the HikariCP DataSource when it's no longer needed.
     */
    @Override
    public void close() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
            HikariAPI.LOGGER.info("Connection closed!");
        }
    }

    /**
     * Provides read-only access to the initial HikariConfig.
     *
     * @return The HikariConfig used to initialize the pool.
     */
    public HikariConfig getConfig() {
        return config;
    }

    /**
     * Provides access to the HikariCP DataSource instance.
     *
     * @return DataSource instance managed by HikariCP.
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
