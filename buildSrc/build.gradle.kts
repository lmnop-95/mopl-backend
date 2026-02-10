plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.spring.boot.get()}")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:${libs.versions.spring.dependency.management.get()}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${libs.versions.spotless.get()}")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:${libs.versions.spotbugs.plugin.get()}")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${libs.versions.sonarqube.get()}")
}
