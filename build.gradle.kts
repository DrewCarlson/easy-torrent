plugins {
    kotlin("multiplatform") version "1.6.10-RC"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

kotlin {
    listOf(
        linuxX64(),
        mingwX64(),
        macosX64(),
    ).forEach { target ->
        target.binaries {
            executable {
                entryPoint = "easytorrent.main"

                when (target.name) {
                    "mingwX64" -> {
                        val mingwPath = File(System.getenv("MINGW64_DIR") ?: "C:/msys64/mingw64")
                        linkerOpts("-L${mingwPath.resolve("lib")}")
                        runTask?.environment("PATH" to mingwPath.resolve("bin"))
                    }
                    "linuxX64" -> {
                        linkerOpts("-L/usr/lib64", "-L/usr/lib/x86_64-linux-gnu", "-lcurl")
                    }
                    "macosX64" -> {
                        linkerOpts("-L/opt/local/lib", "-L/usr/local/opt/curl/lib", "-lcurl")
                    }
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.drewcarlson:mobiuskt-core:0.3.0")
                implementation("org.drewcarlson:mobiuskt-coroutines:0.3.1-SNAPSHOT")
                implementation("org.drewcarlson:torrentsearch:0.1.1")
                implementation("org.drewcarlson:qbittorrent-client:0.2.0")
                implementation("com.github.ajalt.mordant:mordant:2.0.0-beta4")
                implementation("me.archinamon:file-io:1.3.4")
                implementation("io.ktor:ktor-client-core") {
                    version { strictly("1.6.2-native-mm-eap-196") }
                }
                implementation("io.ktor:ktor-client-curl:1.6.2-native-mm-eap-196")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core") {
                    version { strictly("1.6.0-RC2") }
                }
            }
        }
    }
}
