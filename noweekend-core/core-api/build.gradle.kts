import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":noweekend-core:core-domain"))
    implementation(project(":noweekend-clients:client-oauth"))
    implementation(project(":noweekend-clients:client-mcp"))
    implementation(project(":noweekend-support:monitoring"))
    implementation(project(":noweekend-support:logging"))
    implementation(project(":noweekend-storage:db-core"))

    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(project(":noweekend-tests:api-docs"))

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0") {
        exclude(group = "io.swagger.core.v3", module = "swagger-annotations")
    }
    implementation("io.swagger.core.v3:swagger-annotations:2.2.15")
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
