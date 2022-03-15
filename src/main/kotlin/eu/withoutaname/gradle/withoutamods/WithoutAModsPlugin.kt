package eu.withoutaname.gradle.withoutamods

import net.minecraftforge.gradle.userdev.UserDevPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.parchmentmc.librarian.forgegradle.LibrarianForgeGradlePlugin

class WithoutAModsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.apply(block = {
            apply<UserDevPlugin>()
            apply<LibrarianForgeGradlePlugin>()
            apply<MavenPublishPlugin>()
            apply<KotlinPluginWrapper>()
            apply(from = "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-3.1.0.gradle")
        })
    }
}