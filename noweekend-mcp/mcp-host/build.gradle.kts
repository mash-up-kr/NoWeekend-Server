plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
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
    maven("https://repo.spring.io/libs-milestone-local")
    maven("https://repo.spring.io/snapshot")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencyManagement {
    imports {
        // Spring AI BOM 업데이트
        mavenBom("org.springframework.ai:spring-ai-bom:1.0.0-M7")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // MCP 클라이언트 스타터 의존성 (자동 구성 의존성은 필요에 따라 추가)
    implementation("org.springframework.ai:spring-ai-starter-mcp-client")

    // 만약 자동 구성을 사용하고 싶다면 다음 의존성도 추가할 수 있습니다.
    implementation("org.springframework.ai:spring-ai-autoconfigure-mcp-client")

    implementation("io.modelcontextprotocol.sdk:mcp:0.9.0")
    implementation("io.modelcontextprotocol.sdk:mcp-spring-webmvc:0.9.0")

    implementation("org.springframework.ai:spring-ai-starter-model-anthropic")

    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
    testImplementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    // LangChain4j Spring Starter
    implementation("dev.langchain4j:langchain4j-spring-boot-starter:1.0.0-beta5")

    // LangGraph4j core
    implementation("org.bsc.langgraph4j:langgraph4j-core:1.5.12")
}
configurations.all {
    resolutionStrategy {
        force("io.modelcontextprotocol.sdk:mcp:0.8.0")
        force("io.modelcontextprotocol.sdk:mcp-spring-webflux:0.8.0")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
