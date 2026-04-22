plugins {
    alias(libs.plugins.amro.android.library)
    alias(libs.plugins.amro.android.compose)
    alias(libs.plugins.amro.koin)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.amro.feature.movies"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

tasks.withType<Test>().configureEach {
    // Paparazzi and coroutine-test-based ViewModel tests both touch Dispatchers.Main.
    // Running them in the same JVM fork leaks state via MainDispatcherLoader, so
    // fork per test class to keep each class's dispatcher setup isolated.
    forkEvery = 1
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.coil.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.compose.viewmodel)

    testImplementation(libs.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.lifecycle.runtime.ktx)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}
