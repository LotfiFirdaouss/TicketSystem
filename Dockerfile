# Stage 1: Build the JAR file
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/TicketSystem-0.0.1-SNAPSHOT.jar TS-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "TS-api.jar"]