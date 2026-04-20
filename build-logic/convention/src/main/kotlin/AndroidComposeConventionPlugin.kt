import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class.java)

        pluginManager.withPlugin("com.android.application") {
            extensions.configure(ApplicationExtension::class.java) {
                buildFeatures.compose = true
            }
        }
        pluginManager.withPlugin("com.android.library") {
            extensions.configure(LibraryExtension::class.java) {
                buildFeatures.compose = true
            }
        }

        dependencies {
            add("implementation", platform(libs.compose.bom))
            add("androidTestImplementation", platform(libs.compose.bom))
            add("implementation", libs.compose.ui)
            add("implementation", libs.compose.ui.graphics)
            add("implementation", libs.compose.ui.tooling.preview)
            add("implementation", libs.compose.material3)
            add("debugImplementation", libs.compose.ui.tooling)
            add("debugImplementation", libs.compose.ui.test.manifest)
        }
    }
}
