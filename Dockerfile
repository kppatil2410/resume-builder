# ---- Build stage ----
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (layer caching)
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

# ---- Run stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/resume-builder-1.0.0.jar app.jar

# Expose port (Railway uses $PORT env var)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
