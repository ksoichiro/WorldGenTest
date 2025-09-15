plugins {
    id("architectury-plugin")
    id("dev.architectury.loom")
}

architectury {
    common("neoforge", "fabric")
}

dependencies {
    // 純粋なMinecraftクラス（プラットフォーム固有依存関係なし）
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