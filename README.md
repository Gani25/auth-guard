# 🔐 Angular 16 + Spring Boot JWT Role-Based Authentication System

This repository contains a **full-stack authentication and authorization system** implemented using **Angular 16 (Frontend)** and **Spring Boot (Backend)**. The project demonstrates **JWT-based authentication**, **role-based access control (RBAC)**, and secure communication between frontend and backend.

---

## 📌 Overview

The system allows users to:

- Sign up with one or multiple roles
- Log in using username/email and password
- Receive a JWT token upon successful authentication
- Access protected frontend routes based on roles
- Access protected backend APIs enforced by Spring Security
- Automatically attach JWT tokens to HTTP requests using an Angular interceptor

---

## 🏗️ Architecture
```

Frontend (Angular 16)
↓
Login / Signup
↓
JWT Token Stored in Browser (LocalStorage)
↓
HTTP Interceptor Adds Token to Requests
↓
Spring Boot JWT Filter
↓
Spring Security Role Validation (@PreAuthorize)
↓
Protected API Response

```

---

## 🗂️ Repository Structure

```

repo-root/
│
├── security_demo_project_jwt/
│ ├── controller/
│ │ └── MainController.java
│ ├── service/
│ │ ├── JwtService.java
│ │ ├── CustomUserDetailsService.java
│ │ └── UserInfoService.java
│ ├── filter/
│ │ └── JwtAuthFilter.java
│ ├── configuration/
│ │ ├── SpringConfig.java
│ │ └── CorsConfig.java
│ ├── entity/
│ │ ├── UserInfo.java
│ │ └── Role.java
│ └── repository/
│ ├── UserInfoRepository.java
│ └── RoleRepository.java
│
├── login-app/
│ ├── src/app/
│ │ ├── login/
│ │ ├── signup/
│ │ ├── home/
│ │ ├── user/
│ │ ├── admin/
│ │ ├── tester/
│ │ ├── auth.service.ts
│ │ ├── role.guard.ts
│ │ ├── auth.interceptor.ts
│ │ └── app-routing.module.ts
│
└── README.md

````

---

## ⚙️ Tech Stack

| Layer | Technology |
|------|------------|
| Frontend | Angular 16 |
| Backend | Spring Boot 3 |
| Security | Spring Security + JWT |
| Database | MySQL |
| ORM | Spring Data JPA |
| Authentication | JWT |
| Password Encryption | BCrypt |
| API Communication | REST |
| State Storage | LocalStorage |

---

## 🚀 Backend Setup (Spring Boot)

### Prerequisites
- Java 17+
- Maven
- MySQL

---

### Database Setup

Create database:
```sql
CREATE DATABASE jwt_security_db;
````

Seed roles:

```sql
INSERT INTO role (role_name) VALUES ('ROLE_USER');
INSERT INTO role (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO role (role_name) VALUES ('ROLE_TESTER');
```

---

### Configuration

Edit `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jwt_security_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

my.secret=your_base64_secret_key
```

---

### Run Backend

```bash
mvn clean install
mvn spring-boot:run
```

Backend runs at:

```
http://localhost:8080
```

---

## 🚀 Frontend Setup (Angular 16)

### Prerequisites

- Node.js 18+
- Angular CLI 16+

---

### Install Dependencies

```bash
cd frontend/jwt-role-app
npm install
npm install jwt-decode
```

---

### Run Frontend

```bash
ng serve
```

Frontend runs at:

```
http://localhost:4200
```

---

## 🔐 API Endpoints

### Public Endpoints

| Method | URL               | Description                   |
| ------ | ----------------- | ----------------------------- |
| POST   | `/signup`         | Register new user             |
| POST   | `/generate-token` | Authenticate and generate JWT |
| GET    | `/home`           | Public endpoint               |
| GET    | `/test`           | Public endpoint               |

---

### Protected Endpoints

| Role        | Method | URL                 |
| ----------- | ------ | ------------------- |
| ROLE_USER   | GET    | `/user`             |
| ROLE_ADMIN  | GET    | `/admin/show-admin` |
| ROLE_TESTER | GET    | `/tester/test`      |

---

## 🔄 Authentication Flow

1. User logs in using `/generate-token`
2. Backend authenticates credentials using Spring Security
3. JWT token is generated and returned
4. Frontend stores token in LocalStorage
5. Angular interceptor attaches token to all HTTP requests
6. Backend JWT filter validates token
7. Role-based authorization is enforced using `@PreAuthorize`

---

## 🛡️ Security Design

### JWT Handling

- Token includes:
  - Subject (username)
  - Roles claim
  - Expiration time

- Used for:
  - Authentication
  - Frontend route access checks

- Backend authorization is always validated using database-loaded authorities

### Role Management

- Frontend sends role names only
- Backend fetches roles from database
- Users are linked to existing roles
- Roles are not created during signup

---

## 🌐 CORS Handling

- CORS is configured at Spring Security level
- OPTIONS preflight requests are allowed
- Authorization and Content-Type headers are explicitly permitted
- Frontend origin is restricted to:

  ```
  http://localhost:4200
  ```

---

## 📦 Angular Features

- Functional Role Guard (`CanActivateFn`)
- JWT decoding using `jwt-decode`
- HTTP Interceptor for automatic token injection
- Conditional UI rendering based on login state
- Multi-role selection using checkboxes during signup
- Route-level role enforcement using route metadata

---

## 🧠 Key Learnings

- Implemented JWT authentication using Spring Security
- Built role-based access control using `@PreAuthorize`
- Designed secure role attachment without JPA cascade persist issues
- Integrated Angular HTTP interceptors for token-based authentication
- Implemented functional route guards in Angular 16
- Managed CORS at the Spring Security filter level
- Prevented duplicate role creation in Many-to-Many relationships
- Handled frontend role-based navigation using JWT claims
