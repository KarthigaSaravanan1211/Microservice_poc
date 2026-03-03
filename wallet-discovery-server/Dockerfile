FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
