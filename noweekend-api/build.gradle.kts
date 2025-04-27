tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":noweekend-core"))
    implementation(project(":noweekend-application"))
    implementation(project(":noweekend-infra"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}