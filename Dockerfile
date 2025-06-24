FROM gradle:8.11.1-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]