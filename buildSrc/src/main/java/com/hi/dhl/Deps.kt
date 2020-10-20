package com.hi.dhl

object Config {
    val compileSdkVersion = 29
    val buildToolsVersion = "29.0.2"
    val minSdkVersion = 19
    val targetSdkVersion = 29
    val versionCode = 10000
    val versionName = "1.0.0"
}

object Versions {
    val kotlin_version = "1.3.70"
    val build_gradle = "4.0.0"


    val appcompat = "1.1.0"
    val kts_core = "1.3.0"
    val constraintlayout = "2.0.2"
    val junit = "4.12"
    val junitExt = "1.1.1"
    val espressoCore = "3.2.0"
    val recyclerview = "1.1.0"
    val fragment = "1.2.5"
    val koin_version = "2.1.5"
    val lifecycle_version = "2.2.0"
}

object Deps {
    val kotlinStdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_version}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val ktsCode = "androidx.core:core-ktx:${Versions.kts_core}"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    val junit = "junit:junit:${Versions.junit}"
    val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val fragment = "androidx.fragment:fragment:${Versions.fragment}"

    object Lifecycle{
        val ktsViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    }

    object Koin{
        val viewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin_version}"
        val fragment = "org.koin:koin-androidx-fragment:${Versions.koin_version}"
    }
}
