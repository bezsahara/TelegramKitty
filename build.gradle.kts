plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"
    `maven-publish`
    signing
    if (File("./network/config/genApi").exists()) {
        id("org.bezsahara.generateapi")
    }
}


group = "org.bezsahara"
version = "1.0"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(24)
}

//tasks
//    .withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>()
//    .configureEach {
//        compilerOptions
//            .languageVersion
//            .set(
//                org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOT
//            )
//    }

