import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kover)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.ktfmt)
}

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      testTask {
        useKarma {
          if (isCiEnvironment) {
            useChromeHeadlessNoSandbox()
          } else {
            useChromiumHeadless()
          }
        }
      }
    }
    binaries.executable()
  }

  jvm()

  sourceSets {
    commonMain.dependencies {
      api(projects.common)
      implementation(libs.androidx.lifecycle.runtimeCompose)
      implementation(libs.androidx.lifecycle.viewmodelCompose)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui)
      implementation(libs.compose.uiToolingPreview)
      implementation(libs.ktor.client.auth)
      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.json)
      implementation(libs.ktor.client.logging)
      implementation(libs.ktor.client.serialization)
      implementation(libs.ktor.serialization.kotlinx.json)
      implementation(libs.ktor.client.content.negotiation)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.compose.ui.test)
    }
    wasmJsMain.dependencies { implementation(libs.ktor.client.js) }
    jvmMain.dependencies { implementation(libs.ktor.client.java) }
    jvmTest.dependencies { implementation(compose.desktop.currentOs) }
  }
}

val isCiEnvironment = providers.environmentVariable("CI").isPresent

tasks.named<Test>("jvmTest") { jvmArgs("--enable-native-access=ALL-UNNAMED") }
