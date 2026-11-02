plugins {
    kotlin("jvm") version "2.4.21"
}

group = "com.martian.robots"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(15)
}

tasks.test {
    useJUnitPlatform()
}