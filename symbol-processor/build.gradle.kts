plugins {
    kotlin("multiplatform")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project("::annotations"))
                implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.4")
            }
        }
        val jvmTest by getting
    }
}
