# Full-Stack BookStore Project

**Author:** Malik Nizar Asad Al Hinnawi

## Overview

This repository contains both the backend and frontend for the **BookStore** project. It demonstrates the use of Kotlin Spring Boot for the backend and a React-based frontend written in TypeScript. The project aims to showcase clean coding practices and effective architecture in a full-stack application, with a primary focus on the backend Kotlin Spring Boot aspects.

## Project Structure

- **Backend**: Kotlin Spring Boot project located in the `/backend` directory.
- **Frontend**: React (TypeScript) project located in the `/frontend` directory.

## Prerequisites

- Docker and Docker Compose installed.
- Node.js and npm installed (for frontend).
- Java Development Kit (JDK 17 or higher).
- Maven installed (for backend).

## Running the Project

### Step 1: Start the Backend

1. Navigate to the `/backend` directory:
   ```bash
   cd bookstore
   ```
2. Ensure the database is running. Start the MySQL database and Adminer using Docker Compose:
   ```bash
   docker-compose up
   ```
   - MySQL runs on port **3306** with a demonstrative password (change it for security).
   - Adminer is available on port **8888** ([http://localhost:8888](http://localhost:8888)).

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   The backend will be available on [http://localhost:8080](http://localhost:8080).

### Step 2: Start the Frontend

1. Navigate to the `/frontend` directory:
   ```bash
   cd 0-frontend/bookstore
   ```
2. Install the dependencies:
   ```bash
   npm install
   ```
3. Start the React application:
   ```bash
   npm run start
   ```
   The frontend will be available on [http://localhost:3000](http://localhost:3000). It uses a proxy to the backend on port **8080**.

## Frontend Overview

The frontend is built using React and TypeScript, inspired by [devtiro/course-kotlin-spring-boot](https://github.com/devtiro/course-kotlin-spring-boot) but adapted for this project. It provides a user-friendly interface to:

- Manage books (CRUD operations).
- Manage authors (CRUD operations).

## Backend Overview

The backend is a Kotlin Spring Boot project described in detail in the `/backend/README.md` file. It provides RESTful API endpoints for managing books and authors, with a MySQL database for persistence.

## Technology Stack

### Frontend
- **React** (TypeScript): For building the user interface.
- **npm**: For dependency management and scripts.

### Backend
- **Kotlin**: For writing clean, concise code.
- **Spring Boot 3.4**: Backend framework.
- **MySQL**: Database for storing application data.
- **Mockk**: Unit testing framework.
- **Maven**: Build tool for the backend.
- **Docker & Docker Compose**: Containerization for the database and Adminer.

## API Communication

The frontend communicates with the backend through RESTful API endpoints. It uses a proxy configured in the frontend (`package.json`) to route requests to the backend running on port **8080**.

## Future Improvements

- **Backend**:
  - Add Spring Security for user authentication and role-based authorization.

- **Frontend**:
  - Improve the UI/UX with additional React features.
  - Integrate frontend validation.

## Acknowledgments

This project is inspired by [devtiro/course-kotlin-spring-boot](https://github.com/devtiro/course-kotlin-spring-boot) but has been tailored for Kotlin Spring Boot 3.4, MySQL, and React.

---
**Malik Nizar Asad Al Hinnawi**.

