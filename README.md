# bus ticketing system

A Java project using Maven, JavaFX, MySQL, and iTextPDF.

## Overview

This project demonstrates a Java application with the following main features:
- **JavaFX** for building the GUI.
- **MySQL Connector** for database operations.
- **iTextPDF** for PDF generation.
- **JUnit 5** for unit testing.

## Prerequisites

- Java 17 or higher
- Maven 3.x
- MySQL Server (if using database features)

## Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd demo1
   ```

2. **Configure Database:**

   - Install and start MySQL Server.
   - Create a new database for the app (e.g., `demo1db`):
     ```sql
     CREATE DATABASE demo1db;
     ```
   - Create a user and grant permissions (replace `username`/`password` as needed):
     ```sql
     CREATE USER 'demo1user'@'localhost' IDENTIFIED BY 'yourpassword';
     GRANT ALL PRIVILEGES ON demo1db.* TO 'demo1user'@'localhost';
     FLUSH PRIVILEGES;
     ```
   - Update the database connection details in your source code (typically in a properties/config file or directly in the Java code). Example JDBC URL:
     ```
     jdbc:mysql://localhost:3306/demo1db
     ```
   - Ensure the MySQL driver is installed (already included as a dependency in `pom.xml`).

   - If the application requires specific tables or schema, create them according to your application's needs. Example:
     ```sql
     CREATE TABLE users (
       id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(255) NOT NULL,
       password VARCHAR(255) NOT NULL
     );
     ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn javafx:run
   ```

## Project Structure

- `src/main/java` - Main application source code.
- `src/test/java` - Test classes.
- `pom.xml` - Maven build configuration.
- `.gitignore` - Ignored files and directories for Git.

## Useful Commands

- **Build:**  
  `mvn clean install`

- **Run (JavaFX):**  
  `mvn javafx:run`

- **Run Tests:**  
  `mvn test`

## Dependencies

- **JavaFX** (`org.openjfx`)
- **MySQL Connector/J** (`mysql:mysql-connector-java`)
- **iTextPDF** (`com.itextpdf:itextpdf`)
- **JUnit 5** (`org.junit.jupiter`)

## Notes

- IDE configuration files for IntelliJ IDEA, Eclipse, and NetBeans are ignored in `.gitignore`.
- If you encounter issues with JavaFX, make sure your Java version matches the requirements in `pom.xml` and you have the required JavaFX SDK modules.

## License

This project is licensed under the Apache License 2.0. See [LICENSE](LICENSE) for details.
