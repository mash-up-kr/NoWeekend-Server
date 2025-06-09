plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-tx")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // jasypt
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
    testImplementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}
