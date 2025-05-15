plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2" apply false
}

group = "noweekend"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
    maven {
        name = "Central Portal Snapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
        mavenBom("org.springframework.ai:spring-ai-bom:1.0.0-M7")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // OpenFeign Client
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    // JSON 매핑용 Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // 기존 MCP / Spring AI 의존성
    implementation("org.springframework.ai:spring-ai-starter-mcp-server")
    implementation("org.springframework.ai:spring-ai-autoconfigure-mcp-server")
    implementation("io.modelcontextprotocol.sdk:mcp:0.9.0")
    implementation("io.modelcontextprotocol.sdk:mcp-spring-webmvc:0.9.0")

    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
}

configurations.all {
    resolutionStrategy {
        force("io.modelcontextprotocol.sdk:mcp:0.9.0")
        force("io.modelcontextprotocol.sdk:mcp-spring-webmvc:0.9.0")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}