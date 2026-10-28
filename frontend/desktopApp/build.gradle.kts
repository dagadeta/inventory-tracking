import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.gradle.ComposeHotRun

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.frontend.shared)
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)
    implementation(libs.compose.uiToolingPreview)

    runtimeOnly(libs.logback.classic)
}

compose.desktop {
    application {
        mainClass = "de.dagadeta.inventorytracking.frontend.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "de.dagadeta.inventorytracking.frontent"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    jvmArgs = jvmArgs + "--enable-native-access=ALL-UNNAMED"
}

kotlin {
    jvmToolchain(21)
}
