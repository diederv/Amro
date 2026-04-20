plugins {
    alias(libs.plugins.amro.android.application)
    alias(libs.plugins.amro.android.compose)
    alias(libs.plugins.amro.koin)
}

android {
    namespace = "com.amro.app"

    defaultConfig {
        applicationId = "com.amro.app"
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":feature:movies"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.navigation.compose)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.compose.ui.test.junit4)
}
