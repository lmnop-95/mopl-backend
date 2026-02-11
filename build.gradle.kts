plugins {
    jacoco
}

listOf("applications", "core", "shared", "infrastructure").forEach { name ->
    project(name) {
        tasks.configureEach { enabled = false }
    }
}

val jacocoExclusions = listOf(
    "**/*Application.class",
    "**/*Config.class",
    "**/*Config$*.class",
    "**/*Properties.class",
    "**/*Properties$*.class",
)

tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Generates aggregated JaCoCo coverage report for all modules."

    dependsOn(
        rootProject.childProjects.values
            .flatMap { it.childProjects.values }
            .map { "${it.path}:test" }
    )

    executionData.setFrom(
        fileTree(rootDir) {
            include("**/build/jacoco/test.exec")
            exclude("buildSrc/**")
        }
    )

    sourceDirectories.setFrom(
        fileTree(rootDir) {
            include(
                "shared/*/src/main/java/**",
                "applications/*/src/main/java/**",
                "core/*/src/main/java/**",
                "infrastructure/*/src/main/java/**",
            )
        }
    )

    classDirectories.setFrom(
        fileTree(rootDir) {
            include(
                "shared/*/build/classes/java/main/**",
                "applications/*/build/classes/java/main/**",
                "core/*/build/classes/java/main/**",
                "infrastructure/*/build/classes/java/main/**",
            )
            exclude(jacocoExclusions)
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
