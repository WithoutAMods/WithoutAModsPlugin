package eu.withoutaname.gradle.withoutamods.extensions

import org.gradle.api.provider.Property

abstract class WithoutAModsExtension {
    abstract val modid: Property<String>
    abstract val group: Property<String>
    abstract val version: Property<String>
    abstract val mappingsChannel: Property<String>
    abstract val mappingsVersion: Property<String>
    abstract val forgeVersion: Property<String>

}