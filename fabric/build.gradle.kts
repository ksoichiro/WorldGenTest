plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modApi("net.fabricmc.fabric-api:fabric-api:0.102.0+1.21.1")

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