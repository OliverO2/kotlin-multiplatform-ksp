@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        withJava()
    }

    js(IR) {
        nodejs()
        binaries.executable()
    }
}
