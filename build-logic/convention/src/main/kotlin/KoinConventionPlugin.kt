import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class.java)

        dependencies {
            add("implementation", platform(libs.koin.bom))
            add("implementation", libs.koin.core)
        }

        pluginManager.withPlugin("com.android.application") {
            dependencies {
                add("implementation", libs.koin.android)
                add("implementation", libs.koin.androidx.compose)
                add("implementation", libs.koin.compose.viewmodel)
            }
        }
        pluginManager.withPlugin("com.android.library") {
            dependencies {
                add("implementation", libs.koin.android)
            }
        }
    }
}
