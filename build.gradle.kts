import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.about.libraries)
    alias(libs.plugins.sql.delight)
    alias(libs.plugins.kotlin.serialization)
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
    implementation(libs.sl4j.api)
    implementation(libs.sl4j.logback)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.compose.resource)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.auth)
}

compose.desktop {
    application {
        mainClass = "ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DroidSense"
            modules("java.sql")
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