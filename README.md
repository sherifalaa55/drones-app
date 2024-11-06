
# Drones App

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK) 17 or higher**: [Download JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- **Gradle**: Ensure Gradle is installed. Alternatively, use the Gradle Wrapper included in the project.
- **Docker**: [Download and Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: Typically bundled with Docker Desktop.

## Getting Started

Follow these instructions to set up and run the Drone Management Application on your local machine.

### Using Gradle

1. **Build the Application**

   Ensure you're in the project root directory and run:

   ```bash
   ./gradlew clean build
   ```

   This command compiles the project, runs tests, and packages the application into a JAR file located in `build/libs/`.

2. **Run the Application**

   ```bash
   ./gradlew bootRun
   ```

   The application will start on port `8080` by default.

3. **Access the Application**

   Open your browser or use `curl` to access:

   ```
   http://localhost:8080
   ```

---

### Using Docker Compose

Docker Compose simplifies the process by orchestrating both the application and PostgreSQL services.

1. **Ensure `docker-compose.yml` is Present**

   The provided `docker-compose.yml` file sets up both the Spring Boot application and PostgreSQL 16.

2. **Build and Start the Services**

   In the project root directory, run:

   ```bash
   docker-compose up -d
   ```

   This command builds the Docker images (if not already built) and starts the containers.

3. **Access the Application**

   ```
   http://localhost:8080
   ```

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Docker](https://www.docker.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [Gradle](https://gradle.org/)

---
