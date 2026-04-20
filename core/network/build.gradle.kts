plugins {
    alias(libs.plugins.amro.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.amro.core.network"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinxSerialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
