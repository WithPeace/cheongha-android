import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.android(action: CommonExtension<*, *, *, *, *>.() -> Unit) {
    val androidExtension = extensions.getByName("android")
    if (androidExtension is CommonExtension<*, *, *, *, *>) {
        androidExtension.apply(action)
    }
}

internal fun Project.java(action: JavaPluginExtension.() -> Unit) {
    val javaPluginExtension = extensions.getByType<JavaPluginExtension>()
    javaPluginExtension.apply {
        action()
    }
}

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
