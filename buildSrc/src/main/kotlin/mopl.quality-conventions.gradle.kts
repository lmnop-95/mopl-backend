plugins {
    id("com.diffplug.spotless")
    checkstyle
    id("com.github.spotbugs")
    jacoco
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

spotless {
    java {
        eclipse().configFile(rootProject.file("config/eclipse/eclipse-java-formatter.xml"))
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

checkstyle {
    toolVersion = libs.findVersion("checkstyle").get().requiredVersion
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    maxWarnings = 0
}

spotbugs {
    effort = com.github.spotbugs.snom.Effort.MAX
    reportLevel = com.github.spotbugs.snom.Confidence.MEDIUM
    excludeFilter = rootProject.file("config/spotbugs/spotbugs-exclude.xml")
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports.maybeCreate("html").required.set(true)
    reports.maybeCreate("xml").required.set(true)
}

val jacocoExclusions = listOf(
    "**/*Application.class",
    "**/*Config.class",
    "**/*Config$*.class",
    "**/*Properties.class",
    "**/*Properties$*.class",
)

tasks.withType<JacocoReport>().configureEach {
    dependsOn(tasks.named("test"))
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        classDirectories.files.map { fileTree(it) { exclude(jacocoExclusions) } }
    )
}
