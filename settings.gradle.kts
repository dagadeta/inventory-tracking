rootProject.name = "inventory-tracking"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    // this aggregates coverage reports for the subprojects but has no koverLog task
    // id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.11"
}

include(":backend", ":frontend:shared", ":frontend:webApp", ":frontend:desktopApp", ":common")

//kover {
//    enableCoverage()
//    reports {
//        excludedClasses.add("inventory_tracking.frontend.shared.generated.resources.*")
//    }
//}
