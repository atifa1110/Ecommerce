buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath ("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.0")
        classpath("com.android.tools.build:gradle:7.3.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("io.gitlab.arturbosch.detekt") version("1.23.0") apply false
    id("com.android.library") version "8.1.0" apply false
}
val accompanistSwiperefreshVersion by extra("0.33.1-alpha")
