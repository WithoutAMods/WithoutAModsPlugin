package eu.withoutaname.gradle.withoutamods

import net.minecraftforge.gradle.common.util.RunConfig
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.UserDevPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.parchmentmc.librarian.forgegradle.LibrarianForgeGradlePlugin

class WithoutAModsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val unset = "" // TODO
        target.apply(block = {
            val config = extensions.create<WithoutAModsExtension>("withoutamod")

            apply<UserDevPlugin>()
            apply<LibrarianForgeGradlePlugin>()
            apply<MavenPublishPlugin>()
            apply<KotlinPluginWrapper>()

            group = config.modConfig.group.get()
            version = config.modConfig.version.get()

            configure<JavaPluginExtension> {
                toolchain.languageVersion.set(JavaLanguageVersion.of(17))
            }

            configure<UserDevExtension> {
                mappings(unset, unset)

                runs {
                    fun RunConfig.default() {
                        workingDirectory(target.file("run"))
                        property("forge.logging.markers", "REGISTRIES")
                        property("forge.logging.console.level", "debug")
                        mods {
                            create(unset) {
                                source(the<JavaPluginExtension>().sourceSets["main"])
                            }
                        }
                    }
                    create("client") {
                        default()
                    }
                    create("server") {
                        default()
                    }
                    create("data") {
                        default()
                        args(
                            "--mod",
                            unset,
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
                mavenCentral()
                maven("https://thedarkcolour.github.io/KotlinForForge/")
            }

            dependencies {
                "minecraft"("net.minecraftforge:forge:$unset")
                add("implementation", the<DependencyManagementExtension>().deobf("\"thedarkcolour:kotlinforforge:1.16.0\""))
            }

            tasks.named("jar").get().finalizedBy("reobfJar")
            configure<PublishingExtension> {
                publications {
                    create<MavenPublication>(unset) {
                        from(components["java"])
                    }
                }
                repositories {
                    maven {
                        url = uri(
                            "https://withoutaname.eu/maven/${
                                if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"
                            }"
                        )
                        credentials {
                            username = System.getenv("MAVEN_USER") ?: ""
                            password = System.getenv("MAVEN_PASSWORD") ?: ""
                        }
                    }
                }
            }
        })
    }
}