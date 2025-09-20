plugins {
    id("dev.architectury.loom") version "1.11-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    implementation(project(":common"))
}

// リソースをcommonプロジェクトから継承
loom {
    if (project(":common").extensions.findByType<net.fabricmc.loom.api.LoomGradleExtensionAPI>()?.accessWidenerPath != null) {
        accessWidenerPath = project(":common").loom.accessWidenerPath
    }
}

// commonプロジェクトのリソースをprocessResourcesに含める
tasks.processResources {
    from(project(":common").sourceSets.main.get().resources)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version
        )
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "${project.property("archives_base_name")}-${project.name}"
            from(components["java"])
        }
    }
}