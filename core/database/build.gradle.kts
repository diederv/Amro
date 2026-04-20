plugins {
    alias(libs.plugins.amro.android.library)
    // Room + KSP wiring lands in Phase 1.
}

android {
    namespace = "com.amro.core.database"
}

dependencies {
    implementation(project(":core:model"))
}
