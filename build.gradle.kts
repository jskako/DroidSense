import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.about.libraries)
    alias(libs.plugins.sqlDelight)
}

group = "com.jskako"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.material.icons)
    implementation(libs.material3)
    implementation(libs.about.libraries.core)
    implementation(libs.about.libraries.compose)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.sqldelight.jvm)
    implementation(libs.sl4j)
}

compose.desktop {
    application {
        mainClass = "ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DroidSense"
            packageVersion = "1.0.0"
            macOS {
                iconFile.set(project.file("icon/mac/icon.icns"))
            }
            windows {
                iconFile.set(project.file("icon/win/icon.ico"))
            }
            linux {
                iconFile.set(project.file("icon/linux/icon.png"))
            }
        }
    }
}

aboutLibraries {
    registerAndroidTasks = false
    prettyPrint = true
}

kotlin {
    sqldelight {
        databases {
            create("DSDatabase") {
                packageName = "com.jskako"
            }
        }
    }
}