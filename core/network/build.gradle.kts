plugins {
    alias(libs.plugins.amro.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.amro.core.network"
}

dependencies {
    implementation(project(":core:model"))
    // Retrofit + OkHttp + kotlinx.serialization wiring lands in Phase 1.
}
