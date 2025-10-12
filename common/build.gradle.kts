plugins {
    id("architectury-plugin")
    id("dev.architectury.loom")
}

architectury {
    common("neoforge", "fabric")
}

dependencies {
    // 純粋なMinecraftクラス（プラットフォーム固有依存関係なし）

    // TerraBlender (compileOnly - ランタイムは各プラットフォームが提供)
    compileOnly("com.github.glitchfiend:TerraBlender-common:${project.property("minecraft_version")}-${project.property("terrablender_version")}")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "${project.property("archives_base_name")}-${project.name}"
            from(components["java"])
        }
    }

    repositories {
        // repositories
    }
}