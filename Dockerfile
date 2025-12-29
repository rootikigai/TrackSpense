# Use Java 17 runtime
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built jar from target
COPY deploy/TrackSpense-1.0-SNAPSHOT.jar app.jar

# Expose port (Render will override)
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "app.jar"]
