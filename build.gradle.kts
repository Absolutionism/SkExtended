import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
	maven("https://ci.ender.zone/plugin/repository/everything/")
	maven("https://jitpack.io")
	maven("https://raw.githubusercontent.com/Shopkeepers/Repository/main/releases/")
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies { // If you would like to modify anything here, head to gradle/libs.versions.toml
    compileOnly(libs.paper.api)
    compileOnly(libs.skript)
    compileOnly(libs.lombok)
	compileOnly(libs.player.vaults) {
		exclude(group = "org.kitteh", module = "cardboardbox")
		exclude(group = "dev.kitteh", module = "cardboardbox")
		exclude(group = "org.kitteh", module = "paste-gg-api")
	}
	compileOnly(libs.grief.prevention)
	compileOnly(libs.shopkeepers)
    annotationProcessor(libs.lombok)
}

group = "com.sirsmurfy2.skextended"
version = "1.0.0"
description = "A Skript Addon to extend Skript's reach."

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        manifest {
            attributes["Implementation-Version"] = rootProject.version
        }
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
    }

    withType<Javadoc>() {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand(
                "version" to rootProject.version,
            )
        }
    }

    defaultTasks("build")

    // 1.8.8 - 1.16.5 = Java 8
    // 1.17           = Java 16
    // 1.18 - 1.20.4  = Java 17
    // 1-20.5+        = Java 21
    val version = "1.21.4"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    runServer { // You may run this as a simple gradle task, to build your plugin, upload that build, and run a server with the plugins specified.
        minecraftVersion(version)
        runDirectory = rootDir.resolve("build/test_runners/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            url("https://github.com/SkriptLang/Skript/releases/download/2.10.0/Skript-2.10.0.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}
