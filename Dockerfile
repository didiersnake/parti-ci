#build stage
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#Java 21 doesn't have an official alpine JRE image from Eclipse Temurin
#run stage with lighter image
FROM amazoncorretto:21-alpine3.18
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
Run rm -rf /tmp/*
RUN apk add --no-cache poppler-utils
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]