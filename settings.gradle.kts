rootProject.name = "worldgentest"

include("common")
include("neoforge")
include("fabric")
// include("forge")  // 一時的にコメントアウト

pluginManagement {
    repositories {
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.parchmentmc.org/")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.parchmentmc.org/")
        mavenCentral()
    }
}