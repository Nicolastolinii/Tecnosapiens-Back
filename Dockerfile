
FROM openjdk:21-ea-17-slim

COPY --from=build target/dblog-0.0.1-SNAPSHOT.jar app.jar



# Start the application using a Java JAR
ENTRYPOINT ["java", "-jar", "app.jar"]