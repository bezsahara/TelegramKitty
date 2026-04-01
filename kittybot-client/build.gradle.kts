plugins {
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.serialization") version "2.3.20"
    id("org.jetbrains.dokka") version "1.9.20"
    `maven-publish`
    signing
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
//    implementation("org.slf4j:slf4j-api:2.0.17")
//    implementation("ch.qos.logback:logback-classic:1.5.19")
    implementation("io.vertx:vertx-core:5.0.8")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:5.0.8")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    compileOnly(project(":kittybot"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
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
            artifactId = "kittybot-client"
            version = project.version.toString()

            artifact(tasks.named("dokkaJavadocJar"))
            artifact(tasks.named("kotlinSourcesJar"))


            pom {
                name.set("TelegramKitty")
                description.set("Library to create Telegram bots")
                url.set("https://github.com/bezsahara/TelegramKitty")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/license/mit")
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
                    url.set("https://github.com/bezsahara/TelegramKitty")
                }
            }
        }
    }
    repositories {
        maven {
            name = "localRepo"
            url = file("../generated-client").toURI()
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
