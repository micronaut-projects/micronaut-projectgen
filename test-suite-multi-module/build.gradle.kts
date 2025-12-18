plugins {
    `java-library`
    id("nu.studer.rocker") version "3.2"
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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
tasks.test {
    useJUnitPlatform()
}
rocker {
    configurations {
        create("main") {
            optimize.set(true)
            templateDir.set(file("src/rocker"))
            outputDir.set(file("src/generated/rocker"))
        }
    }
}
