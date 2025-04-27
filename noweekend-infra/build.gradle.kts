tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(project(":noweekend-core"))
    implementation(project(":noweekend-application"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-hc5")
    implementation("io.github.openfeign:feign-micrometer")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
}
