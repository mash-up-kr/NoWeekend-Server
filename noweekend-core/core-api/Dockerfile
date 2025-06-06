FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/core-api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]