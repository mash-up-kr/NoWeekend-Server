rootProject.name = "spring-boot-kotlin-template"

include(
    "noweekend-core:core-domain",
    "noweekend-core:core-api",
    "noweekend-storage:db-core",
    "noweekend-tests:api-docs",
    "noweekend-support:logging",
    "noweekend-support:monitoring",
    "noweekend-clients:client-oauth",
    "noweekend-clients:client-mcp"
)

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val asciidoctorConvertVersion: String by settings
    val ktlintVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "org.asciidoctor.jvm.convert" -> useVersion(asciidoctorConvertVersion)
                "org.jlleitschuh.gradle.ktlint" -> useVersion(ktlintVersion)
            }
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
include("noweekend-clients:client-oauth")
findProject(":noweekend-clients:client-oauth")?.name = "client-oauth"
include("noweekend-clients:client-mcp")
findProject(":noweekend-clients:client-mcp")?.name = "client-mcp"
