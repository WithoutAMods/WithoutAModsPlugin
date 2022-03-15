package eu.withoutaname.gradle.withoutamods

import net.minecraftforge.gradle.common.util.RunConfig
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

fun Project.withoutamod(block: Config.() -> Unit = {}) {
    val config = Config().apply(block)

    version = config.version
    group = config.group

    configure<KotlinJvmProjectExtension> {
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    println(
        "Java: ${System.getProperty("java.version")}, JVM: ${System.getProperty("java.vm.version")} (${
            System.getProperty(
                "java.vendor"
            )
        }), Arch: ${System.getProperty("os.arch")}"
    )
    configure<UserDevExtension> {
        mappings(config.mappingsChannel, config.mappingsVersion)

        // accessTransformer = file("src/main/resources/META-INF/accesstransformer.cfg") // Currently, this location cannot be changed from the default.

        runs {
            fun RunConfig.default() {
                workingDirectory(project.file("run"))
                property("forge.logging.markers", "REGISTRIES")
                property("forge.logging.console.level", "debug")
                mods {
                    create(config.modid) {
                        source(the<JavaPluginExtension>().sourceSets["main"])
                    }
                }
            }
            create("client") {
                default()
                property("forge.enabledGameTestNamespaces", config.modid)
            }
            create("server") {
                default()
                property("forge.enabledGameTestNamespaces", config.modid)
            }
            create("gameTestServer") {
                default()
                property("forge.enabledGameTestNamespaces", config.modid)
            }
            create("data") {
                default()
                args(
                    "--mod",
                    config.modid,
                    "--all",
                    "--output",
                    file("src/generated/resources/"),
                    "--existing",
                    file("src/main/resources/")
                )
            }
        }
    }

    configure<JavaPluginExtension> {
        sourceSets["main"].resources { srcDir("src/generated/resources") }
        withJavadocJar()
        withSourcesJar()
    }

    repositories {
    }

    dependencies {
        "minecraft"("net.minecraftforge:forge:${config.forgeVersion}")
    }

    tasks["jar"].finalizedBy("reobfJar")

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
        repositories {
            maven {
                url = uri("file://${project.projectDir}/mcmodsrepo")
            }
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation
    }
}