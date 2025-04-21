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

	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.google.guava:guava:32.1.2-jre")

}

group = "com.sirsmurfy2.skextended"
version = "1.0.0"
description = "A Skript Addon to extend Skript's reach."

val skriptVersion = "2.11.0"
val addonPath = "build/libs/SkExtended-$version.jar"
val testScriptsPath = "src/test/scripts"
val extraPluginsPath = "extra-plugins"

fun createTestTask(action : String) {
	tasks.register<JavaExec>(action + "Vanilla") {
		group = "execution"
		environment("INPUT_TEST_SCRIPT_DIRECTORY", testScriptsPath)
		environment("INPUT_EXTRA_PLUGINS_DIRECTORY", extraPluginsPath)
		environment("INPUT_SKRIPT_REPO_REF", skriptVersion)
		environment("INPUT_SKRIPT_TEST_ACTION", action)
		environment("INPUT_RUN_VANILLA_TESTS", "true")
		environment("INPUT_ADDON_JAR_PATH", addonPath)

		mainClass.set("com.sirsmurfy2.skextended.SkriptTestEnvironment")
		classpath = sourceSets["main"].runtimeClasspath
	}
	tasks.register<JavaExec>(action) {
		group = "execution"
		environment("INPUT_TEST_SCRIPT_DIRECTORY", testScriptsPath)
		environment("INPUT_EXTRA_PLUGINS_DIRECTORY", extraPluginsPath)
		environment("INPUT_SKRIPT_REPO_REF", skriptVersion)
		environment("INPUT_SKRIPT_TEST_ACTION", action)
		environment("INPUT_RUN_VANILLA_TESTS", "false")
		environment("INPUT_ADDON_JAR_PATH", addonPath)

		mainClass.set("com.sirsmurfy2.skextended.SkriptTestEnvironment")
		classpath = sourceSets["main"].runtimeClasspath
	}
}

createTestTask("quickTest")
createTestTask("skriptTestDev")
createTestTask("skriptTestJava17")
createTestTask("skriptTestJava21")


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
