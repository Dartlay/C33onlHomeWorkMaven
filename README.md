# 📚 Book Library Application

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![License](https://img.shields.io/badge/License-MIT-green)

A full-featured web application for managing a digital book library with online reading and download capabilities.

## 🌟 Key Features

### 👨‍💻 For Users
- 🔐 Secure JWT authentication
- 🔍 Book search and filtering
- 📖 Online reading (PDF, EPUB, MOBI, TXT)
- ⬇️ Download books
- 📚 Personal library
- 🏆 Top books and new releases

### 👨‍💼 For Administrators
- 🛠️ CRUD operations for books
- 👥 User management
- 📁 Content upload (books + covers)

## 🛠 Technology Stack

### Backend
| Technology | Version |
|------------|--------|
| Spring Boot | 3.2.0 |
| Spring Security | 6.1.0 |
| Hibernate ORM | 6.4.4 |
| Lombok | 1.18.28 |
| JJWT | 0.12.3 |

### Frontend
- Thymeleaf
- Bootstrap 5

### Databases
- PostgreSQL (production)
- H2 (testing)

### Infrastructure
- Docker
- Maven

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL 15+
- Docker (optional)

### Installation
```bash
git clone https://github.com/yourusername/book-library.git
cd book-library

# Database setup (before first run)
createdb booklibrary

# Build and run
mvn spring-boot:run
```

Application will be available at: [http://localhost:8080](http://localhost:8080)

## 🐳 Docker Deployment

## 📚 API Documentation

Available via Swagger UI:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

![Swagger UI](docs/swagger-screenshot.png)

## 🏗 Project Structure
```
book-library/
├── src/
│   ├── main/
│   │   ├── java/com/book/
│   │   │   ├── config/       # Configurations
│   │   │   ├── controller/   # Controllers
│   │   │   ├── dto/          # Data Transfer Objects
│   │   │   ├── model/        # Database Entities
│   │   │   └── service/      # Business Logic
│   │   └── resources/
│   │       ├── static/       # CSS/JS/Images
│   │       └── templates/   # Thymeleaf templates
├── docker-compose.yml
└── pom.xml
```

## 🔒 Security
- JWT authentication
- BCrypt password hashing
- CSRF protection
- Password leak check via Have I Been Pwned API
- Role-based access control (USER/ADMIN)

## 🤝 How to Contribute
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
