plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
    `maven-publish`
}
dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    testAnnotationProcessor(mn.micronaut.inject.java)
    api(project(":micronaut-projectgen-core"))
}
group = "io.micronaut.projectgen"
version = rootProject.version
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}

