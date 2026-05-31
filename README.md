# Ecommerce-application
Spring boot + React 

## Overview
This is a full-stack e-commerce application built using Spring Boot (backend), React (frontend), and MySQL (database).  
The project supports product management, user operations, authentication, and basic shopping features.

---

## Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security  
- MySQL
- Maven
- Lombok
- Swagger for API documentation

### Frontend
- React.js
- React Router
- React Redux & Redux Toolkit
- Axios
- CSS / TailwindCSS
- Headless UI
- Material UI

### Database
- H2 for development
- MySQL

---

## Features

### User Features
- User registration and login
- Authentication with Jwt Cookies and also Headers (for learning)
- View products
- Product details page

### Product Features
- Add / update / delete products (admin)
- Product listing
- Product image upload
- Pricing with discount support
- Stock availability status

### Backend Features
- REST API architecture
- MySQL integration
- File upload handling
- CORS configuration for frontend communication

---

## Project Structure

### Backend (Spring Boot)
backend/
├── controllers/
├── services/
├── repositories/
├── entities/
├── config/
└── resources/

### Frontend (React)
frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── services/
│   └── App.js

---

## Setup Instructions

### 1. Clone the repository
git clone https://github.com/your-username/ecommerce-app.git


### 2. Backend Setup

Configure database in application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Run backend:
cd backend
mvn spring-boot:run

Backend runs on:
http://localhost:8080

---

### 3. Frontend Setup

cd frontend
npm install
npm start

Frontend runs on:
http://localhost:3000

---

## API Documentation (Swagger)
http://localhost:8080/swagger-ui/index.html

---

## CORS Configuration
Frontend allowed origin:
http://localhost:3000

---

## Postman
Postman collections are located in:
/postman

---

## Future Improvements
- Cart system
- Checkout and payments
- Order history
- Email notifications
- Role-based access (admin/user)
- Deployment (Docker / cloud)

---

## Author
Cristea Bianca Stefania
GitHub: https://github.com/bianca-cristea

---

## License
This project is for educational purposes.