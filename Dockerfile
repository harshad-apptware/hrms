# Use OpenJDK 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better layer caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Install system dependencies
RUN apt-get update && apt-get install -y curl
#
## Install dotenvx from the official script
#RUN curl -fsSL https://dotenvx.sh/install.sh | sh

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/hrms-0.0.1-SNAPSHOT.jar"]

