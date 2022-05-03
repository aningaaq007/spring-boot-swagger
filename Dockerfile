FROM openjdk:11-jre-slim

ENV TZ=Europe/Moscow
ARG TIMEZONE=Europe/Moscow

ARG JAR_FILE=./swagger-0.0.1-SNAPSHOT.jar
RUN mkdir /app
COPY ${JAR_FILE} /app/
COPY ./application.properties /app/
WORKDIR /app

ENTRYPOINT ["java","-Dspring.config.location=classpath:file:/app/application.properties","-jar","/app/swagger-0.0.1-SNAPSHOT.jar"]