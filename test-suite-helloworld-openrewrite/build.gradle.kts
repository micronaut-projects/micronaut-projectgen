plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
}

dependencies {
    api(project(":micronaut-projectgen-openrewrite"))
    api(project(":test-suite-helloworld"))
    testImplementation(libs.rewrite.test)
}
