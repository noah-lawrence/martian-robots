plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "com.martian.robots"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.martian.robots.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    // The JUnit BOM keeps every JUnit artifact on the same version
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.kotest.assertions.core)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
