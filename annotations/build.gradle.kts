@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

kotlin {
    jvm()

    js(IR) {
        nodejs()
    }
}
