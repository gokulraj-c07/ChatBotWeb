Technologies and Tools Used:-
-----------------------------
1. Backend :
    * Java 17 (Install on your machine)
    * Spring Boot 4
    * Spring Web
    * Spring Data JPA
    * Maven
    * Lombok
2. Frontend :
    * HTML
    * CSS
    * JavaScript
    * Thymeleaf
3. Database :
    * MySQL (Install on your machine)

Operating System:-
-----------------
    * This project is mainly designed for: Windows OS
    * Some features use PowerShell commands and Windows services.

Project Structure:-
-------------------
        * bot/
        │
        ├── src/
        │   ├── main/
        │   │   ├── java/
        │   │   │   └── com/
        │   │   │       └── app/
        │   │   │           └── bot/      # Main application package
        │   │   │               ├── controller/   # REST Controllers
        │   │   │               ├── service/      # Service layer
        │   │   │               ├── repository/   # JPA Repositories
        │   │   │               ├── model/        # JPA Entities
        │   │   │               ├── dto/          # Data Transfer Objects
        │   │   │               └── config/       # Configuration
        │   │   │
        │   │   ├── resources/
        │   │   │   ├── application.properties   # Database config
        │   │   │   ├── static/              # CSS, JS, Images
        │   │   │   └── templates/             # HTML Templates (Thymeleaf)
        │   │   └── webapp/                    # Web application (if Spring Boot < 2.0)
        │   │
        │   └── test/                          # Test classes
        │
        ├── pom.xml                            # Maven configuration
        └── README.md

Database Configuration:-
------------------------

Step 1: Install MySQL
Install:
    - MySQL Server
    - MySQL Workbench

Step 2: Create Database
Run this SQL query: CREATE DATABASE bot;

Step 3: Update application.properties
Open: src/main/resources/application.properties
Update the database credentials:
    *spring.datasource.url=jdbc:mysql://localhost:3306/bot
    *spring.datasource.username=your username
    *spring.datasource.password=your password

How to Run the Project:-
------------------------

1.Open the terminal on your IDE
2.Navigate to the project directory
3.Run the command: .\mvnw clean install
4.Run the command: .\mvnw spring-boot:run