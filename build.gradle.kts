import org.gradle.api.tasks.bundling.Zip

plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"
    `maven-publish`
    signing
//    if (File("./network/config/genApi").exists()) {
//        id("org.bezsahara.generateapi")
//    }
}


group = "org.bezsahara"

val kittybotLocalPublishTask = ":kittybot:publishMavenJavaPublicationToLocalRepoRepository"
val kittybotClientLocalPublishTask = ":kittybot-client:publishMavenJavaPublicationToLocalRepoRepository"

fun registerGeneratedRepoArchiveTask(
    taskName: String,
    publishTaskPath: String,
    outputDirName: String,
    archiveName: String,
) = tasks.register<Zip>(taskName) {
    group = "publishing"
    description = "Publishes $publishTaskPath and zips the generated Maven repository."
    dependsOn(publishTaskPath)
    from(layout.projectDirectory.dir(outputDirName)) {
        include("org/**")
    }
    destinationDirectory.set(layout.projectDirectory.dir(outputDirName))
    archiveFileName.set(archiveName)
}

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
    jvmToolchain(17)
}

val zipKittybotGeneratedRepo = registerGeneratedRepoArchiveTask(
    taskName = "zipKittybotGeneratedRepo",
    publishTaskPath = kittybotLocalPublishTask,
    outputDirName = "generated",
    archiveName = "kitty.zip",
)

val zipKittybotClientGeneratedRepo = registerGeneratedRepoArchiveTask(
    taskName = "zipKittybotClientGeneratedRepo",
    publishTaskPath = kittybotClientLocalPublishTask,
    outputDirName = "generated-client",
    archiveName = "client.zip",
)

tasks.register("publishGeneratedRepos") {
    group = "publishing"
    description = "Publishes kittybot and kittybot-client into their generated local repositories."
    dependsOn(kittybotLocalPublishTask, kittybotClientLocalPublishTask)
}

tasks.register("publishAndZipGeneratedRepos") {
    group = "publishing"
    description = "Publishes both libraries and refreshes generated/kitty.zip and generated-client/client.zip."
    dependsOn(zipKittybotGeneratedRepo, zipKittybotClientGeneratedRepo)
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

