
FROM openjdk:21-ea-17-slim

COPY target/dblog-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the application using a Java JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]