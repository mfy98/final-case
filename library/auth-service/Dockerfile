FROM amazoncorretto:21

WORKDIR /app

COPY target/*.jar app.jar

ARG SERVICE_PORT
EXPOSE ${SERVICE_PORT}

ENTRYPOINT ["java","-jar","/app/app.jar"]
