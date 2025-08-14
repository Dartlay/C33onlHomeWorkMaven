package com.book;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
public class OnlineLibraryApplication {

    public static void main(String[] args) {
        // Временный код для генерации секретного ключа (удалить после использования)
        generateJwtSecret();

        SpringApplication.run(OnlineLibraryApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private static void generateJwtSecret() {
        try {
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

            System.out.println("\n=== СГЕНЕРИРОВАННЫЙ JWT СЕКРЕТ ===");
            System.out.println("Добавьте это в application.properties:");
            System.out.println("app.jwt-secret=" + base64Key);
            System.out.println("==================================\n");
        } catch (Exception e) {
            System.err.println("Ошибка генерации ключа: " + e.getMessage());
        }
    }
}