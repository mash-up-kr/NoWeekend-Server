FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

COPY build/libs/mcp-host-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]