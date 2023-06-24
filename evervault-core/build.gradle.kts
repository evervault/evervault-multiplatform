import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.8.21"
    id("io.github.ttypic.swiftklib") version "0.2.1"
    id("maven-publish")
    id("signing")
}

val ktorVersion = "2.3.1"

group = "com.evervault.sdk"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")

                // ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")

                // JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("org.bouncycastle:bcprov-jdk15on:1.70")
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey ?: "", signingPassword ?: "")

    sign(publishing.publications["kotlinMultiplatform"])
}
