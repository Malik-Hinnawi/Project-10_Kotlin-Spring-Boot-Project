# BookStore

**Author:** Malik Nizar Asad Al Hinnawi

## Overview

BookStore is a Kotlin Spring Boot project designed to demonstrate clean and maintainable Kotlin code for building a bookstore application. This project leverages:
- **Spring Boot 3.4** for the backend framework.
- **MySQL** as the database.
- **Mockk** for unit testing.

The project serves as an educational tool and a reference for developing a Spring Boot application with Kotlin, complete with clean architecture and practical features.

## Features

1. **Book Management**
    - Manage books through the Book Controller.
    - Includes CRUD operations for books.

2. **Author Management**
    - Manage authors through the Author Controller.
    - Includes CRUD operations for authors.

3. **Business Logic**
    - Enforces a one-to-many relationship: one author can have many books.

4. **Database Setup**
    - MySQL is used as the database, set up using Docker Compose.
    - Adminer is included for easy database interaction via a web interface.

5. **Future Plans**
    - Integration with Spring Security for authorization.

## Prerequisites

- Docker and Docker Compose installed on your system.
- Java Development Kit (JDK 21 or higher).
- Maven installed.

## Running the Project

### Step 1: Setup the Database

1. Navigate to the project root directory.
2. Run the following command to start the MySQL database and Adminer:
   ```bash
   docker-compose up
   ```

   This will:
    - Launch a MySQL database running on port **3306** with a demonstrative password.
    - Launch Adminer on port **8888**. Access it at: [http://localhost:8888](http://localhost:8888).

3. Update the database password and other credentials in the `application.yml` file (found under `src/main/resources`).

### Step 2: Start the Spring Boot Application

1. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```
2. Access the application endpoints. The base URL is:
   ```
   http://localhost:8080
   ```

### Step 3: Interact with the Application

- Use tools like Postman or curl to interact with the API.
- Alternatively, use Adminer to inspect database records directly.

## API Endpoints

### Book Controller

- `GET /books` - Retrieve all books.
- `GET /books/{isbn}` - Retrieve a book by ID.
- `POST /books` - Create a new book.
- `PUT /books/{isbn}` - Update an existing book.
- `PATCH /books/{isbn}` - Partially Updates an existing book.
- `DELETE /books/{isbn}` - Delete a book.

### Author Controller

- `GET /authors` - Retrieve all authors.
- `GET /authors/{id}` - Retrieve an author by ID.
- `POST /authors` - Create a new author.
- `PUT /authors/{id}` - Update an existing author.
- `PATCH /authors/{id}` - Partially updates an author.
- `DELETE /authors/{id}` - Delete an author.

## Unit Tests

- Unit tests for this project are written using **Mockk**, a Kotlin-first mocking library.
- Test coverage includes both controllers and service layers.
- Run tests with:
  ```bash
  mvn test
  ```

## Docker Compose Details

- **MySQL**
    - Host: `localhost`
    - Port: `3306`
    - User: `root`
    - Password: `demonstrative_password` (change this for security).

- **Adminer**
    - Access it at: [http://localhost:8888](http://localhost:8888).
    - Use the following credentials to log in:
        - **System**: `MySQL`
        - **Server**: `db` (Docker service name).
        - **Username**: `root`
        - **Password**: `demonstrative_password`

## Technology Stack

- **Kotlin**: Main programming language.
- **Spring Boot 3.4**: Framework for building the application.
- **MySQL**: Database for data persistence.
- **Mockk**: Unit testing framework.
- **Docker & Docker Compose**: Containerization of the database and tools.

## Future Improvements

- **Spring Security Integration**
    - Add role-based access control and secure endpoints.

## Acknowledgments

This project is inspired by [devtiro/course-kotlin-spring-boot](https://github.com/devtiro/course-kotlin-spring-boot) but is adapted for Spring Boot 3.4 and MySQL.

---
**Malik Nizar Asad Al Hinnawi**.

