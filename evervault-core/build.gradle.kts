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
            kotlinOptions.jvmTarget = "11"
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
                implementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("Evervault")
                description.set("A library of shared Evervault functionality for Mobile SDK's")
                url.set("https://github.com/evervault/evervault-multiplatform")
                developers {
                    developer {
                        name.set("engineering")
                        organization.set("Evervault")
                        email.set("engineering@evervault.com")
                    }
                }
                scm {
                    connection.set("scm:git:ssh://git@github.com:evervault/evervault-multiplatform.git")
                    url.set("https://github.com/evervault/evervault-multiplatform")
                }
                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://mit-license.org")
                    }
                }
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
}
