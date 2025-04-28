plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
}

tasks.getByName("jar") {
    enabled = true
}

dependencies {
    implementation(project(":noweekend-core"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}
