package eu.withoutaname.gradle.withoutamods

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

abstract class WithoutAModsExtension {

    abstract val modConfig: ModConfig
        @Nested
        get

    fun mod(action: Action<ModConfig>) {
        action.execute(modConfig)
    }

    internal fun init() {
        modConfig.init()
    }

    abstract class ModConfig {

        abstract val group: Property<String>
        abstract val version: Property<String>

        fun init() {
            version.convention("1.0.0")
        }
    }

}