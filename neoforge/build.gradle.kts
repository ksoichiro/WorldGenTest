plugins {
    id("net.neoforged.moddev")
}

neoForge {
    version = project.property("neoforge_version").toString()

    mods {
        create("worldgentest") {
            sourceSet(sourceSets.main.get())
            // commonプロジェクトのソースも含める
            sourceSet(project(":common").sourceSets.main.get())
        }
    }

    runs {
        create("client") {
            client()
            gameDirectory = file("run")
        }

        create("server") {
            server()
            gameDirectory = file("run-server")
        }
    }
}

dependencies {
    implementation(project(":common"))

    // TerraBlender
    implementation("com.github.glitchfiend:TerraBlender-neoforge:${project.property("minecraft_version")}-${project.property("terrablender_version")}")

    // NeoForge固有の実装に必要な依存関係は自動的に追加される
}

tasks.processResources {
    from(project(":common").sourceSets.main.get().resources)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    inputs.property("version", project.version)

    filesMatching("META-INF/neoforge.mods.toml") {
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