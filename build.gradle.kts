listOf("applications", "core", "shared", "infrastructure").forEach { name ->
    project(name) {
        tasks.configureEach { enabled = false }
    }
}
