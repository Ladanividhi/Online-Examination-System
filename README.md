# Online Examination System

A Spring Boot-based Online Examination System with secure user authentication, exam management, and assessment capabilities.

## 🚀 Features

- *User Management*
  - Secure authentication and authorization
  - Role-based access (Teacher, Student)
  - User profile management

- *Exam Management*
  - Create and manage multiple exams
  - Question bank management
  - Multiple question types support

- *Result Processing*
  - Result analysis
  - Performance statistics
  - Result history tracking

## 🛠 Tech Stack

- *Backend:* Spring Boot 3.x
- *Security:* Spring Security
- *Database:* MySQL
- *Build Tool:* Maven

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 🔧 Installation & Setup

1. *Clone the repository*
   git clone https://github.com/Ladanividhi/Online-Examination-System.git and then
   cd online-exam
   

2. *Configure MySQL*
   - Create a new database
   sql
   CREATE DATABASE exam2;
   
   - Update application.properties with your database credentials

3. *Build and Run*
   mvn clean install
   mvn spring-boot:run
   

4. *Access the application*
   
   http://localhost:8080
   

## 🔑 API Endpoints

### Authentication
- POST /api/users - Register new user

### Exams
- GET /api/exams - List all exams
- POST /api/exams - Create new exam
- PUT /api/exams/{id} - Update exam
- DELETE /api/exams/{id} - Delete exam

### Questions
- GET /api/questions - List questions
- POST /api/questions - Add question
- PUT /api/questions/{id} - Update question
- DELETE /api/questions/{id} - Delete question

### Results
- GET /api/results - View results
- POST /api/results - Add result

## 🔐 Security

- Authentication
- Password encryption using BCrypt
- Role-based access control

## 📝 Database Schema

The system uses the following main entities:
- Users
- Exams
- Questions
- Results
- Roles


## 🙏 Thank You
Thank you for exploring the Online Examination System. Your feedback and contributions are always welcome!
