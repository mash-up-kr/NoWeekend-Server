plugins {
    kotlin("kapt")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(project(":noweekend-support:logging"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    compileOnly(project(":noweekend-core:core-domain"))
    testImplementation(project(":noweekend-core:core-domain"))

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:${properties["queryDslVersion"]}:jakarta")
    kapt("com.querydsl:querydsl-apt:${properties["queryDslVersion"]}:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // query 값 정렬
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:${properties["p6spyVersion"]}")
}

kapt {
    correctErrorTypes = true
}

val querydslGenerated = file("build/generated/source/kapt/main")
tasks.named("clean") {
    doLast {
        querydslGenerated.deleteRecursively()
    }
}
