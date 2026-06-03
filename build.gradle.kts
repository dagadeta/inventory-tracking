import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsPlugin

plugins {
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
}

val isCiEnvironment = providers.environmentVariable("CI").isPresent
val isArm64Host = System.getProperty("os.arch").contains("aarch64", ignoreCase = true) ||
        System.getProperty("os.arch").contains("arm64", ignoreCase = true)

if (isCiEnvironment && isArm64Host) {
    // on a GitLab CI runner on a Raspberry Pi, use the local node installation
    allprojects {
        plugins.withType<NodeJsPlugin> {
            the<NodeJsEnvSpec>().download.set(false)
        }
        plugins.withType<WasmNodeJsPlugin> {
            the<WasmNodeJsEnvSpec>().download.set(false)
        }
    }
}
