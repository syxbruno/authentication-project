![Java](https://img.shields.io/badge/JAVA-E65100?style=for-the-badge&labelColor=FFFFFF)
![Spring](https://img.shields.io/badge/SPRING-4CAF50?style=for-the-badge&labelColor=FFFFFF)
![JWT](https://img.shields.io/badge/JWT-8313C2?style=for-the-badge&labelColor=FFFFFF)
![Maven](https://img.shields.io/badge/MAVEN-0074D9?style=for-the-badge&labelColor=FFFFFF)
![Docker](https://img.shields.io/badge/DOCKER-0074D9?style=for-the-badge&labelColor=FFFFFF)
![MySQL](https://img.shields.io/badge/MYSQL-3A6599?style=for-the-badge&labelColor=FFFFFF)
![FlyWay](https://img.shields.io/badge/FLYWAY-C92626?style=for-the-badge&labelColor=FFFFFF)

# authentication-project

This project is a monolithic application focused on user authentication and authorization. It manages the full user lifecycle, from
registration and email verification to password reset and granting different permission levels.

---

## Project Overview

The application uses a monolithic architecture based on the **Spring Boot** ecosystem, providing a robust solution for security management.
The authentication system is built with **JWT (JSON Web Tokens)** for generating and validating access tokens and refresh tokens, ensuring
security and session renewal without requiring multiple logins.

Data persistence is managed with **Spring Data JPA** and the **MySQL** database, with **Flyway** handling schema migrations. The application
and the database are orchestrated with **Docker** to simplify the development environment.

---

## Tech Stack

| Category             | Technology                                              |
|----------------------|---------------------------------------------------------|
| **Language**         | Java 21                                                 |
| **Frameworks**       | Spring (JPA, Validation, Web, DevTools, Security, Mail) |
| **Authentication**   | JWT, OAuth (Google), Google authenticator               |
| **Containerization** | Docker, Google JIB                                      |
| **Database**         | MySQL                                                   |
| **Migrations**       | Flyway                                                  |

---

## Application Endpoints

### HelloController

`GET /student` Returns `"Hello Student"` for users with the **STUDENT** role.  
`GET /teacher` Returns `"Hello Teacher"` for users with the **TEACHER** role.  
`GET /director` Returns `"Hello Director"` for users with the **DIRECTOR** role.

### AuthController

`POST /login` Authenticates a user with `email` and `password` in the request body.  
`POST /register` Creates a new user with `name`, `email`, and `password`.  
`PATCH /verify` Verifies the user's email using a `code` sent in the request body.  
`POST /update-token` Generates a new access token using an existing refresh token.  
`POST /verify-a2f` If A2F authentication is active, it authenticates a user via Google Authenticator.  

### LoginGoogleController

`GET /login/google` Log in a user already registered using `Google`.

### UserController

`PATCH /add-profile/{id}` Adds a role to the user with the specified ID. Requires the **DEVELOPER** role and a body containing
`profilesName` (`STUDENT`, `TEACHER`, or `DIRECTOR`).  
`POST /send-code` Sends an email with a code for password reset.  
`PATCH /alter-password` Resets the user's password with `code`, `newPassword`, and `repeatPassword`.  
`POST /a2f` Enables A2F authentication and generates a qrcode.  
`PATCH /disable-a2f` Disables A2F authentication for the logged in user.  

---

## How to Run the Application

1. **Clone the repository:**

   ```bash
   git clone https://github.com/syxbruno/authentication-project.git
   cd authentication-project

2. **Build the Docker image of the REST API:**

   ```bash
   mvn compile jib:dockerBuild

3. **Start the containers:**

   ```bash
   docker-compose up

4. **REST API URL**

   ```bash
   http://localhost:8080