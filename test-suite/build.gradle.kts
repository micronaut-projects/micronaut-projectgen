plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
}
dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    testAnnotationProcessor(mn.micronaut.inject.java)
    api(project(":micronaut-projectgen-core"))
}
