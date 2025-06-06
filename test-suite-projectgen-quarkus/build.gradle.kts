plugins {
    id("io.micronaut.build.internal.projectgen-module")
}
dependencies {
    api(project(":micronaut-projectgen-core"))
    api(project(":micronaut-projectgen-features-gradle"))
    api(project(":micronaut-projectgen-features-javalibs"))
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    testImplementation(project(":micronaut-projectgen-test"))
}
