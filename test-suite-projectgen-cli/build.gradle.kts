plugins {
    id("io.micronaut.build.internal.projectgen-test-module")
}
dependencies {
    implementation(project(":micronaut-projectgen-core"))
    implementation(mnPicocli.picocli)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
}
