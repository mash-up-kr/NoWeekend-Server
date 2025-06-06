dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    // jasypt
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
    testImplementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}
