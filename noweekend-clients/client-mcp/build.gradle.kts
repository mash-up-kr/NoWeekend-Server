dependencies {
    implementation(project(":noweekend-core:core-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")

    implementation("org.springframework.boot:spring-boot-starter-xml")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
}
