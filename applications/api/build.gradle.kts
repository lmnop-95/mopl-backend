plugins {
    id("mopl.spring-boot-application")
}

dependencies {
    implementation(project(":shared:common"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
}
