import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    idea
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("kapt")
}

springBoot {
    mainClass.set("com.example.resource.ResourceApplicationKt")
    buildInfo()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
