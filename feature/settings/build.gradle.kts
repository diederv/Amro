plugins {
    alias(libs.plugins.amro.android.library)
    alias(libs.plugins.amro.android.compose)
}

android {
    namespace = "com.amro.feature.settings"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:designsystem"))

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}
