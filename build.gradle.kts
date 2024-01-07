import com.mikepenz.aboutlibraries.plugin.AboutLibrariesTask
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("com.mikepenz.aboutlibraries.plugin") version "10.10.0"
}

group = "com.jskako"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material:material-icons-extended:1.5.11")
    implementation("org.jetbrains.compose.material3:material3-desktop:1.5.11")
    implementation("com.mikepenz:aboutlibraries-core:10.10.0")
    implementation("com.mikepenz:aboutlibraries-compose-m3:10.10.0")

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
