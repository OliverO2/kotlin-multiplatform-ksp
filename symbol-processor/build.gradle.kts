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
    }

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
