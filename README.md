# 📇 Connexio – Contact Management System

Connexio is a full-stack contact management system built using **Spring Boot (Backend)** and **React (Frontend)**. It provides secure authentication using JWT, CRUD operations for contacts, and a clean modern UI with optimized state management and API handling.

---

## 🚀 Tech Stack

### 🖥️ Frontend
- React (Vite / CRA)
- Axios (with interceptors)
- TanStack Query (Server state caching)
- React Hook Form + Zod (Form validation)
- React Router
- Zustand (Global state management)
- Tailwind CSS

### ⚙️ Backend
- Spring Boot
- Spring Data JPA
- MySQL
- JWT Authentication
- ModelMapper
- Lombok
- Hibernate

---

## 📁 Project Structure
Connexio/
│
├── backend/ # Spring Boot application
├── frontend/ # React application




---
## 🛠️ Installation & Setup Guide

### 📌 Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/connexio.git
cd connexio

🖥️ Backend Setup (Spring Boot)
📌 Step 2: Open Backend
Open the backend/ folder in IntelliJ IDEA (Recommended)

⚙️ Step 3: Configure Application Properties
Update src/main/resources/application.properties:
spring.application.name=Connexio

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/connexio_schema?useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server Context Path
server.servlet.context-path=/api

# JWT Secret
jwt.secret=YOUR_JWT_SECRET

Step 4: Database Setup
Create MySQL database:
CREATE DATABASE connexio_schema;


🔐 Step 5: Update Security Config
Inside:
config/SecurityConfig.java
Update allowed frontend origin:
http://localhost:5173 (default)

▶️ Step 6: Run Backend
Backend runs at:http://localhost:8080/api
