package org.example;


import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@SpringBootApplication
public class CrudappApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudappApplication.class, args);
    }

    @Bean
    public CommandLineRunner checkDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("✅ SUCCESSFULLY CONNECTED TO: " +
                        connection.getMetaData().getDatabaseProductName() +
                        " (" + connection.getCatalog() + ")");
            } catch (Exception e) {
                System.err.println("❌ CONNECTION ERROR: " + e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    CommandLineRunner testDbStructure(UserRepository repo) {
        return args -> {
            try {
                Map<String, Object> result = repo.testColumns();
                System.out.println("✅ Структура таблицы корректна: " + result.keySet());
            } catch (Exception e) {
                System.err.println("❌ Ошибка структуры таблицы: " + e.getMessage());
            }
        };
    }
}