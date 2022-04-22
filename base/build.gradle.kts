@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
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

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(project("::annotations"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
        }
        val jsMain by getting {
            kotlin.srcDir("build/generated/ksp/js/jsMain/kotlin")
        }
    }
}

dependencies {
    // Provide symbol processing for each Kotlin '*Main' source set.
    kotlin.sourceSets.forEach {
        val kspConfiguration = when {
            it.name == "commonMain" -> "kspMetadata"
            it.name.endsWith("Main") -> "ksp${it.name.substringBefore("Main").capitalize()}"
            else -> null
        }
        if (kspConfiguration != null)
            add(kspConfiguration, project(":symbol-processor"))
    }
}

// Fix KSP task dependencies (https://github.com/google/ksp/issues/963)
afterEvaluate {  // WORKAROUND: both register() and named() fail – https://github.com/gradle/gradle/issues/9331
    tasks {
        val kspKotlinMetadata by getting
        withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
            if (this !== kspKotlinMetadata)
                dependsOn(kspKotlinMetadata)
        }
    }
}
