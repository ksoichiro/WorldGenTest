plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.26-beta" apply false
    id("net.minecraftforge.gradle") version "6.0.24" apply false
}

val minecraftVersion = "1.21.1"

architectury {
    minecraft = minecraftVersion
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    group = "com.example"
    version = "1.0.0"

    repositories {
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.parchmentmc.org/")
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

subprojects {
    if (project.name != "neoforge") {
        apply(plugin = "dev.architectury.loom")

        extensions.configure<net.fabricmc.loom.api.LoomGradleExtensionAPI> {
            silentMojangMappingsLicense()
        }

        dependencies {
            "minecraft"("com.mojang:minecraft:$minecraftVersion")
            "mappings"(project.extensions.getByType<net.fabricmc.loom.api.LoomGradleExtensionAPI>().officialMojangMappings())
        }
    }
}