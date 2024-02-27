# Base image for building the application
FROM openjdk:11-slim AS build

# Copy source code
COPY COPY ./src /home/app/src/

# Copy POM file
COPY ./pom.xml /home/app

# Build the project using Maven
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-slim

# Copy the application jar from build stage
COPY --from=build /home/app/target/dblog-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar

# Expose port 8080
EXPOSE 8080

# Start the application using a Java JAR
ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]
