import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinxSerialization)
  alias(libs.plugins.ktfmt)
}

kotlin {
  jvm()

  @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }

  sourceSets {
    commonMain.dependencies { implementation(libs.ktor.serialization.kotlinx.json) }
    commonTest.dependencies { implementation(libs.kotlin.test) }
  }
}

kotlin { jvmToolchain(25) }
