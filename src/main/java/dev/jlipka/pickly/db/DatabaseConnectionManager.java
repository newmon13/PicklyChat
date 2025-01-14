package dev.jlipka.pickly.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;


@Slf4j
public class DatabaseConnectionManager {
    private static volatile HikariDataSource dataSource;
    private static final Object INIT_LOCK = new Object();

    public static void initialize(DatabaseConfig config) {
        if (dataSource == null) {
            synchronized (INIT_LOCK) {
                if (dataSource == null) {
                    HikariConfig hikariConfig = new HikariConfig();
                    hikariConfig.setJdbcUrl(config.getJdbcUrl());
                    hikariConfig.setUsername(config.getUsername());
                    hikariConfig.setPassword(config.getPassword());

                    hikariConfig.setMaximumPoolSize(10);
                    hikariConfig.setMinimumIdle(5);
                    hikariConfig.setIdleTimeout(300000);
                    hikariConfig.setConnectionTimeout(20000);

                    dataSource = new HikariDataSource(hikariConfig);
                    log.info("Database connection pool initialized");
                }
            }
        } else {
            log.warn("Database connection pool already initialized");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("Database connection pool not initialized");
        }
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null) {
            HikariDataSource ds = dataSource;
            dataSource = null;
            ds.close();
            log.info("Database connection pool closed");
        }
    }
}