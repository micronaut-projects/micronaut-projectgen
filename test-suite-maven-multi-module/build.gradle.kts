plugins {
    `java-library`
}
dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    compileOnly(mn.micronaut.inject.java)
    implementation(project(":micronaut-projectgen-core"))
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    testImplementation(project(":micronaut-projectgen-test"))
}
tasks.test {
    useJUnitPlatform()
}
