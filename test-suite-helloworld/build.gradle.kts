plugins {
    `java-library`
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(platform("io.micronaut.platform:micronaut-platform:${libs.versions.micronaut.platform.get()}"))
    annotationProcessor(mn.micronaut.inject.java)
    api(project(":micronaut-projectgen-core"))
    testImplementation(project(":micronaut-projectgen-test"))
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
}
