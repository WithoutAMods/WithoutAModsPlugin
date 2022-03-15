plugins {
    kotlin("jvm") version "1.5.21"
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    jacoco
}

group = "eu.withoutaname.gradle.plugin"
version = "1.0.0-alpha.3"

gradlePlugin {
    plugins {
        create("WithoutAMods") {
            id = "eu.withoutaname.gradle.mods.withoutamods"
            implementationClass = "eu.withoutaname.gradle.withoutamods.WithoutAModsPlugin"
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.parchmentmc.org")
}

dependencies {
    implementation("net.minecraftforge.gradle", "ForgeGradle", "5.1.31")
    implementation("org.parchmentmc", "librarian", "1.2.0")
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.6.10")

    testImplementation("org.assertj", "assertj-core", "3.22.0")
    testImplementation("org.mockito.kotlin", "mockito-kotlin", "4.0.0")

    val junit = "5.8.2"
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junit)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junit)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junit)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        csv.required.set(false)
        html.required.set(false)
        xml.required.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            from(components["java"])
            pom {
                licenses {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri(
                "https://withoutaname.eu/maven/${
                    if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "gradle"
                }"
            )
            credentials {
                username = System.getenv("MAVEN_USER") ?: ""
                password = System.getenv("MAVEN_PASSWORD") ?: ""
            }
        }
    }
}
