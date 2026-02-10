@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

rootProject.name = "mopl-backend"

include(
    "applications:api",
    "applications:websocket",
    "applications:sse",
    "applications:batch",
    "applications:scheduler",
    "applications:consumer",
)

include(
    "core:user",
    "core:content",
    "core:review",
    "core:playlist",
    "core:follow",
    "core:conversation",
    "core:notification",
    "core:watching",
)

include(
    "shared:common",
)

include(
    "infrastructure:persistence",
    "infrastructure:redis",
    "infrastructure:kafka",
    "infrastructure:mail",
    "infrastructure:storage",
    "infrastructure:external-api",
)
