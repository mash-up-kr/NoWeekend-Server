FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/mcp-host-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]