
plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.9.20"
    kotlin("plugin.serialization") version "2.0.0"
    signing
    `maven-publish`
}

group = "org.bezsahara.samples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":kittybot"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
    implementation("io.ktor:ktor-server-core:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("io.ktor:ktor-server-netty:2.3.12")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}

