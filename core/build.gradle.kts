plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.10"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Koin Core (без Android-специфичных частей)
    implementation("io.insert-koin:koin-core:3.4.0")

    // Для работы с Result
    implementation("com.github.kittinunf.result:result:5.3.0")

    // Для будущего использования в domain
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
}