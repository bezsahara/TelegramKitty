plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.0"
    id("org.jetbrains.dokka") version "1.9.20"
    `maven-publish`
    signing
}

group = "org.bezsahara"
version = "1.0"

repositories {
    mavenCentral()
}

val ktor_version: String = "2.3.11"

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "org.bezsahara"
            artifactId = "kittybot"
            version = project.version.toString()

            artifact(tasks.named("dokkaJavadocJar"))
            artifact(tasks.named("kotlinSourcesJar"))


            pom {
                name.set("TelegramKitty")
                description.set("A sample Kotlin project for demonstrating Maven publishing")
                url.set("https://github.com/bezsahara/TelegramKitty")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("bezsahara")
                        name.set("Hlib")
                        email.set("bezsahara888@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/bezsahara/TelegramKitty.git")
                    developerConnection.set("scm:git:ssh://github.com/bezsahara/TelegramKitty.git")
                    url.set("https://github.com/bezsahara/KotlinPublishTT")
                }
            }
        }
    }
    repositories {
        maven {
            name = "localRepo"
            url = file("../generated").toURI()
        }
    }
}

tasks.named("generateMetadataFileForMavenJavaPublication") {
    dependsOn("kotlinSourcesJar")
}

val iniPath: String? = System.getenv("SK_P")

if (iniPath != null) {
    val signingKey = file("$iniPath\\bez\\d1_SECRET.asc").readText()
    val passPhrase = file("$iniPath\\bez\\pass_phrase.txt").readText().trim()
    signing {
        useInMemoryPgpKeys(signingKey, passPhrase)
        sign(publishing.publications["mavenJava"])
    }
}
