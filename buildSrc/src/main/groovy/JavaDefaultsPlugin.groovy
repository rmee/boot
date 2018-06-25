import org.gradle.api.Incubating
import org.gradle.api.Plugin
import org.gradle.api.Project

@Incubating
class JavaDefaultsPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.with {
			apply plugin: 'java'
			apply plugin: 'jacoco'
		}

	}
}
