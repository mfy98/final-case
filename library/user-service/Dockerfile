# Use a JDK 21 base image
FROM amazoncorretto:21

# Set working directory
WORKDIR /app

# Copy the fat JAR produced by Maven
COPY target/*.jar app.jar

# Expose the port (override in compose if needed)
ARG SERVICE_PORT
EXPOSE ${SERVICE_PORT}
 
# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
