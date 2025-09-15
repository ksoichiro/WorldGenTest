plugins {
    id("net.neoforged.moddev")
}

neoForge {
    version = project.property("neoforge_version").toString()

    mods {
        create("worldgentest") {
            sourceSet(sourceSets.main.get())
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

    // NeoForge固有の実装に必要な依存関係は自動的に追加される
}

tasks.processResources {
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