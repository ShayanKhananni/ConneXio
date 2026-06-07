# 📇 Connexio – Contact Management System

Connexio is a full-stack contact management system built using **Spring Boot (Backend)** and **React (Frontend)**.  
It provides secure authentication using JWT, CRUD operations for contacts, and a clean modern UI with optimized state management and API handling.

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

```
Connexio/
│
├── backend/     # Spring Boot application
└── frontend/    # React application
    └── csv/
        └── book.csv   # Sample CSV for contact import
```

## 🛠️ Installation & Setup Guide

### 📌 Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/connexio.git
cd connexio
```

---

## 🖥️ Backend Setup (Spring Boot)

### 📌 Step 2: Open Backend

Open the `backend/` folder in **IntelliJ IDEA** (Recommended).

---

### ⚙️ Step 3: Configure Application Properties

Update `src/main/resources/application.properties`:

```properties
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
```

---

### 🗄️ Step 4: Database Setup

Create the MySQL database:

```sql
CREATE DATABASE connexio_schema;
```

---

### 🔐 Step 5: Update Security Config

Inside `config/SecurityConfig.java`, update the allowed frontend origin:
http://localhost:5173 (By your react server url)

---

### ▶️ Step 6: Run the Backend

The backend will be available at:
http://localhost:8080/api


---

## ⚛️ Frontend Setup (React)

### 📌 Step 1: Open Frontend

Open the `frontend/` folder in **VS Code** (Recommended), then install all dependencies by running the following in the terminal:

```bash
npm install
```

---

### ⚙️ Step 2: Configure Axios Base URL

Inside `src/api/axiosInstance.ts`, the base URL is set to point to your Spring Boot backend:

```typescript
// Base URL for API requests
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})
```

> ⚠️ **Note:** If your backend is running on a different port, or if you've changed the `server.servlet.context-path` in `application.properties`, update the `baseURL` here accordingly.

---

### ▶️ Step 3: Run the Frontend

Start the development server:

```bash
npm run dev
```

The frontend will be available at:

```
http://localhost:5173
```

---

## 📥 CSV Import Feature

Connexio supports bulk contact importing via CSV upload.

### 📄 Sample CSV

A sample CSV file is provided at `frontend/csv/book.csv`. Use this file to test the import feature.

### 📋 CSV Format

Your CSV must include the following **required columns**:

| Column | Description |
|---|---|
| `firstName` | Contact's first name |
| `lastName` | Contact's last name |
| `email_1` | Primary email address |
| `email_label_1` | Label for primary email (e.g. `work`, `personal`) |
| `phone_1` | Primary phone number |
| `phone_label_1` | Label for primary phone (e.g. `mobile`, `home`) |

The following columns are **optional**:

| Column | Description |
|---|---|
| `email_2` | Secondary email address |
| `email_label_2` | Label for secondary email |
| `phone_2` | Secondary phone number |
| `phone_label_2` | Label for secondary phone |
| `linkedin_url` | LinkedIn profile URL |
| `facebook_url` | Facebook profile URL |
| `instagram_url` | Instagram profile URL |

### ✅ How to Import

1. Log in to Connexio.
2. Navigate to the **Contacts** section.
3. Click the **Import CSV** button.
4. Select your CSV file (you can use `frontend/csv/book.csv` as a starting point).
5. Review the parsed preview and confirm the import.
6. Make sure you format phone cells as Text for, phone numbers else it will import phone incorrectly 

> ⚠️ **Note:** Make sure your CSV headers exactly match the column names listed above. The first row of the file must be the header row.
