# KSP Multiplatform Example Project

This project demonstrates how to use the [Kotlin Symbol Processing API](https://github.com/google/ksp) in a JVM/Js multiplatform project requiring code generated per source set.

## Purpose

This project's annotation processor generates a property `qualifiedClassName` for classes annotated with `@Qualifiable`.

`qualifiedClassName` is available on all targets, including `js`, making it useful to create automatically named loggers.

## Quick Start

Execute `./gradlew allTests`

## Notes

This processor is capable of generating separate code per source set.

To do so, `QualifiableProcessor.kt` in the `symbol-processor` subproject contains special hacks to discover input and output source sets, and tailors its code generation accordingly.

This project contains fixes outlined in [Issue #963 · google/ksp](https://github.com/google/ksp/issues/963), but extends the example discussed there by actually having different sets of code generated per source set.

## TODO

* **Source set discovery** hacks should be removed once a better solution is available. This would probably require an API change in KSP, making its current input and output source sets available to the symbol processor's environment. 
* The possibility to support **incremental compilation** is unclear, as outlined in a TODO comment in `QualifiableProcessor.kt`.
