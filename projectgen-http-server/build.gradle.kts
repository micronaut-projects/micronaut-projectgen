plugins {
    id("io.micronaut.build.internal.projectgen-module")
}
dependencies {
    api(mn.micronaut.http.server)
    api(project(":micronaut-projectgen-core"))
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(libs.java.diff.utils)
    testRuntimeOnly(mnLogging.logback.classic)
}
