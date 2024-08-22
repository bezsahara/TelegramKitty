plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "TelegramKitty"
if (file("./network/config/genApi").exists()) {
    includeBuild("./GenerateAPI")
}
include("samples")
include("kittybot")
