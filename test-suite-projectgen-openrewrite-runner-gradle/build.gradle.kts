plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
}
repositories {
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases")
    }
}
dependencies {
    api(project(":micronaut-projectgen-openrewrite"))
    implementation(libs.gradle.tooling.api)
}
