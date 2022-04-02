package eu.withoutaname.gradle.withoutamods

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

fun Project.publishing() {
    apply<MavenPublishPlugin>()


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
}