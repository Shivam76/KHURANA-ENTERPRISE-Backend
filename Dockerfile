# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build artifact
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/khurana-enterprise-*.jar app.jar

ENV PORT=8087
EXPOSE 8087

# Render provides $PORT env var dynamically
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8087} -jar app.jar"]
