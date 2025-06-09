plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
}
dependencies {
    api(project(":micronaut-projectgen-openrewrite"))
    implementation(libs.maven.invoker)
}
