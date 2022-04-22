pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.6.10-1.0.4" apply false
        kotlin("multiplatform") version "1.6.10" apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "kotlin-multiplatform-ksp"

include(
    "::annotations",
    ":base",
    ":symbol-processor",
)
