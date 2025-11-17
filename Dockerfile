FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/apitcc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE $PORT
CMD java -Dserver.port=${PORT:-8080} -jar app.jar
