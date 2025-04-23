# User Management System

Система управления пользователями с веб-интерфейсом и REST API

## ⚙️ Требования
- Java 17+
- PostgreSQL 13+
- Apache Tomcat 9+
- Maven 3.6+
- Docker (опционально)

## 🛠️ Установка

Создайте БД в PostgreSQL:

sql
psql -U your-name -d user_management_db -W user_management_db;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
Настройте подключение в src/main/java/org/example/config/DatabaseConfig.java:

config.setJdbcUrl("jdbc:postgresql://localhost:5432/user_management_db");
config.setUsername("your_db_username");  // Замените на ваше имя пользователя
config.setPassword("your_db_password");  // Замените на ваш пароль
🚀 Запуск
Вариант 1: С Tomcat
Скопируйте target/your-app.war в webapps/ Tomcat

Запустите Tomcat

Вариант 2: С Docker
Напишете мне если вам потребуеться Dockerfile b docker-compose.yml

API Endpoints
Метод	Путь	Описание
GET	/api/get?id={id}	Получить пользователя
POST	/api/create	Создать пользователя
PUT	/api/change-login	Изменить логин
DELETE	/api/delete?id={id}	Удалить пользователя




