plugins {
    id("com.android.application") version "8.8.0" apply false
    kotlin("android") version "2.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.55" apply false
    kotlin("kapt") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}