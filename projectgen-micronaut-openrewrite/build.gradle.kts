plugins {
    id("io.micronaut.build.internal.projectgen-module")
}
dependencies {
    api(project(":micronaut-projectgen-openrewrite"))
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
}
