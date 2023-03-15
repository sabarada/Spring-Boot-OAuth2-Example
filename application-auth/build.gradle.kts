import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    idea
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("kapt")
}

springBoot {
    mainClass.set("com.example.resource.AuthApplicationKt")
    buildInfo()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

dependencies {
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:0.4.1")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}