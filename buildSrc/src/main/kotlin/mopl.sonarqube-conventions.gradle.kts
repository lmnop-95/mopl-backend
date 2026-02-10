plugins {
    id("org.sonarqube")
}

sonar {
    properties {
        property("sonar.projectKey", System.getenv("SONAR_PROJECT_KEY") ?: "")
        property("sonar.organization", System.getenv("SONAR_ORGANIZATION") ?: "")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
