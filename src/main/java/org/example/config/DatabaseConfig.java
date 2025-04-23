package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
    private static final HikariDataSource dataSource;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://host.docker.internal:5432/user_management_db");
            config.setUsername("Dartlay");
            config.setPassword("12345");

            config.setMaximumPoolSize(5);
            config.setConnectionTimeout(3000);
            config.setIdleTimeout(60000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
            try (java.sql.Connection conn = dataSource.getConnection()) {
                System.out.println("✅ Database connection test successful!");
            }
        } catch (Exception e) {
            System.err.println("❌ Database initialization failed:");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}