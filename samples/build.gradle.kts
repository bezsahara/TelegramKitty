plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"
}

group = "org.bezsahara.samples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":kittybot"))
    implementation(project(":kittybot-client"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(24)
}

tasks.register<JavaExec>("runMainKt") {
    group = "application"
    description = "Runs org.bezsahara.samples.MainKt with the samples runtime classpath"
    dependsOn(tasks.classes)
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.bezsahara.samples.MainKt")
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(24))
    })
    environment("BOT_TOKEN", System.getenv("BOT_TOKEN") ?: "123:ABC")
}

