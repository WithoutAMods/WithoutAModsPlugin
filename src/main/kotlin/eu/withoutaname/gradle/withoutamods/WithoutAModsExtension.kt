package eu.withoutaname.gradle.withoutamods

import org.gradle.api.provider.Property

abstract class WithoutAModsExtension {

    abstract val modConfig: ModConfig
    fun mod(block: ModConfig.() -> Unit) {
        modConfig.apply(block)
    }

    abstract class ModConfig {

        abstract val group: Property<String>
        abstract val version: Property<String>
    }

}