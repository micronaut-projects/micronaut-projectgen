plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
    `maven-publish`
}
dependencies {
    api(project(":micronaut-projectgen-openrewrite"))
    api(project(":test-suite-helloworld"))
    testImplementation(libs.rewrite.test)
}
group = "io.micronaut.projectgen"
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

