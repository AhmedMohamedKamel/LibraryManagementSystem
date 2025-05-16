# Library Management System

A Spring Boot-based RESTful API project for managing library operations including authors, books, users, and borrowing records.

## 📦 Project Overview

This project demonstrates the use of:

- Spring Boot (Web, JPA, Security)
- PostgreSQL for database storage
- Hibernate for ORM
- Custom validation and global exception handling
- Factory and Validator design patterns
- RESTful API design with pagination and error management

---

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Database Setup

1. Ensure PostgreSQL is installed and running.
2. Create a database named `dev`:
   ```sql
   CREATE DATABASE dev;
   ```
3. Make sure the credentials match this setup:
   - **Username:** `your username`
   - **Password:** `your password`

You can modify these in `application.properties` if needed.

---

## ⚙️ Configuration

The application uses the following configuration (from `application.properties`):

```properties
spring.application.name=Library Managemet System
server.port=8080

app.name = Library Managemet System
app.description = description

spring.datasource.url=jdbc:postgresql://localhost:5432/dev
spring.datasource.username=your username
spring.datasource.password=your password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

> ⚠️ **Note:** `ddl-auto=create` will drop and recreate the schema on every run. Change it to `update` in production.

---

## 🧪 Running the Application

```bash
mvn spring-boot:run
```

Or run `DemoApplication.java` from your IDE.

After starting, visit:  
📍 `http://localhost:8080`

---

## 🔐 Security

- Spring Security is enabled.
- Endpoints are protected by default.
- To test APIs, you must authenticate (Basic Auth).

If you prefer to disable security for development, configure a `SecurityConfig` class to allow all endpoints.

---

## 🧠 API Features

### Authors
- Retrieve all authors (paginated)
- Retrieve by ID
- Create, update, delete
- Validation: unique by name and birth date

### Books
- Retrieve all books (paginated)
- Retrieve by ID
- Search by title, author name, or ISBN
- Create, update, delete
- Validation: unique by title + ISBN + publication date
- Only assign existing authors

### Users
- Retrieve all users (paginated)
- Retrieve by ID
- Create, update, delete
- Validation: unique email and phone
- Passwords must be encoded

### Borrowing Records
- Retrieve all records (paginated)
- Retrieve by ID
- Create, update, delete
- Filter by user or book
- A book cannot be borrowed by two users at the same time

---

## 🔧 Project Structure

```
src/main/java/com/example/demo/
├── controller          # REST Controllers
├── service             # Business Logic
├── repository          # Data Access Layer
├── model               # Entity Definitions
├── validator           # Custom Validation Logic
├── exception           # Error Handling & Global Exception Resolver
└── configurations      # Security and CORS Configs
```

---

## 🧪 Postman Testing

To test the endpoints:

1. Set header: `Content-Type: application/json`
2. Use Basic Auth (username/password)
3. Use the following endpoints:

### Authors

- `POST /api/authors` – Create author
- `GET /api/authors?page=0&size=5` – Paginated list
- `GET /api/authors/{id}` – Get by ID

### Books

- `POST /api/books` – Create book
- `GET /api/books` – Paginated list
- `GET /api/books/search?title=abc`
- `GET /api/books/search?isbn=123456789`

### Users

- `POST /api/users` – Create user
- `GET /api/users` – Paginated list

### Borrowing Records

- `POST /api/borrowing-records?userId=1&bookId=2&borrowDate=2025-05-15&returnDate=2025-05-25`
- `GET /api/borrowing-records`
- `GET /api/borrowing-records/user/{userId}`
- `GET /api/borrowing-records/book/{bookId}`

---

## ✅ Validation Summary

- Duplicate authors/books/users are not allowed.
- Phone number and email must be unique for users.
- Plain text passwords are rejected.
- Books can only be borrowed by one user at a time.

---

## 🧑‍💻 Author

**Ahmed Mohamed Kamel**  
🔗 [LinkedIn](https://www.linkedin.com/in/ahmed-mohamed-kamel/)


## 📄 License

This project is licensed under the MIT License.  
Use it for learning, improving, and contributing!
