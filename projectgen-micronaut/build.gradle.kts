plugins {
    id("io.micronaut.build.internal.projectgen-module")
    id("nu.studer.rocker") version "3.0.4"
}
dependencies {
    api(project(":micronaut-projectgen-core"))
    api(project(":micronaut-projectgen-features-gradle"))
    annotationProcessor(mnSourcegen.micronaut.sourcegen.generator.java)
    implementation(mnSourcegen.micronaut.sourcegen.annotations)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(mnTest.junit.jupiter.params)
    testImplementation(project(":micronaut-projectgen-test"))
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

spotless {
    java {
        targetExclude("src/**/*.rocker.raw")
    }
}
