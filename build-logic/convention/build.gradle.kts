plugins {
    `kotlin-dsl`
}

group = "com.amro.buildlogic"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    // Make the version catalog type-safe accessors available to convention plugin code.
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("amroAndroidApplication") {
            id = "amro.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("amroAndroidLibrary") {
            id = "amro.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("amroAndroidCompose") {
            id = "amro.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("amroKotlinLibrary") {
            id = "amro.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("amroKoin") {
            id = "amro.koin"
            implementationClass = "KoinConventionPlugin"
        }
    }
}
