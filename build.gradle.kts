plugins {
    id("com.android.application") version "7.2.2" apply false
    kotlin("android") version "1.7.0" apply false
    id("dagger.hilt.android.plugin") version "2.43.1" apply false
    kotlin("kapt") version "1.7.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}