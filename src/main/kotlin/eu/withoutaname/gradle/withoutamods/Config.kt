package eu.withoutaname.gradle.withoutamods

import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.the
import java.net.URI

@DslMarker
annotation class ConfigDSL

@ConfigDSL
data class Config(
    var modid: String = "modid",
    var group: String = "com.yourname.modid",
    var version: String = "1.0.0",
    var forgeVersion: String = "1.18.2-40.0.15",
    var kotlinForForgeVersion: String = "3.1.0",
    var mappings: Mappings = Mappings(),
    var dependencies: Dependencies = Dependencies()
) {

    fun mappings(block: Mappings.() -> Unit) {
        mappings.apply(block)
    }

    fun dependencies(block: Dependencies.() -> Unit) {
        dependencies.apply(block)
    }
}

@ConfigDSL
data class Mappings(var channel: String = "official", var version: String = "1.18.2") {

    fun official(version: String) {
        channel = "official"
        this.version = version
    }

    fun parchment(version: String) {
        channel = "parchment"
        this.version = version
    }
}

@ConfigDSL
class Dependencies {

    private val mavenRepos: MutableSet<URI> = mutableSetOf()
    val repositoryConfig: MutableList<RepositoryHandler.() -> Unit> =
        mutableListOf({ mavenRepos.forEach { maven(it) } })
    val dependencyConfig: MutableList<DependencyHandlerScope.() -> Unit> = mutableListOf()

    fun maven(url: URI) {
        mavenRepos.add(url)
    }

    fun maven(url: String) {
        maven(URI(url))
    }

    fun mod(group: String, name: String, version: String, url: URI, hasApi: Boolean = false) {
        maven(url)
        dependencies {
            if (hasApi) {
                add("compileOnly", the<DependencyManagementExtension>().deobf("$group:$name:$version:api"))
                add("runtimeOnly", the<DependencyManagementExtension>().deobf("$group:$name:$version"))
            } else {
                add("implementation", the<DependencyManagementExtension>().deobf("$group:$name:$version"))
            }
        }
    }

    fun repositories(block: RepositoryHandler.() -> Unit) {
        repositoryConfig.add(block)
    }

    fun dependencies(block: DependencyHandlerScope.() -> Unit) {
        dependencyConfig.add(block)
    }
}