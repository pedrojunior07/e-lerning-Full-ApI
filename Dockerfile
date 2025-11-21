
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
# RUN mvn dependency:go-offline

# Copy the entire source code
COPY src ./src

# Package the application (skip tests if needed)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app
# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


