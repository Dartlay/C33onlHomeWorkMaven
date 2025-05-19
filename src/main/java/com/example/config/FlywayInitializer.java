package com.example.config;

import org.flywaydb.core.Flyway;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FlywayInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/Test_DB_Server", "Dartlay", "12345")
                .baselineOnMigrate(true)
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
        System.out.println("Миграции базы данных успешно выполнены");
    }
}