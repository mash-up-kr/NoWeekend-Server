import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":noweekend-core:core-domain"))
    implementation(project(":noweekend-clients:client-apple"))
    implementation(project(":noweekend-clients:client-kakao"))
    implementation(project(":noweekend-clients:client-mcp"))
    implementation(project(":noweekend-support:monitoring"))
    implementation(project(":noweekend-support:logging"))
    implementation(project(":noweekend-storage:db-core"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")

    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    testImplementation(project(":noweekend-tests:api-docs"))
}

tasks.named<BootJar>("bootJar") {
    enabled = true
    dependsOn("copyApiDocument")

    from("src/main/resources/static/docs") {
        into("static/docs")
    }
}

tasks.getByName("jar") {
    enabled = false
}
